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
import org.geotools.data.FeatureSource;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
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
//import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import wContour.Interpolate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzugis.map.gis.draw.DrawMap;
import com.lzugis.map.utils.ColorUtil;
import com.lzugis.map.utils.FeaureUtil;
import com.lzugis.map.utils.MathUtil;
import com.lzugis.map.utils.StyleUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class DrawMap_GridImpl extends DrawMap {

	private double xres = 1;
	private double yres = 1;
	private double outMinX = 0;
	private double outMinY = 0;
	private double outMaxX = 0;
	private double outMaxY = 0;
	@SuppressWarnings("rawtypes")
	private FeatureCollection areaFC = null;

	final FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2();
	private static GeometryFactory geometryFactory = JTSFactoryFinder
			.getGeometryFactory(null);
	private WKTReader reader = new WKTReader(geometryFactory);
	private List<Geometry> gridGeo = null;

	// 间距
	private double xGip = 0;
	private double yGip = 0;
	// 生成网格多少行列
	private int gridRow = 500;
	private int gridColumn = 500;

	/**
	 * 根据参数绘制
	 */
	public boolean draw(ParamData pd) {
		try {
			areaFC = pd.getAreaFC();
			JSONArray cityCodes = pd.getCityCode();
			boolean fd = true;
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
	private boolean drawImage(JSONArray lg, double[][] points,
			FeatureCollection fc, String savePath, String titile, int mapW,
			int mapH) {
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
			outMinX = layerMinX - 0.1;
			outMinY = layerMinY - 0.1;
			outMaxX = layerMaxX + 0.1;
			outMaxY = layerMaxY + 0.1;
			ReferencedEnvelope outMapArea = new ReferencedEnvelope(outMinX,
					outMaxX, outMinY, outMaxY, WEB_CRS);

			// 视野
			MapViewport viewport = new MapViewport(mapArea);
			map.setViewport(viewport);

			// 网格
			double[][] gridPoints = creatGrid(lg, points, gridRow, gridColumn,
					layerMinX, layerMinY, layerMaxX, layerMaxY);
			if (gridPoints != null) {
				Layer gridlayer = getGridLayer(lg, gridPoints);
				if (gridlayer != null) {
					map.addLayer(gridlayer);
				} else {
					System.err.println("网格没有生成! pointlayer为空!");
					return false;
				}
			} else {
				System.err.println("网格没有生成! gridPoints为空!");
				return false;
			}
			map.addLayer(layer);
			// 图像设置
			BufferedImage bi = new BufferedImage(mapW, mapH,
					BufferedImage.TYPE_4BYTE_ABGR_PRE);

			// 初始化渲染器
			StreamingRenderer sr = new StreamingRenderer();

			sr.setMapContent(map);

			// 初始化输出图像
			Graphics2D img = bi.createGraphics();
			// 计算比例 java绘制
			xres = mapW / (outMaxX - outMinX);
			yres = mapH / (outMaxY - outMinY);
			if (areaFC != null) {
				Style stylearea = StyleUtil.getPolygonStyle2(null, "0xff0000",
						"1", "1", "0xffffff", "0");
				Layer layerarea = new FeatureLayer(areaFC, stylearea);
				layerarea.setVisible(true);
				map.addLayer(layerarea);

			}
			img.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// 增加背景透明设置
			bi = img.getDeviceConfiguration().createCompatibleImage(mapW, mapH,
					Transparency.TRANSLUCENT);

			img = bi.createGraphics();

			Rectangle rect = new Rectangle(0, 0, mapW, mapH);
			// 绘制地图
			sr.paint(img, rect, outMapArea);
			map.dispose();
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
	private double[][] creatGrid(JSONArray vs, double[][] _discreteData,
			int width, int height, double minX, double minY, double maxX,
			double maxY) {
		double[] _CValues = null;
		double _undefData = -9999.0;
		double[][] points = null;

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
			// 求间距
			if (_X != null && _X.length > 1) {
				xGip = _X[1] - _X[0];
			}
			// 求间距
			if (_Y != null && _Y.length > 1) {
				yGip = _Y[1] - _Y[0];
			}
			int i = 0;
			points = new double[3][width * height];
			for (int y = 0; y < _Y.length; y++) {
				for (int x = 0; x < _X.length; x++) {
					points[0][i] = _X[x];
					points[1][i] = _Y[y];
					points[2][i] = _gridData[y][x];
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return points;
	}

	/**
	 * 生成网格图层
	 * 
	 * @param vs
	 *            图例
	 * @param points
	 *            网格点
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Layer getGridLayer(JSONArray vs, double[][] points) {
		Layer pointlayer = null;

		try {
			if (xGip == 0 || yGip == 0) {
				System.err.println("getPointLayer方法  xGip==0或者yGip==0");
				return null;
			}

			Date sd = new Date();
			// 要素
			if (points != null && points.length > 0) {
				List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < points[0].length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					double sx = points[0][i];
					double sy = points[1][i];
					double ex = sx + xGip;
					double ey = sy + yGip;
					String cs = sx + " " + sy + " ," + sx + " " + ey + " ,"
							+ ex + " " + ey + " ," + ex + " " + sy + " ," + sx
							+ " " + sy;
					Polygon polygon = (Polygon) reader.read("POLYGON((" + cs
							+ "))");

					map.put("the_geom", polygon);
					map.put("value", MathUtil.getDoubleNatData(points[2][i], 6));
					values.add(map);
				}

				SimpleFeatureCollection sc = FeaureUtil
						.creatSimpleFeatureByFeilds("polygons",
								"crs:4326,the_geom:Polygon,value:double",
								values);
				FeatureSource fs = FeaureUtil
						.creatFeatureSourceByCollection(sc);
				ListFeatureCollection fss = new ListFeatureCollection(
						FeaureUtil.creatSimpleFeatureType("ps",
								"crs:4326,the_geom:Polygon,value:double"));
				// 过滤
				if (gridGeo != null && gridGeo.size() > 0) {
					for (Geometry g : gridGeo) {
						try {
							if (g != null) {
								Filter filter = ff2.intersects(
										ff2.property("the_geom"),
										ff2.literal(g));
								SimpleFeatureCollection fs_tmp = (SimpleFeatureCollection) fs
										.getFeatures(filter);
								SimpleFeatureIterator itertor;
								try {
									itertor = (SimpleFeatureIterator) fs_tmp
											.features();
									while (itertor.hasNext()) {
										SimpleFeature feature = itertor.next();
										fss.add(feature);
									}
									itertor.close();
								} catch (Exception e) {
									e.printStackTrace();
									continue;
								}

							}

						} catch (Exception e) {
							continue;
						}

					}

				}
				Rule[] polygonrules = new Rule[1];
				if (vs != null && vs.size() > 0) {
					polygonrules = new Rule[vs.size()];
					for (int i = 0; i < vs.size(); i++) {
						JSONObject vsmap = vs.getJSONObject(i);
						if (vsmap.containsKey("minvalue")) {
							double minvalue = vsmap.getDoubleValue("minvalue");
							if (vsmap.containsKey("maxvalue")) {
								double maxvalue = vsmap
										.getDoubleValue("maxvalue");
								if (vsmap.containsKey("color")) {
									String[] cStr = vsmap.getString("color")
											.split(",");
									Color c = new Color(
											Integer.parseInt(cStr[0]),
											Integer.parseInt(cStr[1]),
											Integer.parseInt(cStr[2]));
									String colorStr = ColorUtil
											.toHexFromColor(c);
									// System.err.println(colorStr);
									polygonrules[i] = StyleUtil
											.getPolygonRule2("value  > "
													+ minvalue + "or value ="
													+ minvalue
													+ "   and  value < "
													+ maxvalue, "0",
													"0x000000", "0.5",
													colorStr, "1");
								} else {
									System.err
											.println("getPointLayer方法未获取到 color legend");
									return null;
								}
							} else {
								System.err
										.println("getPointLayer方法未获取到 maxvalue legend");
								return null;
							}
						} else {
							System.err
									.println("getPointLayer方法未获取到 minvalue legend");
							return null;
						}

					}
				}
				Style pointstyle = StyleUtil.getStyleByrules(polygonrules);
				pointlayer = new FeatureLayer(fss, pointstyle);

				// 网格
				Date se = new Date();
				System.err.println("图层 花费时间是 : "
						+ ((se.getTime() - sd.getTime()) / 1000) + " 秒");

				return pointlayer;

			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

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
}
