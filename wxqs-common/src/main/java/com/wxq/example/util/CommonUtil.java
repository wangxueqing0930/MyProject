package com.wxq.example.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommonUtil {

	private static final Class<?>[] baseTypes = { String.class, Integer.class,
			Byte.class, Long.class, Short.class, Float.class, Double.class,
			Character.class, Boolean.class, BigDecimal.class, BigInteger.class,
			Date.class };

	public synchronized static String getRandomFileName() {
		return UUID.randomUUID().toString();
	}

	/**
	 * <p>
	 * 判断一个类是不是基本的数据类型
	 * 
	 * @param clazz class类
	 * @return 是否基础类型
	 */
	public static boolean isBaseDataType(Class<?> clazz) {
		List<Class<?>> baseTypeList = Arrays.asList(baseTypes);
		return baseTypeList.contains(clazz) || clazz.isPrimitive();
	}

}

