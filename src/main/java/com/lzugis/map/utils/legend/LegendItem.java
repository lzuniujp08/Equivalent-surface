package com.lzugis.map.utils.legend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class LegendItem {

	
	 private int width = 0;
	 private int height = 0;
	 private int x  = 0;
	 private int y  = 0;
	 private String color = null;
	 private String caption = null;
	 
	 
	public LegendItem() {
	}

	
	public LegendItem(int width,int height,int x ,int y ,String color,String caption) {
	this.caption = caption;
	this.x = x;
	this.y = y;
	this.color = color;
	this.width = width;
	this.height = height;
	}
	
	
	public void draw(Graphics2D graphics){
		int cW = this.width*1/3;
		//对指定的矩形区域填充颜色
		int fontSiz = 14;
		if(height<14){
			fontSiz =height;
		}
		
		Font f2 = new Font("Helvetica", Font.BOLD,fontSiz);
		graphics.setFont(f2);
		graphics.setColor(toColorFromString(this.color));	
		graphics.fillRect(x, y, cW, height);
		//graphics.drawRect(x, y, width, height);
		graphics.setColor(toColorFromString("0x000000"));
		graphics.drawString(this.caption,x+cW+5,y+height*1/2+5);
	}
	
	
	 /** 
     * 字符串转换成Color对象 
     * @param colorStr 16进制颜色字符串 
     * @return Color对象 
     * */  
    private Color toColorFromString(String colorStr){ 
    	if(colorStr.contains("0x")||colorStr.contains("0X")){
    		colorStr = colorStr.substring(2,colorStr.length());
    	}
    	else if(colorStr.contains("#")){
    		colorStr = colorStr.substring(1,colorStr.length());
    	}
        Color color =  new Color(Integer.parseInt(colorStr, 16)) ;  
        return color;  
    }  
}
