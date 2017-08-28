package com.lzugis.map.gis.draw.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.lzugis.map.cont.ParamData;
import com.lzugis.map.gis.draw.DrawMap;
import com.lzugis.map.utils.*;
import com.stmgis.task.map.utils.*;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import wContour.Contour;
import wContour.Interpolate;
import wContour.Global.Border;
import wContour.Global.PointD;
import wContour.Global.PolyLine;
import wContour.Global.Polygon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Geometry;

public class DrawMap_Web_Polygon extends DrawMap {

	public DrawMap_Web_Polygon() {
	}

	private double outMinX = 0;
	private double outMinY = 0;
	private double outMaxX = 0;
	private double outMaxY = 0;
	int xrow = 100;
	int yrow = 100;
	final FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2();

	private int imgW = 0;

	/**
	 * 根据参数绘制
	 */
	@SuppressWarnings("rawtypes")
	public boolean draw(ParamData pd) {
		boolean fd = true;
		try {
			JSONArray cityCodes = pd.getCityCode();
			boolean oldfd = true;
			for (int i = 0; i < cityCodes.size(); i++) {
				String cd = cityCodes.getString(i);
				super.setSavePathAndFc(pd, cd);
				oldfd = drawImage(pd.getLegend(), pd.getPoints(),
						pd.getImgName(), pd.getWidth(),
						pd.getHeight(), pd.getFc());
//				write2Shape(pd.getSavePath(), pd.getFc());
				System.err.println(pd.getImgName() + " : " + oldfd);
				fd = oldfd && fd;
			}
		} catch (Exception e) {
			return false;
		}
		
		return fd;
	}

	private boolean write2Shape(String savePath, FeatureCollection fc){
		boolean _success = false;
		CommonMethod cm = new CommonMethod();
		FeatureJSON fjson = new FeatureJSON();
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("{\"type\": \"FeatureCollection\",\"features\": ");
			savePath = savePath+"test.json";
			FeatureIterator itertor = fc.features();
			List<String> list = new ArrayList<String>();
			while (itertor.hasNext())
			{
				SimpleFeature feature = (SimpleFeature)itertor.next();
				StringWriter writer = new StringWriter();
				fjson.writeFeature(feature, writer);
				list.add(writer.toString());
			}
			itertor.close();
			sb.append(list.toString());
			sb.append("}");
			//写入文件
			cm.append2File(savePath, sb.toString(), true);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return _success;
	}

	@SuppressWarnings("rawtypes")
	private boolean drawImage(JSONArray vs,double[][] ps ,
			String savePath, int mapW, int mapH, FeatureCollection fc) {

		// 创建地图
		try {
			MapContent map = new MapContent();
			map.setTitle("绘制");

			// 设置输出范围
			CoordinateReferenceSystem WEB_CRS = CRS.decode("EPSG:4326", true);

			// 底图边界
			double layerMinX = fc.getBounds().getMinX();
			double layerMinY = fc.getBounds().getMinY();
			double layerMaxX = fc.getBounds().getMaxX();
			double layerMaxY = fc.getBounds().getMaxY();

			System.out.println(layerMinX+", "+layerMinY+", "+layerMaxX+", "+layerMaxY);

			ReferencedEnvelope mapArea = new ReferencedEnvelope(layerMinX,
					layerMaxX, layerMinY, layerMaxY, WEB_CRS);

			SimpleFeatureCollection grids = creatGrid(vs,
					ps, xrow, yrow, layerMinX, layerMinY, layerMaxX,
					layerMaxY);
			// 视野
			MapViewport viewport = new MapViewport(mapArea);
			map.setViewport(viewport);

			FeatureSource dc = clipFeatureCollection(fc, grids);

			String jsonPath = System.getProperty("user.dir")+"/out/";
			write2Shape(jsonPath, dc.getFeatures());

			if(dc ==null||dc.getFeatures().isEmpty()){
				return false;
			}
			Rule[] polygonrules = new Rule[1];
			if (vs != null && vs.size() > 0) {
				polygonrules = new Rule[vs.size()];
				for (int i = 0; i < vs.size(); i++) {
					JSONObject vsmap = vs.getJSONObject(i);
					if (vsmap.containsKey("minvalue")) {
						double minvalue = vsmap.getDoubleValue("minvalue");
						if (vsmap.containsKey("maxvalue")) {
							double maxvalue = vsmap.getDoubleValue("maxvalue");
							if (vsmap.containsKey("color")) {
								String[] cStr = vsmap.getString("color").split(
										",");
								Color c = new Color(Integer.parseInt(cStr[0]),
										Integer.parseInt(cStr[1]),
										Integer.parseInt(cStr[2]));
								String colorStr = ColorUtil.toHexFromColor(c);

								polygonrules[i] = StyleUtil.getPolygonRule2(
										"lvalue  > " + minvalue + "or lvalue ="
												+ minvalue
												+ "   and  hvalue < "
												+ maxvalue, "1", colorStr, "1",
										colorStr, "1");

							}
						}
					}

				}
			}
			Style pointstyle = StyleUtil.getStyleByrules(polygonrules);
			Layer dlayer = new FeatureLayer(dc, pointstyle);
			map.addLayer(dlayer);

			// 图像设置
			BufferedImage bi = new BufferedImage(mapW, mapH,
					BufferedImage.TYPE_4BYTE_ABGR_PRE);
			// 初始化渲染器
			StreamingRenderer sr = new StreamingRenderer();
			sr.setMapContent(map);

			// 初始化输出图像
			Graphics2D img = bi.createGraphics();

			img.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// 增加背景透明设置
			bi = img.getDeviceConfiguration().createCompatibleImage(
					mapW + imgW, mapH, Transparency.TRANSLUCENT);
			img = bi.createGraphics();

			Rectangle rect = new Rectangle(0, 0, mapW, mapH);

			// 绘制地图
			sr.paint(img, rect, mapArea);
//			map.dispose();
			JMapFrame.showMap(map);
			
			// 路径判断
			if (!savePath.contains(".")) {
				savePath = savePath + ".png";
			}
			File savePathFile = new File(savePath);
			// 写出图片
			ImageIO.write(bi, "png", savePathFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private FeatureSource clipFeatureCollection(FeatureCollection fc,
			SimpleFeatureCollection gs) {
		FeatureSource cs = null;
		try {
			List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
			FeatureIterator contourFeatureIterator = gs.features();
			FeatureIterator dataFeatureIterator = fc.features();
			while (dataFeatureIterator.hasNext()) {
				Feature dataFeature = dataFeatureIterator.next();
				Geometry dataGeometry = (Geometry) dataFeature.getProperty(
						"geometry").getValue();
				while (contourFeatureIterator.hasNext()) {
					Feature contourFeature = contourFeatureIterator.next();
					Geometry contourGeometry = (Geometry) contourFeature
							.getProperty("geometry").getValue();
					double lv = (Double) contourFeature.getProperty("lvalue")
							.getValue();
					double hv = (Double) contourFeature.getProperty("hvalue")
							.getValue();
					if (dataGeometry.intersects(contourGeometry)) {
						Geometry geo = dataGeometry
								.intersection(contourGeometry);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("the_geom", geo);
						map.put("lvalue", lv);
						map.put("hvalue", hv);
						values.add(map);
					}

				}

			}

			contourFeatureIterator.close();
			dataFeatureIterator.close();

			SimpleFeatureCollection sc = FeaureUtil
					.creatSimpleFeatureByFeilds(
							"polygons",
							"crs:4326,the_geom:MultiPolygon,lvalue:double,hvalue:double",
							values);
			cs = FeaureUtil.creatFeatureSourceByCollection(sc);

		} catch (Exception e) {
			e.printStackTrace();
			return cs;
		}

		return cs;
	}

	/**
	 * 生成格点
	 * 
	 * @param vs
	 *            图例
	 * @param _discreteData
	 *            原始点
	 * @param w
	 *            网格数 x
	 * @param h
	 *            网格数 y
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return
	 */
	@SuppressWarnings({ "unused" })
	private SimpleFeatureCollection creatGrid(JSONArray vs,
			double[][] _discreteData, int width, int height, double minX,
			double minY, double maxX, double maxY) {
		double[] _CValues = null;
		double _undefData = -9999.0;
		double[][] points = null;
		SimpleFeatureCollection polygonCollection = null;
		List<PolyLine> cPolylineList = new ArrayList<PolyLine>();
		List<Polygon> cPolygonList = new ArrayList<Polygon>();
		try {
			// 区间值
			if (vs != null && vs.size() > 0) {
				_CValues = new double[vs.size()];
				for (int i = 0; i < vs.size(); i++) {
					JSONObject map = vs.getJSONObject(i);
					if (map.containsKey("minvalue")) {
						double minvalue = map.getDoubleValue("minvalue");
						if (map.containsKey("maxvalue")) {
							double maxvalue = map.getDoubleValue("maxvalue");
							_CValues[i] = maxvalue;
						}
					}
				}
			} else {
				return null;
			}

			double[] _X = new double[width];
			double[] _Y = new double[height];

			Interpolate.CreateGridXY_Num(minX, minY, maxX, maxY, _X, _Y);
			double[][] _gridData = new double[width][height];

			int nc = _CValues.length;

			_gridData = Interpolate.Interpolation_IDW_Neighbor(_discreteData,
					_X, _Y, 12, _undefData);// IDW插值

			if (_gridData.length == 0) {
				return polygonCollection;
			}
			int[][] S1 = new int[_gridData.length][_gridData[0].length];
			List<Border> _borders = Contour.tracingBorders(_gridData, _X, _Y,
					S1, _undefData);
			cPolylineList = Contour.tracingContourLines(_gridData, _X, _Y, nc,
					_CValues, _undefData, _borders, S1);// 生成等值线
			cPolylineList = Contour.smoothLines(cPolylineList);// 平滑
			cPolygonList = Contour.tracingPolygons(_gridData, cPolylineList,
					_borders, _CValues);

			String geojsonpogylon = getPolygonGeoJson(cPolygonList);

			if (geojsonpogylon != null) {
				polygonCollection = GeoJSONUtil
						.readGeoJsonByString(geojsonpogylon);

			}

			return polygonCollection;

		} catch (Exception e) {
			e.printStackTrace();
			return polygonCollection;
		}

	}

	/**
	 * 获取等值面的geoJson
	 * 
	 * @param cPolygonList
	 *            等值面
	 * @return geoJson
	 */
	private static String getPolygonGeoJson(List<Polygon> cPolygonList) {
		String geo = null;
		String geometry = " { \"type\":\"Feature\",\"geometry\":";
		String properties = ",\"properties\":{ \"hvalue\":";

		String head = "{\"type\": \"FeatureCollection\"," + "\"features\": [";
		String end = "  ] }";
		if (cPolygonList == null || cPolygonList.size() == 0) {
			return null;
		}
		try {
			for (Polygon pPolygon : cPolygonList) {

				List<Object> ptsTotal = new ArrayList<Object>();
				List<Object> pts = new ArrayList<Object>();

				PolyLine pline = pPolygon.OutLine;

				for (PointD ptD : pline.PointList) {
					List<Double> pt = new ArrayList<Double>();
					pt.add(ptD.X);
					pt.add(ptD.Y);
					pts.add(pt);
				}

				ptsTotal.add(pts);

				if (pPolygon.HasHoles()) {
					for (PolyLine cptLine : pPolygon.HoleLines) {
						List<Object> cpts = new ArrayList<Object>();
						for (PointD ccptD : cptLine.PointList) {
							List<Double> pt = new ArrayList<Double>();
							pt.add(ccptD.X);
							pt.add(ccptD.Y);
							cpts.add(pt);
						}
						if (cpts.size() > 0) {
							ptsTotal.add(cpts);
						}
					}
				}

				JSONObject js = new JSONObject();
				js.put("type", "Polygon");
				js.put("coordinates", ptsTotal);
				double hv = pPolygon.HighValue;
				double lv = pPolygon.LowValue;

				if (hv == lv) {
					if (pPolygon.IsClockWise) {
						if (!pPolygon.IsHighCenter) {
							hv = hv - 0.1;
							lv = lv - 0.1;
						}

					} else {
						if (!pPolygon.IsHighCenter) {
							hv = hv - 0.1;
							lv = lv - 0.1;
						}
					}
				} else {
					if (!pPolygon.IsClockWise) {
						lv = lv + 0.1;
					} else {
						if (pPolygon.IsHighCenter) {
							hv = hv - 0.1;
						}
					}

				}

				geo = geometry + js.toString() + properties + hv
						+ ", \"lvalue\":" + lv + "} }" + "," + geo;

			}
			if (geo.contains(",")) {
				geo = geo.substring(0, geo.lastIndexOf(","));
			}

			geo = head + geo + end;
		} catch (Exception e) {
			e.printStackTrace();
			return geo;
		}
		return geo;

	}

}
