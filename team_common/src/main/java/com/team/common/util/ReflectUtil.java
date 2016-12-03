package com.team.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author lwh
 * 
 */
public class ReflectUtil {
	/**
	 * 根据方法名获取Method
	 * 
	 * @param ownerClass
	 *            ownerClass
	 * @param methodName
	 *            methodName
	 * @return Method
	 */
	public static Method getMethod(Class<Object> ownerClass, String methodName) {
		Method[] methodList = ownerClass.getMethods();
		for (int x = 0; x < methodList.length; x++) {
			Method tempMethod = methodList[x];
			String temPMethodName = tempMethod.getName();
			if (temPMethodName.equals(methodName)) {
				return tempMethod;
			}
		}
		return null;
	}

	/**
	 * 根据方法名、参数个数获取Method
	 * 
	 * @param ownerClass
	 *            ownerClass
	 * @param methodName
	 *            methodName
	 * @param length
	 *            length
	 * @return Method
	 */
	public static Method getMethod(Class ownerClass, String methodName, int length) {
		Method[] methodList = ownerClass.getMethods();
		for (int x = 0; x < methodList.length; x++) {
			Method tempMethod = methodList[x];
			String temPMethodName = tempMethod.getName();
			if (temPMethodName.equals(methodName) && tempMethod.getParameterTypes().length == length) {
				return tempMethod;
			}
		}
		return null;
	}

	/**
	 * 检查接口类、实现类是否匹配，检查方法在接口类中是否定义
	 * 
	 * @param interFaceClass
	 *            interFaceClass
	 * @param instanceClass
	 *            instanceClass
	 * @param methodName
	 *            methodName
	 * @return boolean
	 */
	public static boolean checkInterFaceImpl(Class<Object> interFaceClass, Class<Object> instanceClass, String methodName) {
		if (!interFaceClass.isInterface()) {
			return false;
		}

		Method method = getMethod(interFaceClass, methodName);
		if (method == null) {
			return false;
		}

		boolean hasInterFace = false;
		Class<Object>[] classList = (Class<Object>[]) instanceClass.getInterfaces();
		for (int i = 0; i < classList.length; i++) {
			Class<Object> temp = classList[i];
			if (temp.equals(interFaceClass)) {
				hasInterFace = true;
			}
		}
		if (!hasInterFace) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * getMethodGenericParameterTypes
	 * 
	 * @param method
	 *            method
	 * @param index
	 *            index
	 * @return List<Class>
	 */
	public static List<Class> getMethodGenericParameterTypes(Method method, int index) {
		List<Class> results = new ArrayList<Class>();
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		Type genericParameterType = genericParameterTypes[index];
		if (genericParameterType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericParameterType;
			Type[] parameterArgTypes = aType.getActualTypeArguments();
			for (Type parameterArgType : parameterArgTypes) {
				Class parameterArgClass = (Class) parameterArgType;
				results.add(parameterArgClass);
			}
			return results;
		}
		return results;
	}

	// 分析ＤＴＯ中的get方法
	public static HashMap<String, Method> ParseDtoGet(Class dtoClass) throws Exception {
		HashMap<String, Method> readMethod = new HashMap<String, Method>();
		if (dtoClass == null) {
			throw new Exception("dtoClass must be initialed");
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(dtoClass);
		PropertyDescriptor[] props;

		props = beanInfo.getPropertyDescriptors();

		for (int i = 0; i < props.length; i++) {
			Method getMethod = props[i].getReadMethod();
			Class type = props[i].getPropertyType();
			if (getMethod != null) {
				String field = props[i].getName().toLowerCase();
				readMethod.put(field, getMethod);
			}
		}
		return readMethod;
	}

	public static HashMap<String, Method> ParseDtoSet(Class dtoClass) throws Exception {
		HashMap<String, Method> writeMethod = new HashMap<String, Method>();
		if (dtoClass == null) {
			throw new Exception("dtoClass must be initialed");
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(dtoClass);
		PropertyDescriptor[] props;

		props = beanInfo.getPropertyDescriptors();

		for (int i = 0; i < props.length; i++) {
			Method setMethod = props[i].getWriteMethod();
			Class type = props[i].getPropertyType();
			if (setMethod != null) {
				String field = props[i].getName().toLowerCase();
				writeMethod.put(field, setMethod);
			}
		}
		return writeMethod;
	}
	public static String[] getFields(Class dtoClass) {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(dtoClass);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
		PropertyDescriptor[] props;

		props = beanInfo.getPropertyDescriptors();
		String[] fieldArray = new String[props.length-1];
		for (int i = 1; i < props.length; i++) {
				String field = props[i].getName().toLowerCase();
				fieldArray[i-1] = field;

		}
		return fieldArray;
	}
	public static HashMap<String, Class> ParseDtoFieldType(Class dtoClass) throws Exception {
		HashMap<String, Class> fieldType = new HashMap<String, Class>();
		if (dtoClass == null) {
			throw new Exception("dtoClass must be initialed");
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(dtoClass);
		PropertyDescriptor[] props;

		props = beanInfo.getPropertyDescriptors();

		for (int i = 0; i < props.length; i++) {
			Class type = props[i].getPropertyType();
			if (type != null) {
				String field = props[i].getName().toLowerCase();
				fieldType.put(field, type);
			}
		}
		return fieldType;
	}

	public static Object invokeGetMethod(Object myDto, String fieldName) throws Exception {
		HashMap<String, Method> readMethod = ParseDtoGet(myDto.getClass());
		if (readMethod.containsKey(fieldName)) {
			Object[] myParams = new Object[0];
			Method myGetMethod = readMethod.get(fieldName);
			return myGetMethod.invoke(myDto, myParams);
		}
		return null;
	}

	public static Object invokeSetMethod(Object myDto, String fieldName, String value) throws Exception {
		HashMap<String, Method> readMethod = ParseDtoGet(myDto.getClass());
		HashMap<String, Class> fieldType = ParseDtoFieldType(myDto.getClass());
		if (readMethod.containsKey(fieldName)) {
			Object[] myParams = new Object[0];
			Method myGetMethod = readMethod.get(fieldName);
			Class fieldClass = fieldType.get(fieldName);

			if (fieldClass.equals(String.class)) {
				myParams[0] = value;
			} else if (fieldClass.equals(java.util.Date.class) || fieldClass.equals(java.sql.Date.class)) {
				if (value != null)
					myParams[0] = DateTimeUtil.getDateOBJ(DateTimeUtil.DATE_PATTERN, value);
			} else if (fieldClass.equals(Integer.class) || fieldClass.equals(int.class)) {
				if (value != null)
					myParams[0] = Integer.valueOf(value);
				else {
					if (fieldClass.equals(int.class)) {
						myParams[0] = new Integer(0);
					} else {
						myParams[0] = null;
					}
				}
			} else if (fieldClass.equals(Long.class) || fieldClass.equals(long.class)) {
				if (value != null)
					myParams[0] = Long.valueOf(value);
				else {
					if (fieldClass.equals(long.class)) {
						myParams[0] = new Long(0);
					} else {
						myParams[0] = null;
					}
				}
			} else if (fieldClass.equals(Short.class) || fieldClass.equals(short.class)) {

				if (value != null)
					myParams[0] = Short.valueOf(value);
				else {
					if (fieldClass.equals(short.class)) {
						myParams[0] = new Short((short) 0);
					} else {
						myParams[0] = null;
					}
				}
			} else if (fieldClass.equals(Float.class) || fieldClass.equals(float.class)) {
				if (value != null)
					myParams[0] = Float.valueOf(value);
				else {
					if (fieldClass.equals(float.class)) {
						myParams[0] = new Float(0);
					} else {
						myParams[0] = null;
					}
				}

			} else if (fieldClass.equals(Double.class) || fieldClass.equals(double.class)) {

				if (value != null)
					myParams[0] = Double.valueOf(value);
				else {
					if (fieldClass.equals(double.class)) {
						myParams[0] = new Double(0);
					} else {
						myParams[0] = null;
					}
				}
			} else if (fieldClass.equals(Boolean.class) || fieldClass.equals(boolean.class)) {

				if (value != null)
					myParams[0] = Boolean.valueOf(value);
				else {
					if (fieldClass.equals(boolean.class)) {
						myParams[0] = new Boolean(false);
					} else {
						myParams[0] = null;
					}
				}
			} else {
				throw new Exception("flatform can not surport the type :" + myDto.getClass() + " of field:" + fieldName);
			}
			return myGetMethod.invoke(myDto, myParams);
		}
		return null;
	}

	/**
	 * 获取obj对象fieldName的Field
	 *
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 获取obj对象fieldName的属性值
	 *
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * 设置obj对象fieldName的属性值
	 *
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}
}