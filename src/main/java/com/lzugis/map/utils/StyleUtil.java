package com.lzugis.map.utils;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.FilterFactory;

/**
 * geotools样式创建工具类
 * 
 * @author Administrator
 *
 */
public class StyleUtil {

	public StyleUtil() {
	}

	private static StyleBuilder sb = new StyleBuilder();// sf的功能大部分可以通过 sb来完成
	private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
	private static FilterFactory ff = CommonFactoryFinder
			.getFilterFactory(null);

	/**
	 * 获取 点样式
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度
	 * @param size
	 *            点大小
	 * @return Style
	 */
	public static Style getPointStyle(String cql, String strokeColor,
			String strokeWith, String fillColor, String fillkeAlpl, String size) {

		Rule[] rules = new Rule[1];
		// 1. 创建点符号 ----------------------------------------------------------
		Graphic gr = sf.createDefaultGraphic();
		Mark mark = sf.getCircleMark();

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));
		mark.setStroke(stroke);
		mark.setFill(sf.createFill(ff.literal(fillColor),
				ff.literal(fillkeAlpl)));
		// 清空原来的符号清单
		gr.graphicalSymbols().clear();
		// 把新符号添加到清单里
		gr.graphicalSymbols().add(mark);

		// 设置大小
		gr.setSize(ff.literal(size));

		PointSymbolizer sym = sf.createPointSymbolizer(gr, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}
		rules[0] = rule;

		FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);
		Style style = sf.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * 获取 线样式
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @return Style
	 */
	public static Style getLineStyle(String cql, String strokeColor,
			String strokeWith) {

		Rule[] rules = new Rule[1];

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));

		LineSymbolizer sym = sf.createLineSymbolizer(stroke, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}
		rules[0] = rule;

		FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);
		Style style = sf.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * 获取 面样式
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillBackgroundColor
	 *            填充颜色 背景
	 * @param fillkeAlpl
	 *            填充透明度 点大小
	 * @return Style
	 */
	public static Style getPolygonStyle(String cql, String strokeColor,
			String strokeWith, String fillColor, String fillBackgroundColor,
			String fillkeAlpl) {

		Rule[] rules = new Rule[1];

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));

		Fill fill = sf
				.createFill(ff.literal(fillColor), ff.literal(fillkeAlpl));

		PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}
		rules[0] = rule;

		FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);
		Style style = sf.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * 获取 面样式
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度 点大小
	 * @return Style
	 */
	public static Style getPolygonStyle(String cql, String strokeColor,
			String strokeWith, String fillColor, String fillkeAlpl) {

		Rule[] rules = new Rule[1];

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));

		Fill fill = sf
				.createFill(ff.literal(fillColor), ff.literal(fillkeAlpl));

		PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}
		rules[0] = rule;

		FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);
		Style style = sf.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}
	
	
	/**
	 * 获取 面样式
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @param strokeAlpl
	 *           边框透明度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度 
	 * @return Style
	 */
	public static Style getPolygonStyle2(String cql, String strokeColor,
			String strokeWith, String strokeAlpl,String fillColor, String fillkeAlpl) {

		Rule[] rules = new Rule[1];

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith), ff.literal(strokeAlpl));

		Fill fill = sf
				.createFill(ff.literal(fillColor), ff.literal(fillkeAlpl));

		PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}
		rules[0] = rule;

		FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);
		Style style = sf.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * 获取 点 文本 样式
	 * 
	 * @param x
	 *            位置 0
	 * @param y
	 *            位置 0.6
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色 "#000000"
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度
	 * @param size
	 *            点大小
	 * @param textColor
	 *            文本颜色
	 * @param fontSize
	 *            字体的大小
	 * @param fontColor
	 *            光晕的颜色
	 * @param feild
	 *            生成文本的字段
	 * @return Style
	 */
	public static Style getPointTextStyle(double x, double y, String cql,
			String strokeColor, String strokeWith, String fillColor,
			String fillkeAlpl, String size, String textColor, String fontSize,
			String fontColor, String feild) {

		Rule[] rules = new Rule[1];

		// 1. 创建文本注记 ---------------------------------------------
		// 注记出现的位置
		AnchorPoint anchorPoint = sb.createAnchorPoint(x, y);
		// 用于控制注记的旋转角度和位置
		PointPlacement pointPlacement = sb.createPointPlacement(anchorPoint,
				null, ff.literal(0));

		// 文本注记
		TextSymbolizer textSymbolizer = sb.createTextSymbolizer(
				sf.createFill(ff.literal(textColor)),
				new Font[] { sf.createFont(ff.literal("微软雅黑"),
						ff.literal("italic"), ff.literal("100"),
						ff.literal(fontSize)) }, null,
				sb.attributeExpression(feild), pointPlacement, null);

		// 2. 创建点符号 ----------------------------------------------------------
		Graphic gr = sf.createDefaultGraphic();
		Mark mark = sf.getCircleMark();

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));
		mark.setStroke(stroke);
		mark.setFill(sf.createFill(ff.literal(fillColor),
				ff.literal(fillkeAlpl)));
		// 清空原来的符号清单
		gr.graphicalSymbols().clear();
		// 把新符号添加到清单里
		gr.graphicalSymbols().add(mark);

		// 设置大小
		gr.setSize(ff.literal(size));

		PointSymbolizer sym = sf.createPointSymbolizer(gr, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		rule.symbolizers().add(textSymbolizer);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}
		rules[0] = rule;

		FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);
		Style style = sf.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * 获取 文本 样式 * @param x 位置 0
	 * 
	 * @param y
	 *            位置 0.6
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param textColor
	 *            文本颜色
	 * @param fontSize
	 *            字体的大小
	 * @param fontColor
	 *            光晕的颜色
	 * @param feild
	 *            生成文本的字段
	 * @param ration
	 *            旋转角度
	 * @return Style
	 */
	public static Style getTextStyle(double x, double y, String cql,
			String textColor, String fontSize, String fontColor, String feild,
			String ration) {

		Rule[] rules = new Rule[1];

		// 1. 创建文本注记 ---------------------------------------------
		// 注记出现的位置
		AnchorPoint anchorPoint = sb.createAnchorPoint(x, y);
		// 用于控制注记的旋转角度和位置
		PointPlacement pointPlacement = sb.createPointPlacement(anchorPoint,
				null, ff.literal(ration));
		// 文本注记
		TextSymbolizer textSymbolizer = sb.createTextSymbolizer(
				sf.createFill(ff.literal(textColor)),
				new Font[] { sf.createFont(ff.literal("微软雅黑"),
						ff.literal("italic"), ff.literal("100"),
						ff.literal(fontSize)) }, null,
				sb.attributeExpression(feild), pointPlacement, null);
		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(textSymbolizer);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}

		} catch (CQLException e) {
			e.printStackTrace();
		}
		rules[0] = rule;

		FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);
		Style style = sf.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * 获取 点渲染规则
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度
	 * @param size
	 *            点大小
	 * @return Style
	 */
	public static Rule getPointRule(String cql, String strokeColor,
			String strokeWith, String fillColor, String fillkeAlpl, String size) {

		// 1. 创建点符号 ----------------------------------------------------------
		Graphic gr = sf.createDefaultGraphic();
		Mark mark = sf.getCircleMark();

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));
		mark.setStroke(stroke);
		mark.setFill(sf.createFill(ff.literal(fillColor),
				ff.literal(fillkeAlpl)));
		// 清空原来的符号清单
		gr.graphicalSymbols().clear();
		// 把新符号添加到清单里
		gr.graphicalSymbols().add(mark);

		// 设置大小
		gr.setSize(ff.literal(size));

		PointSymbolizer sym = sf.createPointSymbolizer(gr, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}

	/**
	 * 获取 线样式-渲染规则
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @return Style
	 */
	public static Rule getLineRule(String cql, String strokeColor,
			String strokeWith) {

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));

		LineSymbolizer sym = sf.createLineSymbolizer(stroke, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}

	/**
	 * 获取 面样式-渲染规则
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度 点大小
	 * @return Style
	 */
	public static Rule getPolygonRule(String cql, String strokeColor,
			String strokeWith, String fillColor, String fillBackgroundColor,
			String fillkeAlpl) {

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));
		Graphic gr = sf.createDefaultGraphic();
		Mark mark = sf.getCircleMark();

		mark.setStroke(stroke);
		mark.setFill(sf.createFill(ff.literal(fillColor),
				ff.literal(fillkeAlpl)));
		// 清空原来的符号清单
		gr.graphicalSymbols().clear();
		// 把新符号添加到清单里
		gr.graphicalSymbols().add(mark);

		// 设置大小
		gr.setSize(ff.literal(20));

		Fill fill = sf.createFill(ff.literal(fillColor),
				ff.literal(fillBackgroundColor), ff.literal(fillkeAlpl), gr);

		PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}

	/**
	 * 获取 面样式-渲染规则
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度 点大小
	 * @return Style
	 */
	public static Rule getPolygonRule(String cql, String strokeColor,
			String strokeWith, String fillColor, String fillkeAlpl) {

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));

		Fill fill = sf
				.createFill(ff.literal(fillColor), ff.literal(fillkeAlpl));

		PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}
	
	

	/**
	 * 获取 面样式-渲染规则
	 * 
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeAlpl
	 *            边框透明度
	 * @param strokeColor
	 *            边框颜色
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度 点大小
	 * @return Style
	 */
	public static Rule getPolygonRule2(String cql, String strokeAlpl, String strokeColor,
			String strokeWith, String fillColor, String fillkeAlpl) {

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith),ff.literal(strokeAlpl));

		Fill fill = sf
				.createFill(ff.literal(fillColor), ff.literal(fillkeAlpl));

		PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}

	/**
	 * 获取 点 文本 样式
	 * 
	 * @param x
	 *            位置 0
	 * @param y
	 *            位置 0.6
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色 "#000000"
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度
	 * @param size
	 *            点大小
	 * @param textColor
	 *            文本颜色
	 * @param fontSize
	 *            字体的大小
	 * @param fontColor
	 *            光晕的颜色
	 * @param feild
	 *            生成文本的字段
	 * @return Style
	 */
	public static Rule getPointTextRule(double x, double y, String cql,
			String strokeColor, String strokeWith, String fillColor,
			String fillkeAlpl, String size, String textColor, String fontSize,
			String fontColor, String feild) {

		// 1. 创建文本注记 ---------------------------------------------
		// 注记出现的位置
		AnchorPoint anchorPoint = sb.createAnchorPoint(x, y);
		// 用于控制注记的旋转角度和位置
		PointPlacement pointPlacement = sb.createPointPlacement(anchorPoint,
				null, ff.literal(0));

		// 文本注记
		TextSymbolizer textSymbolizer = sb.createTextSymbolizer(
				sf.createFill(ff.literal(textColor)),
				new Font[] { sf.createFont(ff.literal("微软雅黑"),
						ff.literal("italic"), ff.literal("100"),
						ff.literal(fontSize)) }, null,
				sb.attributeExpression(feild), pointPlacement, null);

		// 2. 创建点符号 ----------------------------------------------------------
		Graphic gr = sf.createDefaultGraphic();
		Mark mark = sf.getCircleMark();

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));
		mark.setStroke(stroke);
		mark.setFill(sf.createFill(ff.literal(fillColor),
				ff.literal(fillkeAlpl)));
		// 清空原来的符号清单
		gr.graphicalSymbols().clear();
		// 把新符号添加到清单里
		gr.graphicalSymbols().add(mark);

		// 设置大小
		gr.setSize(ff.literal(size));

		PointSymbolizer sym = sf.createPointSymbolizer(gr, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		rule.symbolizers().add(textSymbolizer);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}
	
	
	/**
	 * 获取 点 文本 样式
	 * 
	 * @param x
	 *            位置 0
	 * @param y
	 *            位置 0.6
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param strokeColor
	 *            边框颜色 "#000000"
	 * @param strokeWith
	 *            边框厚度
	 * @param fillColor
	 *            填充颜色
	 * @param fillkeAlpl
	 *            填充透明度
	 * @param size
	 *            点大小
	 * @param textColor
	 *            文本颜色
	 * @param fontSize
	 *            字体的大小
	 * @param fontColor
	 *            光晕的颜色
	 * @param feild
	 *            生成文本的字段
	 * @return Style
	 */
	public static Rule getPointTextRule(double x, double y, String cql,
			String strokeColor, String strokeWith, String fillColor,
			String fillkeAlpl, String size, String textColor, String fontSize,
			String fontColor, String feild,String fontStyle) {

		// 1. 创建文本注记 ---------------------------------------------
		// 注记出现的位置
		AnchorPoint anchorPoint = sb.createAnchorPoint(x, y);
		// 用于控制注记的旋转角度和位置
		PointPlacement pointPlacement = sb.createPointPlacement(anchorPoint,
				null, ff.literal(0));

		// 文本注记
		TextSymbolizer textSymbolizer = sb.createTextSymbolizer(
				sf.createFill(ff.literal(textColor)),
				new Font[] { sf.createFont(ff.literal("微软雅黑"),
						ff.literal(fontStyle), ff.literal("100"),
						ff.literal(fontSize)) }, null,
				sb.attributeExpression(feild), pointPlacement, null);

		// 2. 创建点符号 ----------------------------------------------------------
		Graphic gr = sf.createDefaultGraphic();
		Mark mark = sf.getCircleMark();

		// 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
		Stroke stroke = sf.createStroke(ff.literal(strokeColor),
				ff.literal(strokeWith));
		mark.setStroke(stroke);
		mark.setFill(sf.createFill(ff.literal(fillColor),
				ff.literal(fillkeAlpl)));
		// 清空原来的符号清单
		gr.graphicalSymbols().clear();
		// 把新符号添加到清单里
		gr.graphicalSymbols().add(mark);

		// 设置大小
		gr.setSize(ff.literal(size));

		PointSymbolizer sym = sf.createPointSymbolizer(gr, null);

		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(sym);
		rule.symbolizers().add(textSymbolizer);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}
		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}

	/**
	 * 获取 文本 样式
	 * 
	 * @param x
	 *            位置 0
	 * @param y
	 *            位置 0.6
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param textColor
	 *            文本颜色
	 * @param fontSize
	 *            字体的大小
	 * @param fontColor
	 *            光晕的颜色
	 * @param feild
	 *            生成文本的字段
	 * @param ration
	 *            旋转角度
	 * @return Style
	 */
	public static Rule getTextRule(double x, double y, String cql,
			String textColor, String fontSize, String feild, String ration) {

		// 1. 创建文本注记 ---------------------------------------------
		// 注记出现的位置
		AnchorPoint anchorPoint = sb.createAnchorPoint(x, y);
		// 用于控制注记的旋转角度和位置
		PointPlacement pointPlacement = sb.createPointPlacement(anchorPoint,
				null, ff.literal(ration));
		// 文本注记
		TextSymbolizer textSymbolizer = sb.createTextSymbolizer(
				sf.createFill(ff.literal(textColor)),
				new Font[] { sf.createFont(ff.literal("微软雅黑"),
						ff.literal("italic"), ff.literal("100"),
						ff.literal(fontSize)) }, null,
				sb.attributeExpression(feild), pointPlacement, null);
		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(textSymbolizer);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}

		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}
	
	/**
	 * 获取 文本 样式
	 * 
	 * @param x
	 *            位置 0
	 * @param y
	 *            位置 0.6
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param textColor
	 *            文本颜色
	 * @param fontSize
	 *            字体的大小
	 * @param fontColor
	 *            光晕的颜色
	 * @param feild
	 *            生成文本的字段
	 * @param ration
	 *            旋转角度
	 * @param fontStyle  normal italic             
	 * @return Style 
	 */
	public static Rule getTextRule(double x, double y, String cql,
			String textColor, String fontSize, String feild, String ration,String fontStyle) {

		// 1. 创建文本注记 ---------------------------------------------
		// 注记出现的位置
		AnchorPoint anchorPoint = sb.createAnchorPoint(x, y);
		// 用于控制注记的旋转角度和位置
		PointPlacement pointPlacement = sb.createPointPlacement(anchorPoint,
				null, ff.literal(ration));
		// 文本注记
		TextSymbolizer textSymbolizer = sb.createTextSymbolizer(
				sf.createFill(ff.literal(textColor)),
				new Font[] { sf.createFont(ff.literal("微软雅黑"),
						ff.literal(fontStyle), ff.literal("100"),
						ff.literal(fontSize)) }, null,
				sb.attributeExpression(feild), pointPlacement, null);
		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(textSymbolizer);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}

		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}

	/**
	 * 获取 文本 样式
	 * 
	 * @param x
	 *            位置 0
	 * @param y
	 *            位置 0.6
	 * @param cql
	 *            过滤条件 例如 Id<100的显示 为null时候 不添加
	 * @param textColor
	 *            文本颜色
	 * @param fontSize
	 *            字体的大小
	 * @param fontColor
	 *            光晕的颜色
	 * @param fontHaloWidth
	 *            光晕的厚度
	 * @param feild
	 *            生成文本的字段
	 * @param ration
	 *            旋转角度
	 * @return Style
	 */
	public static Rule getTextRuleHalo(double x,double y,String cql, String textColor,
			String fontSize, String fontColor, String fontHaloWidth,
			String feild, String ration) {

		// 1. 创建文本注记 ---------------------------------------------
		// 注记出现的位置
		AnchorPoint anchorPoint = sb.createAnchorPoint(x, y);
		// 用于控制注记的旋转角度和位置
		PointPlacement pointPlacement = sb.createPointPlacement(anchorPoint,
				null, ff.literal(ration));

		// 文本注记
		TextSymbolizer textSymbolizer = sb.createTextSymbolizer(
				sf.createFill(ff.literal(textColor)),
				new Font[] { sf.createFont(ff.literal("微软雅黑"),
						ff.literal("Normal"), ff.literal("100"),
						ff.literal(fontSize)) },
				sf.createHalo(sf.createFill(ff.literal(fontColor)),
						ff.literal(fontHaloWidth)),
				sb.attributeExpression(feild), pointPlacement, null);
		// 一个规则可以包含一个 条件及满足条件的样式
		Rule rule = sf.createRule();
		rule.symbolizers().add(textSymbolizer);
		try {
			// 设置条件
			// CQL " ID < 100"
			if (cql != null) {
				rule.setFilter(CQL.toFilter(cql));
			}

		} catch (CQLException e) {
			e.printStackTrace();
		}

		return rule;
	}

	/**
	 * 根据渲染规则 获取样式
	 * 
	 * @param rules
	 *            渲染规则
	 * @return 样式
	 */
	public static Style getStyleByrules(Rule[] rules) {
		FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);
		Style style = sf.createStyle();
		style.featureTypeStyles().add(fts);

		return style;

	}

}
