package com.team.common.mybatis.page;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import com.team.common.util.ReflectUtil;

/**
 * 通过拦截<code>StatementHandler</code>的<code>prepare</code>方法，重写sql语句实现物理分页。
 * 老规矩，签名里要拦截的类型只能是接口。.
 * 
 * @author 湖畔微风
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PageInterceptor implements Interceptor {

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(PageInterceptor.class);

	/** The Constant DEFAULT_OBJECT_FACTORY. */
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

	/** The Constant DEFAULT_OBJECT_WRAPPER_FACTORY. */
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	/** The default dialect. */
	private static String defaultDialect = "mysql"; // 数据库类型(默认为mysql)

	/** The default page sql id. */
	private static String defaultPageSqlId = ".*Page$"; // 需要拦截的ID(正则匹配)

	/** The dialect. */
	private static String dialect = ""; // 数据库类型(默认为mysql)

	/** The page sql id. */
	private static String pageSqlId = ""; // 需要拦截的ID(正则匹配)

	/**
	 * Intercept.
	 * 
	 * @param invocation
	 *            the invocation
	 * @return the object
	 * @throws Throwable
	 *             the throwable
	 * @version
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
		// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
		}
		// 分离最后一个代理对象的目标类
		while (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
		}
		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		dialect = configuration.getVariables().getProperty("dialect");
		if (null == dialect || "".equals(dialect)) {
			logger.warn("Property dialect is not setted,use default 'mysql' ");
			dialect = defaultDialect;
		}
		pageSqlId = configuration.getVariables().getProperty("pageSqlId");
		if (null == pageSqlId || "".equals(pageSqlId)) {
			logger.warn("Property pageSqlId is not setted,use default '.*Page$' ");
			pageSqlId = defaultPageSqlId;
		}
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		// 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
		if (mappedStatement.getId().matches(pageSqlId)) {
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			Object parameterObject = boundSql.getParameterObject();
			if (parameterObject == null) {
				throw new NullPointerException("parameterObject is null!");
			} else {
				PageCondition page = (PageCondition) metaStatementHandler.getValue("delegate.boundSql.parameterObject.page");
				if (page == null) {
					page = new PageCondition();
				}

				Connection connection = (Connection) invocation.getArgs()[0];

				String sql = boundSql.getSql();

				checkOrderSql(page, connection);

				// 重写sql
				String pageSql = buildPageSql(sql, page);
				metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
				// 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
				metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
				metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
				// 重设分页参数里的总页数等
				setPageParameter(sql, connection, mappedStatement, boundSql, page);
			}
		}
		// 将执行权交给下一个拦截器
		return invocation.proceed();
	}

	private void checkOrderSql(PageCondition page, Connection connection) throws SQLException {

		if (page.getOrdersql() != null) {
			String ordersql = page.getOrdersql().toLowerCase();
			ordersql = ordersql.replace("order", "");
			ordersql = ordersql.replace("by", "");
			String[] filds = ordersql.split(",");
			if (filds != null && filds.length > 0) {
				for (int i = 0; i < filds.length; i++) {
					String fild = filds[i].trim();
					fild = fild.replace("desc", "");
					fild = fild.replace("asc", "");
					String[] fildname = fild.split("\\.", 2);
					String checkfild = "";
					if (fildname.length == 1) {
						checkfild = fildname[0].trim();
					} else if (fildname.length == 2) {
						checkfild = fildname[1].trim();
					} else {
						logger.error("checkOrderSql error,fild size error, fild: " + fild);
						page.setOrdersql("");
						return;
					}

					String sql = "select count(column_name) count from information_schema.columns where table_schema =  'zhubajie_bjtt' and column_name = ? ";
					PreparedStatement ps = connection.prepareStatement(sql);
					ps.setString(1, checkfild);

					ResultSet rs = ps.executeQuery();
					ResultSetMetaData meta = rs.getMetaData();
					int culumnCount = meta.getColumnCount();

					while (rs.next()) {
						for (int j = 1; j <= culumnCount; j++) {
							// 每列记录信息
							String fieldName = meta.getColumnName(j).toLowerCase();
							String count = rs.getString(fieldName);

							if (count != null && !"".equals(count.trim())) {
								if (Integer.valueOf(count) == 0) {
									logger.error("checkOrderSql error, can not find fild: " + fild + " in information_schema.columns ");
									page.setOrdersql("");
									return;
								}
							} else {
								logger.error("checkOrderSql error, can not find count is null, fild : " + fild);
								page.setOrdersql("");
								return;
							}
						}
					}

				}
			}
		}

	}

	/**
	 * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用者就可用通过 分页参数
	 * <code>PageParameter</code>获得相关信息。.
	 * 
	 * @param sql
	 *            the sql
	 * @param connection
	 *            the connection
	 * @param mappedStatement
	 *            the mapped statement
	 * @param boundSql
	 *            the bound sql
	 * @param page
	 *            the page
	 * @version
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql, PageCondition page)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		// 查询总数的时候需要去掉asc desc排序内容

		int index = sql.lastIndexOf("order by");
		if (index > 0) {
			sql = sql.substring(0, index - 1);
		}

		// 记录总记录数
		String countSql = "select count(0) from (" + sql + ") as total";
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(),
					boundSql.getParameterObject());

			// 由于该物理分页不支持mybatis的<foreach>标签，so对该分页做一下更改
			Field metaParamsField = ReflectUtil.getFieldByFieldName(boundSql, "metaParameters");
			if (metaParamsField != null) {
				MetaObject mo = (MetaObject) ReflectUtil.getValueByFieldName(boundSql, "metaParameters");
				ReflectUtil.setValueByFieldName(countBS, "metaParameters", mo);
			}

			setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
			rs = countStmt.executeQuery();
			int totalCount = 0;
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}

			page.setTotalCount(totalCount);
			int totalPage = totalCount / page.getPageSize() + ((totalCount % page.getPageSize() == 0) ? 0 : 1);
			page.setTotalPage(totalPage);

		} catch (SQLException e) {
			logger.error("Ignore this exception", e);
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
			try {
				if (null != countStmt) {
					countStmt.close();
				}
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
		}

	}

	/**
	 * 对SQL参数(?)设值.
	 * 
	 * @param ps
	 *            the ps
	 * @param mappedStatement
	 *            the mapped statement
	 * @param boundSql
	 *            the bound sql
	 * @param parameterObject
	 *            the parameter object
	 * @throws SQLException
	 *             the sQL exception
	 * @version
	 */
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		parameterHandler.setParameters(ps);
	}

	/**
	 * 根据数据库类型，生成特定的分页sql.
	 * 
	 * @param sql
	 *            the sql
	 * @param page
	 *            the page
	 * @return the string
	 * @return
	 */
	private String buildPageSql(String sql, PageCondition page) {
		if (page != null) {
			StringBuilder pageSql = new StringBuilder();
			if ("mysql".equals(dialect)) {
				pageSql = buildPageSqlForMysql(sql, page);
			} else if ("oracle".equals(dialect)) {
				pageSql = buildPageSqlForOracle(sql, page);
			} else if ("sybase".equals(dialect)) {
				pageSql = buildPageSqlForSybase(sql, page);
			} else {
				return sql;
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	/**
	 * mysql的分页语句.
	 * 
	 * @param sql
	 *            the sql
	 * @param page
	 *            the page
	 * @return String
	 * @version
	 */
	public StringBuilder buildPageSqlForMysql(String sql, PageCondition page) {

		StringBuilder pageSql = new StringBuilder(100);
		String beginrow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
		pageSql.append(sql);
		pageSql.append(" limit " + beginrow + "," + page.getPageSize());
		return pageSql;
	}

	/**
	 * 使用临时表完成分页.为防止临时表数据过大，当查询的数据起始数超过总数的一半后，
	 * 采用逆序的方式查询数据，并在临时表里再采用相反的顺序将数据重新排序。 因此在使用 sybase分页查询时，必须显示的指定排序字段和排序顺序。
	 * 
	 * @param sql
	 *            the sql
	 * @param page
	 *            the page
	 * @return String
	 * @version
	 */
	public static StringBuilder buildPageSqlForSybase(String sql, PageCondition page) {
		StringBuilder pageSql = new StringBuilder(100);
		int beginrow = (page.getCurrentPage() - 1) * page.getPageSize();
		int endrow = page.getCurrentPage() * page.getPageSize();

		// 临时表随机命名，防止名称冲突
		sql = sql.toLowerCase();
		String temp = "#temp" + new Random().nextInt(1000000);
		String fromSql = sql.substring(sql.indexOf("from"));

		String selectsqlcontext = sql.substring(sql.indexOf("select") + 6, sql.indexOf("from"));
		// String order = "";
		String tempOrder = "asc";
		/*
		 * if (beginrow * 2 > page.getTotalCount()) { if
		 * (fromSql.lastIndexOf("desc") > 0) { // order = "asc"; order = "desc";
		 * fromSql = fromSql.substring(0, fromSql.lastIndexOf("desc")) + order;
		 * } else if (fromSql.lastIndexOf("asc") > 0) { // order = "desc"; order
		 * = "asc"; fromSql = fromSql.substring(0, fromSql.lastIndexOf("asc")) +
		 * order; } // tempOrder = "desc"; tempOrder = "asc"; }
		 */
		pageSql.append("rollback  set chained off   ").append(" ");
		pageSql.append("select top ").append(endrow).append(selectsqlcontext).append(",rownum=identity(int) into ").append(temp).append(" ");
		pageSql.append(fromSql).append(" ");
		pageSql.append("select * from ").append(temp).append(" where rownum > ").append(beginrow).append(" order by rownum ").append(tempOrder)
				.append(" ");
		pageSql.append("drop table " + temp);

		return pageSql;
	}

	/**
	 * 参考hibernate的实现完成oracle的分页.
	 * 
	 * @param sql
	 *            the sql
	 * @param page
	 *            the page
	 * @return String
	 * @version
	 */
	public StringBuilder buildPageSqlForOracle(String sql, PageCondition page) {
		StringBuilder pageSql = new StringBuilder(100);
		String beginrow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
		String endrow = String.valueOf(page.getCurrentPage() * page.getPageSize());

		pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
		pageSql.append(sql);
		pageSql.append(" ) temp where rownum <= ").append(endrow);
		pageSql.append(") where row_id > ").append(beginrow);
		return pageSql;
	}

	/**
	 * Plugin.
	 * 
	 * @param target
	 *            the target
	 * @return the object
	 * @version
	 */
	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	/**
	 * Sets the properties.
	 * 
	 * @param properties
	 *            the new properties
	 */
	@Override
	public void setProperties(Properties properties) {
	}

}
