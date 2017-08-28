package com.lzugis.map.utils;

import java.math.BigDecimal;

public class MathUtil {

	public MathUtil() {
	}

	/**
	 * 四舍五入取值
	 * @param data 要四舍五入取值的数字
	 * @param len 留几位小数
	 * @return
	 */
	public static double getNatData(String data,int len){
		String result = String.format("%."+len+"f", data);
		return  Double.parseDouble(result);
	}
	
	
	
	/**
	 * 四舍五入取值
	 * @param data 要四舍五入取值的数字
	 * @param len 留几位小数
	 * @return
	 */
	public static float getNatData(float data,int len){
		String result = String.format("%."+len+"f", data);
		return  Float.parseFloat(result);
	}
	
	/**
	 * 四舍五入取值
	 * @param data 要四舍五入取值的数字
	 * @param len 留几位小数
	 * @return
	 */
	public static Float getFloatNatData(double data,int len){
		String result = String.format("%."+len+"f", data);
		return  Float.parseFloat(result);
	}
	
	/**
	 * 四舍五入取值
	 * @param data 要四舍五入取值的数字
	 * @param len 留几位小数
	 * @return
	 */
	public static Double getDoubleNatData(double data,int len){
		String result = String.format("%."+len+"f", data);
		return  Double.parseDouble(result);
	}
	
	/**
	 * 四舍五入取值
	 * @param data 要四舍五入取值的数字
	 * @param len 留几位小数
	 * @return
	 */
	public static int getIntNatData(double data,int len){
		String result = String.format("%."+len+"f", data);
		return  Integer.parseInt(result);
	}
	
	/**
	 * 四舍五入取值
	 * @param data 要四舍五入取值的数字
	 * @param len 留几位小数
	 * @return
	 */
	public static double getNatData(double data,int len){
		BigDecimal bd = new BigDecimal(data).setScale(len,
				BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * 取小数后多少位
	 * @param data 要取小数的数字
	 * @param len 留几位小数
	 * @return
	 */
	public static double getIndexData(double data,int len){
		String dataStr = data+"";
		String dataXiaoShuStr = "0";
		if(dataStr.contains(".")){
			dataXiaoShuStr = dataStr.substring(dataStr.indexOf("."), dataStr.length());
			dataStr = dataStr.substring(0, dataStr.indexOf("."));
		}
		if(dataXiaoShuStr.length()>=len+1){
			dataXiaoShuStr = dataXiaoShuStr.substring(0, len+1);
		}
		dataStr = dataStr + dataXiaoShuStr;
		return Double.parseDouble(dataStr);
	}
	
	/**
	 * 舍掉小数取整
	 * @param data舍掉小数取整的数字
	 * @return
	 */
	public static double getDoubleDataNotNat(double data){
		
		return Math.floor(data);
	}
	
	/**
	 * 舍掉小数取整
	 * @param data舍掉小数取整的数字
	 * @return
	 */
	public static float getFloatDataNotNat(double data){
		
		return Float.parseFloat( Math.floor(data)+"");
	}
	
	
}
