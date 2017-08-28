package com.lzugis.map.utils;

import java.awt.Color;

public class ColorUtil {

	public ColorUtil() {
	}

	 /** 
     * Color对象转换成字符串 
     * @param color Color对象 
     * @return 16进制颜色字符串 
     * */  
	public static String toHexFromColor(Color color){  
        String r,g,b;  
        StringBuilder su = new StringBuilder();  
        r = Integer.toHexString(color.getRed());  
        g = Integer.toHexString(color.getGreen());  
        b = Integer.toHexString(color.getBlue());  
        r = r.length() == 1 ? "0" + r : r;  
        g = g.length() ==1 ? "0" +g : g;  
        b = b.length() == 1 ? "0" + b : b;  
        r = r.toUpperCase();  
        g = g.toUpperCase();  
        b = b.toUpperCase();  
        su.append("0x");  
        su.append(r);  
        su.append(g);  
        su.append(b);  
        //0xFF0000FF  
        return su.toString();  
    } 
	
	 /** 
     * 字符串转换成Color对象 
     * @param colorStr 16进制颜色字符串 
     * @return Color对象 
     * */  
	public static Color toColorFromString(String colorStr){ 
    	
    	if(colorStr.contains("0x")||colorStr.contains("0X")){
    		colorStr = colorStr.substring(2,colorStr.length());
    	}
    	else if(colorStr.contains("#")){
    		colorStr = colorStr.substring(1,colorStr.length());
    	}
         
        Color color =  new Color(Integer.parseInt(colorStr, 16)) ;  
        //java.awt.Color[r=0,g=0,b=255]  
        return color;  
    } 
    
    
    
}
