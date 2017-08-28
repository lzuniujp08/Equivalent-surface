package com.lzugis.map.factory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import com.lzugis.map.cont.ParamData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzugis.map.cont.MapType;
import com.lzugis.map.param.MapParam;
import com.lzugis.map.utils.JSONUtil;

public class ParamFactory {

	@SuppressWarnings("unchecked")
	public static ParamData setPm(MapParam mp) {
		ParamData pd = new ParamData();
		try {

			if (mp != null) {

				pd.setMapType(mp.getMapType());
				pd.setDataType(mp.getDataType());
				pd.setFeatureType(mp.getFeatureType());

				pd.setDatatime(mp.getDataTime());

				pd.setLengonflag(mp.getLegendType());

				pd.setLogotitle(mp.getLogoTitle());

				pd.setTitle(mp.getTitle());

				HashMap<String, Object> osmap = new HashMap<String, Object>();
				if (mp.getCitycodeList() != null
						&& mp.getCitycodeList().size() > 0) {
					osmap.put("cs", mp.getCitycodeList());

					if (mp.getRenderColors() != null
							&& mp.getRenderColors().size() > 0) {
						osmap.put("rs", mp.getRenderColors());
					}

					if (mp.getDatas() != null && mp.getDatas().size() > 0) {
						osmap.put("data", mp.getDatas());
					}

					String os = JSONUtil.toJson(osmap);
					JSONObject osparamsObj = JSON.parseObject(os);
					if (osparamsObj.containsKey("rs")) {
						pd.setLegend(osparamsObj.getJSONArray("rs"));
					}
					if (osparamsObj.containsKey("cs")) {
						pd.setCityCode(osparamsObj.getJSONArray("cs"));
					}

					if (osparamsObj.containsKey("data")) {
						setPoints(osparamsObj.getJSONArray("data"), pd);
					}

					if (osparamsObj.containsKey("data")) {
						if (pd.getMapType() != null
								&& (pd.getMapType().toLowerCase()
										.equals(MapType.GRID_STATION) || pd
										.getMapType().toLowerCase()
										.equals(MapType.GRID_STATION_LEGEND))) {
							pd.setStations(osparamsObj.getJSONArray("data"));
						}

					}

				}
			}
			readProperty(pd);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("解析ParamData 出错 ParamFactory类 55行 ");
			return null;
		}
		// 检验数据
		if (!queck(pd)) {
			return null;
		}
		return pd;

	}

	/**
	 * 解析参数 生成参数对象
	 * 
	 * @param params
	 *            参数Json
	 * @return
	 */
	public static ParamData read(String params) {
		ParamData pd = new ParamData();
		try {
			if ("" != params) {
				JSONObject paramsObj = JSON.parseObject(params);
				if (paramsObj != null) {

					if (paramsObj.containsKey("mapType")) {
						pd.setMapType(paramsObj.getString("mapType"));
					}
					if (paramsObj.containsKey("dataType")) {
						pd.setDataType(paramsObj.getString("dataType"));
					}
					if (paramsObj.containsKey("featureType")) {
						pd.setFeatureType(paramsObj.getString("featureType"));
					}

					if (paramsObj.containsKey("datatime")) {
						pd.setDatatime(paramsObj.getString("datatime"));
					}

					if (paramsObj.containsKey("cityCode")) {
						pd.setCityCode(paramsObj.getJSONArray("cityCode"));
					}
					if (paramsObj.containsKey("areaType")) {
						pd.setAreaType(paramsObj.getString("areaType"));
					}

					if (paramsObj.containsKey("legendType")) {
						pd.setLengonflag(paramsObj.getString("legendType"));
					}

					if (paramsObj.containsKey("logotitle")
							|| paramsObj.containsKey("logoTitle")) {
						if (paramsObj.containsKey("logoTitle")) {
							pd.setLogotitle(paramsObj.getString("logoTitle"));
						}
						if (paramsObj.containsKey("logotitle")) {
							pd.setLogotitle(paramsObj.getString("logotitle"));
						}

					}

					if (paramsObj.containsKey("title")) {
						pd.setTitle(paramsObj.getString("title"));
					}

					if (paramsObj.containsKey("caption")) {
						pd.setTitle(paramsObj.getString("caption"));
					}

					if (paramsObj.containsKey("renderColors")) {
						JSONObject js = null;

						if (paramsObj.get("renderColors") instanceof String) {
							String jStr = paramsObj.getString("renderColors");
							js = JSON.parseObject(jStr);
						} else if (paramsObj.get("renderColors") instanceof JSONObject) {
							js = paramsObj.getJSONObject("renderColors");
						}

						if (js != null && js.containsKey("data")) {
							pd.setLegend(js.getJSONArray("data"));
						}

					}
					if (paramsObj.containsKey("imgName")) {
						pd.setImgName(paramsObj.getString("imgName"));
					}

					if (paramsObj.containsKey("data")) {
						com.alibaba.fastjson.JSONArray jsa = paramsObj
								.getJSONArray("data");
						setPoints(jsa, pd);
					}

					// ==================站点判断=======================//
					if (pd.getMapType() != null
							&& (pd.getMapType().toLowerCase()
									.equals(MapType.GRID_STATION) || pd
									.getMapType().toLowerCase()
									.equals(MapType.GRID_STATION_LEGEND))) {
						if (paramsObj.containsKey("data")) {
							com.alibaba.fastjson.JSONArray jsas = paramsObj
									.getJSONArray("data");
							pd.setStations(jsas);
						}
					}

				}
				readProperty(pd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("解析ParamData 出错 ParamFactory类 55行 ");
			return null;
		}
		// 检验数据
		if (!queck(pd)) {
			return null;
		}
		return pd;

	}

	/**
	 * 设置数据点
	 * 
	 * @param jsonArray
	 * @param pd
	 */
	private static void setPoints(com.alibaba.fastjson.JSONArray jsa,
			ParamData pd) {
		try {
			if (jsa != null && jsa.size() > 0) {
				double[][] ps = new double[3][jsa.size()];
				for (int i = 0; i < jsa.size(); i++) {
					JSONObject xyv = null;
					if (jsa.get(i) instanceof JSONObject) {
						xyv = jsa.getJSONObject(i);
					}
					if (xyv != null && xyv.containsKey("x")
							&& xyv.containsKey("y") && xyv.containsKey("v")) {
						ps[0][i] = xyv.getDoubleValue("x");
						ps[1][i] = xyv.getDoubleValue("y");
						ps[2][i] = xyv.getDoubleValue("v");
					}

				}
				pd.setPoints(ps);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 数据校验
	 * 
	 * @param dm
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean queck(ParamData pd) {
		// 数据校验
		if (pd == null) {
			System.err.println("ParamFactory类 ParamData为空");
			return false;
		}
		if (pd.getDataType() == null) {
			System.err.println("ParamFactory类 getDataType为空");
			return false;
		}

		if (pd.getFeatureType() == null) {
			System.err.println("ParamFactory类 getFeatureType为空");
			return false;
		}

		if (pd.getDataType() == null) {
			System.err.println("ParamFactory类 getDataType为空");
			return false;
		}

		if (pd.getDatatime() == null) {
			System.err.println("ParamFactory类 getDatatime为空");
			return false;
		}

		if (pd.getCityCode() == null) {
			System.err.println("ParamFactory类 getCityCode为空");
			return false;
		}
		if (pd.getLegend() == null) {
			System.err.println("ParamFactory类 getLegend为空");
			return false;
		}
		if (pd.getPoints() == null) {
			System.err.println("ParamFactory类 getPoints为空");
			return false;
		}
		if (pd.getSavePath() == null) {
			System.err.println("ParamFactory类 getCityCode为空");
			return false;
		}
		if (pd.getHeight() == 0) {
			System.err.println("ParamFactory类 getHeight为0");
			return false;
		}
		if (pd.getWidth() == 0) {
			System.err.println("ParamFactory类 getWidth为0");
			return false;
		}

		// ==================站点判断=======================//
		// grid_station 网格_带站点值
		if (pd.getMapType().toLowerCase().equals(MapType.GRID_STATION)) {
			if (pd.getStations() == null) {
				System.err.println("ParamFactory类 getStations为空");
				return false;
			}
		}
		// grid_legend 网格_带图例标题
		else if (pd.getMapType().toLowerCase().equals(MapType.GRID_LEGEND)) {
			if (pd.getTitle() == null) {
				System.err.println("ParamFactory类 getTitle为空");
				// return false;
			}
//			String cql = "CITYCODE not like '"+pd.getCityCode().getString(0)+"'";
//			FeatureCollection fc = ShpData.getFeatureSourceByGeoJson(cql);
//			if (fc == null || fc.isEmpty()) {
//				return false;
//			}
//			pd.setAreaFC(fc);
		}
		// grid_station_legend 网格_带站点值_带图例标题
		else if (pd.getMapType().toLowerCase()
				.equals(MapType.GRID_STATION_LEGEND)) {
			if (pd.getTitle() == null) {
				System.err.println("ParamFactory类 getTitle为空");
				// return false;
			}
			if (pd.getStations() == null) {
				System.err.println("ParamFactory类 getStations为空");
				return false;
			}
		}
		else if (pd.getMapType().toLowerCase()
				.equals(MapType.CONTOUR_LEGEND)){
//			String cql = "CITYCODE not like '"+pd.getCityCode().getString(0)+"'";
//			FeatureCollection fc = ShpData.getFeatureSourceByGeoJson(cql);
//			if (fc == null || fc.isEmpty()) {
//				return false;
//			}
//			pd.setAreaFC(fc);
		}
		return true;
	}

	/**
	 * 读取配置 宽 高 保存路径
	 * 
	 */
	private static void readProperty(ParamData pd) {
		String path = "/config/map.properties";
		Properties prop = new Properties();
		InputStream input = null;
		InputStreamReader inputStreamReader = null;
		try {
			input = Object.class.getResourceAsStream(path);
			inputStreamReader = new InputStreamReader(input, "utf-8");
			prop.load(inputStreamReader);
			pd.setSavePath(prop.getProperty("savePath"));
			pd.setWidth(Integer.parseInt(prop.getProperty("width")));
			pd.setHeight(Integer.parseInt(prop.getProperty("height")));
			;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
