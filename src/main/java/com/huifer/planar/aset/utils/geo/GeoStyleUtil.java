package com.huifer.planar.aset.utils.geo;

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
 * <p>Title : GeoStyleUtil </p>
 * <p>Description : GeoTools样式构造器</p>
 *
 * @author huifer
 * @date 2019-01-18
 */
public class GeoStyleUtil {

    /**
     * styleBuilder
     */
    private static StyleBuilder styleBuilder = new StyleBuilder();
    /**
     * styleFactory
     */
    private static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    /**
     * filterFactory
     */
    private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    public GeoStyleUtil() {
    }




    /**
     * 面样式
     *
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框宽度
     * @param fillColor 填充颜色
     * @param fillBackgroundColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @return {@link Style}
     */
    public static Style polygonStyle(String condition, String strokeColor,
            String strokeWith, String fillColor, String fillBackgroundColor,
            String fillkeAlpl) {
        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));
        Fill fill = styleFactory
                .createFill(filterFactory.literal(fillColor), filterFactory.literal(fillkeAlpl));
        PolygonSymbolizer polygonSymbolizer = styleFactory
                .createPolygonSymbolizer(stroke, fill, null);

        Rule[] rules = new Rule[1];
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(polygonSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }
        rules[0] = rule;

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * polygon style
     * @param cql 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param fillColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @return {@link Style}
     */
    public static Style getPolygonStyle(String cql, String strokeColor,
            String strokeWith, String fillColor, String fillkeAlpl) {

        Rule[] rules = new Rule[1];

        // 创建蓝色的边框 这里需要注意 filterFactor 的相关方法可以把 文本形式的表达式转换成表达式对象
        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));

        Fill fill = styleFactory
                .createFill(filterFactory.literal(fillColor), filterFactory.literal(fillkeAlpl));

        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        try {
            if (cql != null) {
                rule.setFilter(CQL.toFilter(cql));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }
        rules[0] = rule;

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
    /**
     * 面样式
     *
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param strokeAlpl 填充颜色
     * @param fillColor 填充透明度
     * @return {@link Style}
     */
    public static Style polygonStyle2(String condition, String strokeColor,
            String strokeWith, String strokeAlpl, String fillColor, String fillkeAlpl) {
        Rule[] rules = new Rule[1];

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith), filterFactory.literal(strokeAlpl));

        Fill fill = styleFactory
                .createFill(filterFactory.literal(fillColor), filterFactory.literal(fillkeAlpl));

        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }
        rules[0] = rule;

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }


    /**
     * 点样式
     *
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框宽度
     * @param fillColor 填充色
     * @param fillAlpl 透明度
     * @param size 点大小
     * @return {@link Style}
     */
    public static Style pointStyle(String condition, String strokeColor, String strokeWith,
            String fillColor, String fillAlpl, int size) {
        Graphic graphic = styleFactory.createDefaultGraphic();
        // 先把图形做出来
        Mark circleMark = styleFactory.getCircleMark();
        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));

        circleMark.setStroke(stroke);
        circleMark.setFill(styleFactory
                .createFill(filterFactory.literal(fillColor), filterFactory.literal(fillAlpl)));

        graphic.graphicalSymbols().clear();
        graphic.graphicalSymbols().add(circleMark);
        graphic.setSize(filterFactory.literal(size));

        PointSymbolizer pointSymbolizer = styleFactory.createPointSymbolizer(graphic, null);
        // 规则
        Rule[] rules = new Rule[1];
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(pointSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }
        rules[0] = rule;
        // 收尾
        FeatureTypeStyle featureTypeStyle = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(featureTypeStyle);
        return style;
    }

    /**
     * 线样式
     *
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框宽度
     * @return {@link Style}
     */
    public static Style lineStyle(String condition, String strokeColor, String strokeWith) {

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));

        LineSymbolizer lineSymbolizer = styleFactory.createLineSymbolizer(stroke, null);
        Rule[] rules = new Rule[1];
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(lineSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));

            }
        } catch (CQLException e) {
            e.printStackTrace();
        }
        rules[0] = rule;
        FeatureTypeStyle featureTypeStyle = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(featureTypeStyle);
        return style;
    }

    /**
     * @param x x
     * @param y y
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param fillColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @param size 点大小
     * @param textColor 文本颜色
     * @param fontSize 字体的大小
     * @param fontColor 光晕的颜色
     * @param feild 生成文本的字段
     * @return {@link Style}
     */
    public static Style pointTextStyle(double x, double y, String condition,
            String strokeColor, String strokeWith, String fillColor,
            String fillkeAlpl, String size, String textColor, String fontSize,
            String fontColor, String feild) {
        Rule[] rules = new Rule[1];
        // 位置
        AnchorPoint anchorPoint = styleBuilder.createAnchorPoint(x, y);
        PointPlacement pointPlacement = styleBuilder.createPointPlacement(anchorPoint,
                null, filterFactory.literal(0));
        // text
        TextSymbolizer textSymbolizer = styleBuilder.createTextSymbolizer(
                styleFactory.createFill(filterFactory.literal(textColor)),
                new Font[]{styleFactory.createFont(filterFactory.literal("微软雅黑"),
                        filterFactory.literal("italic"), filterFactory.literal("100"),
                        filterFactory.literal(fontSize))}, null,
                styleBuilder.attributeExpression(feild), pointPlacement, null);
        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));
        mark.setStroke(stroke);
        mark.setFill(styleFactory.createFill(filterFactory.literal(fillColor),
                filterFactory.literal(fillkeAlpl)));
        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);

        // 设置大小
        gr.setSize(filterFactory.literal(size));

        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        rule.symbolizers().add(textSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }
        rules[0] = rule;

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;

    }

    /**
     * 文字样式
     *
     * @param x x
     * @param y y
     * @param condition 过滤条件
     * @param textColor 文本颜色
     * @param fontSize 字体的大小
     * @param fontColor 光晕的颜色
     * @param feild 生成文本的字段
     * @param ration 旋转角度
     * @return {@link Style}
     */
    public static Style textStyle(double x, double y, String condition, String textColor,
            String fontSize, String fontColor, String feild, String ration) {

        Rule[] rules = new Rule[1];

        AnchorPoint anchorPoint = styleBuilder.createAnchorPoint(x, y);
        PointPlacement pointPlacement = styleBuilder.createPointPlacement(anchorPoint,
                null, filterFactory.literal(ration));
        TextSymbolizer textSymbolizer = styleBuilder.createTextSymbolizer(
                styleFactory.createFill(filterFactory.literal(textColor)),
                new Font[]{styleFactory.createFont(filterFactory.literal("微软雅黑"),
                        filterFactory.literal("italic"), filterFactory.literal("100"),
                        filterFactory.literal(fontSize))}, null,
                styleBuilder.attributeExpression(feild), pointPlacement, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(textSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }

        } catch (CQLException e) {
            e.printStackTrace();
        }
        rules[0] = rule;

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * 点渲染规则
     *
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param fillColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @param size 点大小
     * @return {@link Rule}
     */
    public static Rule pointRule(String condition, String strokeColor, String strokeWith,
            String fillColor, String fillkeAlpl, int size) {

        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));
        mark.setStroke(stroke);
        mark.setFill(styleFactory.createFill(filterFactory.literal(fillColor),
                filterFactory.literal(fillkeAlpl)));
        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);

        gr.setSize(filterFactory.literal(size));

        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }

    /**
     * 线样式渲染规则
     *
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @return {@link Rule}
     */
    public static Rule lineRule(String condition, String strokeColor, String strokeWith) {

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));

        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }


    /**
     * 面渲染规则
     *
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param fillColor 填充颜色
     * @param fillBackgroundColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @return {@link Rule}
     */
    public static Rule polygonRule(String condition, String strokeColor, String strokeWith,
            String fillColor, String fillBackgroundColor, String fillkeAlpl) {

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));
        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(stroke);
        mark.setFill(styleFactory.createFill(filterFactory.literal(fillColor),
                filterFactory.literal(fillkeAlpl)));
        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);

        gr.setSize(filterFactory.literal(20));

        Fill fill = styleFactory.createFill(filterFactory.literal(fillColor),
                filterFactory.literal(fillBackgroundColor), filterFactory.literal(fillkeAlpl), gr);

        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }

    /****
     * 面渲染规则
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param fillColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @return {@link Rule}
     */
    public static Rule polygonRule(String condition, String strokeColor,
            String strokeWith, String fillColor, String fillkeAlpl) {

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));

        Fill fill = styleFactory
                .createFill(filterFactory.literal(fillColor), filterFactory.literal(fillkeAlpl));

        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }
        return rule;
    }


    /**
     * 面渲染规则
     *
     * @param condition 过滤条件
     * @param strokeAlpl 边框透明度
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param fillColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @return {@link Rule}
     */
    public static Rule polygonRule2(String condition, String strokeAlpl, String strokeColor,
            String strokeWith, String fillColor, String fillkeAlpl) {
        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith), filterFactory.literal(strokeAlpl));

        Fill fill = styleFactory
                .createFill(filterFactory.literal(fillColor), filterFactory.literal(fillkeAlpl));

        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }

    /**
     * 点文字样式
     *
     * @param x x
     * @param y y
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param fillColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @param size 点大小
     * @param textColor 文本颜色
     * @param fontSize 字体的大小
     * @param fontColor 光晕的颜色
     * @param feild 生成文本的字段
     * @return {@link Rule}
     */
    public static Rule pointTextRule(double x, double y, String condition,
            String strokeColor, String strokeWith, String fillColor,
            String fillkeAlpl, String size, String textColor, String fontSize,
            String fontColor, String feild) {

        AnchorPoint anchorPoint = styleBuilder.createAnchorPoint(x, y);
        PointPlacement pointPlacement = styleBuilder.createPointPlacement(anchorPoint,
                null, filterFactory.literal(0));

        TextSymbolizer textSymbolizer = styleBuilder.createTextSymbolizer(
                styleFactory.createFill(filterFactory.literal(textColor)),
                new Font[]{styleFactory.createFont(filterFactory.literal("微软雅黑"),
                        filterFactory.literal("italic"), filterFactory.literal("100"),
                        filterFactory.literal(fontSize))}, null,
                styleBuilder.attributeExpression(feild), pointPlacement, null);

        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));
        mark.setStroke(stroke);
        mark.setFill(styleFactory.createFill(filterFactory.literal(fillColor),
                filterFactory.literal(fillkeAlpl)));
        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);

        gr.setSize(filterFactory.literal(size));

        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        rule.symbolizers().add(textSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }

    /**
     * 点文字样式
     *
     * @param x x
     * @param y y
     * @param condition 过滤条件
     * @param strokeColor 边框颜色
     * @param strokeWith 边框厚度
     * @param fillColor 填充颜色
     * @param fillkeAlpl 填充透明度
     * @param size 点大小
     * @param textColor 文本颜色
     * @param fontSize 字体的大小
     * @param fontColor 光晕的颜色
     * @param feild 生成文本的字段
     * @param fontStyle 字体
     * @return {@link Rule}
     */
    public static Rule pointTextRule(double x, double y, String condition,
            String strokeColor, String strokeWith, String fillColor,
            String fillkeAlpl, String size, String textColor, String fontSize,
            String fontColor, String feild, String fontStyle) {

        AnchorPoint anchorPoint = styleBuilder.createAnchorPoint(x, y);
        PointPlacement pointPlacement = styleBuilder.createPointPlacement(anchorPoint,
                null, filterFactory.literal(0));

        TextSymbolizer textSymbolizer = styleBuilder.createTextSymbolizer(
                styleFactory.createFill(filterFactory.literal(textColor)),
                new Font[]{styleFactory.createFont(filterFactory.literal("微软雅黑"),
                        filterFactory.literal(fontStyle), filterFactory.literal("100"),
                        filterFactory.literal(fontSize))}, null,
                styleBuilder.attributeExpression(feild), pointPlacement, null);

        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();

        Stroke stroke = styleFactory.createStroke(filterFactory.literal(strokeColor),
                filterFactory.literal(strokeWith));
        mark.setStroke(stroke);
        mark.setFill(styleFactory.createFill(filterFactory.literal(fillColor),
                filterFactory.literal(fillkeAlpl)));
        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);

        gr.setSize(filterFactory.literal(size));

        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        rule.symbolizers().add(textSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }


    /**
     * 文本 样式
     *
     * @param x x
     * @param y y
     * @param condition 过滤条件
     * @param textColor 文本颜色
     * @param fontSize 字体的大小
     * @param feild 生成文本的字段
     * @param ration 旋转角度
     * @return {@link Rule}
     */
    public static Rule textRule(double x, double y, String condition,
            String textColor, String fontSize, String feild, String ration) {

        AnchorPoint anchorPoint = styleBuilder.createAnchorPoint(x, y);
        PointPlacement pointPlacement = styleBuilder.createPointPlacement(anchorPoint,
                null, filterFactory.literal(ration));
        TextSymbolizer textSymbolizer = styleBuilder.createTextSymbolizer(
                styleFactory.createFill(filterFactory.literal(textColor)),
                new Font[]{styleFactory.createFont(filterFactory.literal("微软雅黑"),
                        filterFactory.literal("italic"), filterFactory.literal("100"),
                        filterFactory.literal(fontSize))}, null,
                styleBuilder.attributeExpression(feild), pointPlacement, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(textSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }
        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }


    /**
     * 文本样式
     *
     * @param x x
     * @param y y
     * @param condition 过滤条件
     * @param textColor 文本颜色
     * @param fontSize 字体的大小
     * @param feild 文本字段
     * @param ration 旋转角度
     * @param fontStyle 文字样式
     * @return {@link Rule}
     */
    public static Rule getTextRule(double x, double y, String condition,
            String textColor, String fontSize, String feild, String ration, String fontStyle) {

        AnchorPoint anchorPoint = styleBuilder.createAnchorPoint(x, y);
        PointPlacement pointPlacement = styleBuilder.createPointPlacement(anchorPoint,
                null, filterFactory.literal(ration));
        TextSymbolizer textSymbolizer = styleBuilder.createTextSymbolizer(
                styleFactory.createFill(filterFactory.literal(textColor)),
                new Font[]{styleFactory.createFont(filterFactory.literal("微软雅黑"),
                        filterFactory.literal(fontStyle), filterFactory.literal("100"),
                        filterFactory.literal(fontSize))}, null,
                styleBuilder.attributeExpression(feild), pointPlacement, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(textSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }

        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }


    /**
     * 文本样式
     *
     * @param x x
     * @param y y
     * @param condition 过滤条件
     * @param textColor 文本颜色
     * @param fontSize 字体的大小
     * @param fontColor 光晕的颜色
     * @param fontHaloWidth 光晕的厚度
     * @param feild 生成文本的字段
     * @param ration 旋转角度
     * @return {@link Rule}
     */
    public static Rule getTextRuleHalo(double x, double y, String condition, String textColor,
            String fontSize, String fontColor, String fontHaloWidth,
            String feild, String ration) {

        AnchorPoint anchorPoint = styleBuilder.createAnchorPoint(x, y);
        PointPlacement pointPlacement = styleBuilder.createPointPlacement(anchorPoint,
                null, filterFactory.literal(ration));

        TextSymbolizer textSymbolizer = styleBuilder.createTextSymbolizer(
                styleFactory.createFill(filterFactory.literal(textColor)),
                new Font[]{styleFactory.createFont(filterFactory.literal("微软雅黑"),
                        filterFactory.literal("Normal"), filterFactory.literal("100"),
                        filterFactory.literal(fontSize))},
                styleFactory.createHalo(styleFactory.createFill(filterFactory.literal(fontColor)),
                        filterFactory.literal(fontHaloWidth)),
                styleBuilder.attributeExpression(feild), pointPlacement, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(textSymbolizer);
        try {
            if (condition != null) {
                rule.setFilter(CQL.toFilter(condition));
            }

        } catch (CQLException e) {
            e.printStackTrace();
        }

        return rule;
    }

    /**
     * 根据渲染规则 获取样式
     *
     * @param rules 渲染规则
     * @return {@link Style}
     */
    public static Style getStyleByrules(Rule[] rules) {
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;

    }

}
