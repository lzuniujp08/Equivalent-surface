package com.lzugis.map.cont;

import java.util.List;

import org.geotools.feature.FeatureCollection;

import com.alibaba.fastjson.JSONArray;

/**
 * 参数类
 * 
 * @author lhy
 *
 */
/**
 * @author Administrator
 *
 */
public class ParamData {

	public ParamData() {
	}


	/**保存路径*/
	private String savePath = null;
	/**保存文件名*/
	private String imgName = null;
	/**宽 */
	private int width = 0;
	/**高*/
	private int height = 0;
	/**底图要素*/
	@SuppressWarnings("rawtypes")
	private FeatureCollection  fc =null;
	/**
	 * 数据类 要生成哪一类图<br>
	 * //grid 网格<br>
	 * //grid_station 网格_带站点值<br>
	 * //grid_legend 网格_带图例标题<br>
	 * //grid_station_legend 网格_带站点值_带图例标题<br>
	 */
	private String mapType = null;
	private String dataType = null;
	private String featureType = null;
	private String datatime = null;
	/**
	 * 城市编码
	 */
	private JSONArray cityCode = null;


	/**
	 * 标题 //grid_legend 网格_带图例标题<br>
	 * //grid_station_legend 网格_带站点值_带图例标题<br>
	 * 上面两种 需要
	 */
	private String title = null;

	private String lengonflag = "or";
	
	private String logotitle = null;
	
	/**
	 * 点,格式 [3][....]<br>
	 * [0][0] x<br>
	 * [1][0] y<br>
	 * [2][0] v<br>
	 * x y v 是一个整体 一个点的值<br>
	 */
	private double[][] points = null;
	/**
	 * 图例
	 */
	private JSONArray legend = null;
	
	/**
	 * 站点  [{"sNum":111,"sName":"北京站","x":106,"y":45}]
	 */
	private JSONArray stations = null;

	private  String areaType = null;
	
	private FeatureCollection  areaFC =null;
	
	


	/**
	 * 标题 //grid_legend 网格_带图例标题<br>
	 * //grid_station_legend 网格_带站点值_带图例标题<br>
	 * 上面两种 需要
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 标题 //grid_legend 网格_带图例标题<br>
	 * //grid_station_legend 网格_带站点值_带图例标题<br>
	 * 上面两种 需要
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 点,格式 [3][....]<br>
	 * [0][0] x<br>
	 * [1][0] y<br>
	 * [2][0] v<br>
	 * x y v 是一个整体 一个点的值<br>
	 * 
	 * @return double[][] points
	 */
	public double[][] getPoints() {
		return points;
	}

	/**
	 * 点,格式 [3][....]<br>
	 * [0][0] x<br>
	 * [1][0] y<br>
	 * [2][0] v<br>
	 * x y v 是一个整体 一个点的值<br>
	 */
	public void setPoints(double[][] points) {
		this.points = points;
	}

	

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@SuppressWarnings("rawtypes")
	public FeatureCollection getFc() {
		return fc;
	}

	@SuppressWarnings("rawtypes")
	public void setFc(FeatureCollection fc) {
		this.fc = fc;
	}

	public JSONArray getLegend() {
		return legend;
	}

	public void setLegend(JSONArray legend) {
		this.legend = legend;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public JSONArray getStations() {
		return stations;
	}

	public void setStations(JSONArray stations) {
		this.stations = stations;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public FeatureCollection getAreaFC() {
		return areaFC;
	}

	public void setAreaFC(FeatureCollection areaFC) {
		this.areaFC = areaFC;
	}

	public String getLengonflag() {
		return lengonflag;
	}

	public void setLengonflag(String lengonflag) {
		this.lengonflag = lengonflag;
	}

	public String getLogotitle() {
		return logotitle;
	}

	public void setLogotitle(String logotitle) {
		this.logotitle = logotitle;
	}


	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getDatatime() {
		return datatime;
	}

	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public JSONArray getCityCode() {
		return cityCode;
	}

	public void setCityCode(JSONArray cityCode) {
		this.cityCode = cityCode;
	}


}
