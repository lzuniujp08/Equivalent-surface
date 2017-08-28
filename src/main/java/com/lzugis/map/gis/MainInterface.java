package com.lzugis.map.gis;

import java.util.Date;


import com.lzugis.map.cont.ParamData;
import com.lzugis.map.factory.DrawMapFactory;
import com.lzugis.map.factory.ParamFactory;
import com.lzugis.map.gis.draw.DrawMap;
import com.lzugis.map.param.MapParam;

/**
 * 程序入口
 * @author lhy
 *
 */
public class MainInterface {

	public MainInterface() {
	}

	/**
	 * 解析参数Json,绘制底图
	 * @param param 参数Json
	 * @return 成功还是失败 
	 */
	public static boolean readParamToDrawMap(MapParam mp) {
		try {
			Date sd = new Date();
			
			// 解析参数
			ParamData pd = ParamFactory.setPm(mp);

			if(pd==null){
				System.err.println("解析参数失败!");
				return false;
			}
			// 根据参数去绘制
			DrawMap dm = DrawMapFactory.read(pd);
			if(dm==null){
				System.err.println("生成DrawMap失败!");
				return false;
			}
			// 开始绘制
			boolean result = dm.draw(pd);
			Date se = new Date();
			System.err.println("一共花费时间是: "+((se.getTime()-sd.getTime())/1000) +" 秒");
			pd.setAreaFC(null);
			pd.setFc(null);
			pd = null;
			dm = null;
			System.gc();
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * 解析参数Json,绘制底图
	 * @param param 参数Json
	 * @return 成功还是失败 
	 */
	public static boolean readParamToDrawMap(String param) {
		try {
			Date sd = new Date();
			// 解析参数
			ParamData pd = ParamFactory.read(param);
			if(pd==null){
				System.err.println("解析参数失败!");
				return false;
			}
			// 根据参数去绘制
			DrawMap dm = DrawMapFactory.read(pd);
			if(dm==null){
				System.err.println("生成DrawMap失败!");
				return false;
			}
			// 开始绘制
			boolean result = dm.draw(pd);
			Date se = new Date();
			System.err.println("一共花费时间是: "+((se.getTime()-sd.getTime())/1000) +" 秒");
			pd.setAreaFC(null);
			pd.setFc(null);
			pd = null;
			dm = null;
			System.gc();
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
