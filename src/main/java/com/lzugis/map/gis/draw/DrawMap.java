package com.lzugis.map.gis.draw;

import java.io.File;

import com.lzugis.map.cont.ParamData;
import com.lzugis.map.cont.ShpData;
import org.geotools.feature.FeatureCollection;

public class DrawMap {
	/**
	 * 根据参数绘制
	 * @return 成功 或者 失败
	 */
	public boolean draw(ParamData pd) {
		return false;
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	public boolean setSavePathAndFc(ParamData pd,String cd){
		String returnStr = null;
		String tmapfileStr = null;
		File file  = null;
		returnStr = pd.getSavePath()
		+"/"+cd
	//	+"/"+pd.getDataType()
		+"/"+pd.getFeatureType();
		
		tmapfileStr = returnStr;
		returnStr = returnStr
		+"/"+pd.getMapType()
		+"_"+pd.getDatatime()+".png";
		
		if(tmapfileStr!= null){
			file = new File(tmapfileStr);
			if(!file.exists()){
				file.mkdirs();
			}
		}
		if(returnStr == null){
			return false;
		}
		pd.setImgName(returnStr);
		
		String cql = "CITYCODE = " + cd;
		//FeatureCollection fc = ShpData.getFeatureSourceByShp(cql);
		FeatureCollection fc = ShpData.getFeatureSourceByGeoJson(cql);
		if (fc == null || fc.isEmpty()) {
			return false;
		}
		pd.setFc(fc);
		return true;
	}

}
