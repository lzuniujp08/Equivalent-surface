package com.lzugis.map.gis.draw.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.lzugis.map.cont.ParamData;
import com.lzugis.map.utils.GridUtil;
import com.lzugis.map.utils.legend.Legend;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
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
import com.lzugis.map.gis.draw.DrawMap;
import com.lzugis.map.utils.ColorUtil;
import com.lzugis.map.utils.FeaureUtil;
import com.lzugis.map.utils.GeoJSONUtil;
import com.lzugis.map.utils.MathUtil;
import com.lzugis.map.utils.StyleUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

public class DrawMap_Contour_LegendImpl extends DrawMap {

	public DrawMap_Contour_LegendImpl() {
	}

	private double xres = 1;
	private double yres = 1;
	private double outMinX = 0;
	private double outMinY = 0;
	private double outMaxX = 0;
	private double outMaxY = 0;
	@SuppressWarnings("rawtypes")
	private FeatureCollection areaFC = null;

	private static GeometryFactory geometryFactory = JTSFactoryFinder
			.getGeometryFactory(null);
	final FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2();
	private WKTReader reader = new WKTReader(geometryFactory);
	private List<Geometry> gridGeo = null;
	private String lengonflag = "or";

	// 间距
	private double xGip = 0;
	private double yGip = 0;
	// 生成网格多少行列
	private int gridRow = 500;
	private int gridColumn = 500;
	int xrow = 100;
	int yrow = 100;
	private int imgW = 0;

	/**
	 * 根据参数绘制
	 */
	public boolean draw(ParamData pd) {
		boolean fd = true;
		try {
			areaFC = pd.getAreaFC();
			imgW = pd.getWidth() / 8 * 1 + 5;
			lengonflag = pd.getLengonflag();
			JSONArray cityCodes = pd.getCityCode();
			boolean oldfd = true;
			for (int i = 0; i < cityCodes.size(); i++) {
				String cd = cityCodes.getString(i);
				super.setSavePathAndFc(pd, cd);

				oldfd = drawImage(pd.getLegend(), pd.getPoints(), pd.getFc(),
						pd.getImgName(), pd.getTitle(), pd.getWidth(),
						pd.getHeight());
				System.err.println(pd.getImgName() + " : " + oldfd);
				fd = oldfd && fd;
			}
			return fd;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param lg
	 *            图例
	 * @param points
	 *            double[3][...] <br>
	 *            [0][...] 经度 <br>
	 *            [1][...] 纬度<br>
	 *            [2][...] 值<br>
	 * 
	 * @param FeatureCollection
	 *            fc 底图要素
	 * 
	 * @param savePath
	 *            保存路径 带文件名
	 * @param titile
	 *            标题
	 * @param width
	 *            生成图片的宽
	 * 
	 * @param height
	 *            生成图片的高
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean drawImage(JSONArray vs, double[][] points,
			@SuppressWarnings("rawtypes") FeatureCollection fc,
			String savePath, String titile, int mapW, int mapH) {
		try {
			// 创建地图
			MapContent map = new MapContent();
			map.setTitle("绘制");

			// 初始化 底图
			Style style = StyleUtil.getPolygonStyle(null, "0xff0000", "1",
					"0xffffff", "0");
			// // 创建底图
			Layer layer = new FeatureLayer(fc, style);
			gridGeo = getGridGeometry(fc);

			// 设置输出范围
			CoordinateReferenceSystem WEB_CRS = CRS.decode("EPSG:4326", true);

			if (layer == null) {
				System.err.println("底图没有创建成功!");
				return false;
			} else {
				try {
					layer.getBounds();
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("底图没有创建成功! layer.getBounds()");
					return false;
				}
			}

			// 底图边界
			double layerMinX = layer.getBounds().getMinX();
			double layerMinY = layer.getBounds().getMinY();
			double layerMaxX = layer.getBounds().getMaxX();
			double layerMaxY = layer.getBounds().getMaxY();
			ReferencedEnvelope mapArea = new ReferencedEnvelope(layerMinX,
					layerMaxX, layerMinY, layerMaxY, WEB_CRS);
			// 输出边界
			outMinX = layerMinX - 0.15;
			outMinY = layerMinY - 0.15;
			outMaxX = layerMaxX + 0.15;
			outMaxY = layerMaxY + 0.15;
			ReferencedEnvelope outMapArea = new ReferencedEnvelope(outMinX,
					outMaxX, outMinY, outMaxY, WEB_CRS);


			// 网格边框
			ReferencedEnvelope gridMapArea = new ReferencedEnvelope(
					outMinX - 0.001, outMaxX - 0.01, outMinY + 0.008, outMaxY,
					WEB_CRS);

			@SuppressWarnings("rawtypes")
			FeatureSource gridfeatureSource = GridUtil
					.getSimpleFeatureSourceByCreateGrids(1, 1, gridMapArea);
			Style gridstyle = StyleUtil.getPolygonStyle(null, "0x000000", "1",
					"0xffffff", "1.0");
			Layer gridMarkLayer = new FeatureLayer(gridfeatureSource, gridstyle);
			map.addLayer(gridMarkLayer);

			Date sd_cg = new Date();
			SimpleFeatureCollection grids = creatGrid(vs,
					points, xrow, yrow, outMinX, outMinY, outMaxX,
					outMaxY);
//			// 输出边界
//			double wr = (layerMaxX - layerMinX) / (outMaxX - outMinX);
//			double hr = (layerMaxY - layerMinY) / (outMaxY - outMinY);
//			mapW = (int) (wr * mapW);
//			mapH = (int) (hr * mapH);


			// 视野
			MapViewport viewport = new MapViewport(mapArea);
			map.setViewport(viewport);

			FeatureSource dc = clipFeatureCollection(fc, grids);
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
			BufferedImage bi = new BufferedImage(mapW + imgW, mapH,
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

			// 计算比例 java绘制
			xres = mapW / (outMaxX - outMinX);
			yres = mapH / (outMaxY - outMinY);
			if (areaFC != null) {
				Style stylearea = StyleUtil.getPolygonStyle2(null, "0x000000",
						"1", "1", "0xffffff", "0");
				Layer layerarea = new FeatureLayer(areaFC, stylearea);
				layerarea.setVisible(true);
				map.addLayer(layerarea);

			}

			Rectangle rect = new Rectangle(0, 0, mapW, mapH);
			// 绘制地图
			sr.paint(img, rect, outMapArea);
			map.dispose();

			// 创建图例
			if (!creatLegend(vs, bi, mapW / 8 * 1, lengonflag)) {
				System.err.println("图例没有创建成功!");
				return false;
			}

			// 绘制标题
			Font f2 = new Font("Helvetica", Font.BOLD, 18);
			img.setFont(f2);
			img.setColor(ColorUtil.toColorFromString("0x000000"));
			img.drawString(titile, mapW / 5 * 1, 25);
			if (areaFC != null) {
				drawString(areaFC, img);
			}
			// 路径判断
			if (!savePath.contains(".")) {
				savePath = savePath + ".png";
			}
			// 写出图片
			ImageIO.write(bi, "png", new File(savePath));

			// JMapFrame.showMap(map);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	

	/**
	 * 创建图例
	 * 
	 * @param vs
	 *            图例Json对象
	 * @param img
	 *            图像对象
	 * @param w
	 *            宽度
	 * @param lR
	 *            "L"或者"R"
	 * @return
	 */
	private boolean creatLegend(JSONArray vs, BufferedImage img, int w,
			String lR) {

		if (vs != null && vs.size() > 0) {
			String[] data = new String[vs.size()];
			for (int i = 0; i < vs.size(); i++) {
				JSONObject vsmap = vs.getJSONObject(i);

				if (vsmap.containsKey("color") && vsmap.containsKey("caption")) {
					String[] cStr = vsmap.getString("color").split(",");
					Color c = new Color(Integer.parseInt(cStr[0]),
							Integer.parseInt(cStr[1]),
							Integer.parseInt(cStr[2]));
					String colorStr = ColorUtil.toHexFromColor(c);
					String title = vsmap.getString("caption");
					data[i] = colorStr + "," + title;
				} else {
					System.err.println("creatLegend方法未获取到 color legend");
					return false;
				}
			}

			Legend l = new Legend(
					data, w, lR);
			return l.draw(img);
		}
		return false;
	}

	/**
	 * 获取面
	 * 
	 * @param gridfeatureSource
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Geometry> getGridGeometry(FeatureCollection fs) {
		Geometry g = null;
		SimpleFeatureIterator itertor;
		List<Geometry> returnLsit = new ArrayList<Geometry>();
		try {
			itertor = (SimpleFeatureIterator) fs.features();
			while (itertor.hasNext()) {
				SimpleFeature feature = itertor.next();
				g = (Geometry) feature.getDefaultGeometry();
				returnLsit.add(g);
			}
			itertor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return returnLsit;
		}

		return returnLsit;
	}

	private void drawString(FeatureCollection fc, Graphics2D graphics) {
		SimpleFeatureIterator itertor;
		try {
			itertor = (SimpleFeatureIterator) fc.features();
			while (itertor.hasNext()) {
				SimpleFeature feature = itertor.next();
				Font font = new Font("TimesRoman", Font.PLAIN, 16);
				Geometry g = (Geometry) feature.getDefaultGeometry();
				Coordinate c = g.getEnvelope().getEnvelopeInternal().centre();
				double lon = c.x;
				double lat = c.y;
				// 设置文字颜色
				int x = MathUtil.getIntNatData((lon - outMinX) * xres, 0);
				int y = MathUtil.getIntNatData((outMaxY - lat) * yres, 0);
				String name = feature.getProperty("NAME_2").getValue()
						.toString();
				graphics.setFont(font);
				graphics.setColor(new Color(00, 00, 00));
				graphics.drawString(name, x, y);
			}
			itertor.close();
		} catch (Exception e) {
			e.printStackTrace();
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
//				Geometry dataGeometry = (Geometry) dataFeature.getProperty(
//						"the_geom").getValue();
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
			// _gridData = Interpolate.Interpolation_IDW_Radius(_discreteData,
			// _X, _Y,
			// 3, 2, _undefData);

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

				// if(hv==lv){
				// if(pPolygon.IsClockWise){
				// if(!pPolygon.IsHighCenter){
				// hv=hv-0.1;
				// lv=lv-0.1;
				// }
				//
				// }
				// else{
				// //lv=lv-0.1;
				// }
				// }else{
				// if(!pPolygon.IsClockWise){
				// lv=lv+0.1;
				// }
				// else{
				// if(pPolygon.IsHighCenter){
				// hv=hv-0.1;
				// }
				// }
				//
				// }

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

				// System.err.println(hv
				// +"   "+lv+" bor "+pPolygon.IsBorder+" c "+pPolygon.IsClockWise+" i "+pPolygon.IsInnerBorder+" h "+pPolygon.IsHighCenter);

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
