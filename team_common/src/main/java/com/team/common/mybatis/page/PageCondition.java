package com.team.common.mybatis.page;

// TODO: Auto-generated Javadoc
/**
 * 分页参数类.
 */
public class PageCondition {

	/** The Constant DEFAULT_PAGE_SIZE. */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/** The page size. */
	private int pageSize;

	/** The current page. */
	private int currentPage;

	/** The pre page. */
	private int prePage;

	/** The next page. */
	private int nextPage;

	/** The total page. */
	private int totalPage;

	/** The total count. */
	private int totalCount;

	/**
	 * 添加排序，适合简单sql查询，由页面传递过来
	 */
	private String ordersql;

	/**
	 * Instantiates a new page parameter.
	 */
	public PageCondition() {
		this.currentPage = 1;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.nextPage = currentPage + 1;
		this.prePage = currentPage - 1;
	}

	/**
	 * Instantiates a new page parameter.
	 * 
	 * @param currentPage
	 *            int
	 * @param pageSize
	 *            int
	 */
	public PageCondition(int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.nextPage = currentPage + 1;
		this.prePage = currentPage - 1;
	}

	/**
	 * Gets the current page.
	 * 
	 * @return int
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Sets the current page.
	 * 
	 * @param currentPage
	 *            int
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		this.nextPage = currentPage + 1;
		this.prePage = currentPage - 1;
	}

	/**
	 * Gets the page size.
	 * 
	 * @return int
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the page size.
	 * 
	 * @param pageSize
	 *            the new page size
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Gets the pre page.
	 * 
	 * @return the pre page
	 */
	public int getPrePage() {
		return prePage;
	}

	/**
	 * Sets the pre page.
	 * 
	 * @param prePage
	 *            the new pre page
	 */
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	/**
	 * Gets the next page.
	 * 
	 * @return the next page
	 */
	public int getNextPage() {
		return nextPage;
	}

	/**
	 * Sets the next page.
	 * 
	 * @param nextPage
	 *            the new next page
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * Gets the total page.
	 * 
	 * @return the total page
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * Sets the total page.
	 * 
	 * @param totalPage
	 *            the new total page
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * Gets the total count.
	 * 
	 * @return the total count
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * Sets the total count.
	 * 
	 * @param totalCount
	 *            the new total count
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getOrdersql() {
		return ordersql;
	}

	public void setOrdersql(String ordersql) {
		this.ordersql = ordersql;
	}

}
