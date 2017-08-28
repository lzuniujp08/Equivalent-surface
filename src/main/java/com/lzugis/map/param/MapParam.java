package com.lzugis.map.param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * 制图参数类
 * 
 * @author hhw
 *
 */
public class MapParam implements Serializable{
	/**
	 * 制图数据集合
	 * Map<String,Object>  = {"x":100,"y":25,"v":233,"sName":"杭州站"}
	 */
	private List datas;
	
	/**
	 * 数据时间
	 */
	private String dataTime;
	
	/**
	 * 城市编码集合（同一份数据可用于多个市县出图）
	 */
	private List<String> citycodeList;
	
	/**
	 * 地图类型，一般分为GIS图，图例图等
	 */
	private String mapType;
	
	/**
	 * 标题
	 */
	private String title = null;
	/**
	 * 图例类型
	 */
	private String legendType = "r";
	
	/**
	 * 底部标题
	 */
	private String logoTitle = null;
	/**
	 * 数据类型
	 */
	private String dataType = null;
	/**
	 * 要素类型
	 */
	private String featureType = null;
	

	/**
	 * 出图色带
	 * Map<String,Object>  = {"caption":">0","maxvalue":100,"minvalue":90,color":"146,22,14"}
	 */
	private List<Map<String,Object>> renderColors = null;
	
	public List getDatas() {
		return datas;
	}

	public void setDatas(List datas) {
		this.datas = datas;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public List<String> getCitycodeList() {
		return citycodeList;
	}

	public void setCitycodeList(List<String> citycodeList) {
		this.citycodeList = citycodeList;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLegendType() {
		return legendType;
	}

	public void setLegendType(String legendType) {
		this.legendType = legendType;
	}

	public String getLogoTitle() {
		return logoTitle;
	}

	public void setLogoTitle(String logoTitle) {
		this.logoTitle = logoTitle;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public List<Map<String, Object>> getRenderColors() {
		return renderColors;
	}

	public void setRenderColors(List<Map<String, Object>> renderColors) {
		this.renderColors = renderColors;
	}
	
	
}
