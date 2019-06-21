package com.huifer.planar.aset.utils.geo;

import java.io.IOException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.DefaultGridFeatureBuilder;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.Grids;
import org.geotools.grid.oblong.Oblongs;

/**
 * <p>Title : GridUtil </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-01-18
 */
public class GridUtil {

    /**
     * 构造网格图
     *
     * @param source 数据
     * @param size 生成数量
     * @return {@link SimpleFeatureCollection}
     */
    public static SimpleFeatureCollection createGrids(SimpleFeatureSource source, int size) {
        ReferencedEnvelope gridBounds;
        SimpleFeatureCollection gridFeature = null;
        try {
            gridBounds = source.getBounds();
            double gridWidth = (gridBounds.getMaxX() - gridBounds.getMinX()) / size;
            SimpleFeatureSource grid = Grids.createSquareGrid(gridBounds, gridWidth);
            gridFeature = grid.getFeatures();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return gridFeature;
    }


    /**
     * 指定边界生成网格
     *
     * @param gridBounds 边框
     * @param size 生成数量
     * @return {@link SimpleFeatureCollection}
     */
    public static SimpleFeatureCollection createGrids(ReferencedEnvelope gridBounds, int size) {
        SimpleFeatureCollection gridFeature = null;
        try {
            double gridWidth = (gridBounds.getMaxX() - gridBounds.getMinX()) / size;
            SimpleFeatureSource grid = Grids.createSquareGrid(gridBounds, gridWidth);
            gridFeature = grid.getFeatures();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return gridFeature;
    }


    /**
     * 指定边界指定行列数量 生成网格
     *
     * @param gridBounds 边界
     * @param hSize 高  几个网格
     * @param wSize 宽 几个网格
     * @return {@link SimpleFeatureCollection}
     */
    public static SimpleFeatureCollection createGrids(ReferencedEnvelope gridBounds, int hSize,
            int wSize) {
        SimpleFeatureCollection gridFeature = null;
        try {
            double gridWidth = (gridBounds.getMaxX() - gridBounds.getMinX()) / wSize;
            double gridHeight = (gridBounds.getMaxY() - gridBounds.getMinY()) / hSize;
            GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
            SimpleFeatureSource grid = Oblongs.createGrid(gridBounds, gridWidth, gridHeight,
                    builder);
            gridFeature = grid.getFeatures();
        } catch (IOException e) {
            e.printStackTrace();
            return gridFeature;
        }

        return gridFeature;
    }

    /**
     * 指定边界指定行列数量 生成网格
     *
     * @param hSize 高  几个网格
     * @param wSize 宽 几个网格
     * @param gridBounds 边界
     * @return {@link SimpleFeatureCollection}
     */
    public static SimpleFeatureCollection createGrids(int hSize, int wSize,
            ReferencedEnvelope gridBounds) {
        SimpleFeatureCollection gridFeature = null;
        try {
            double gridWidth = (gridBounds.getMaxX() - gridBounds.getMinX()) / wSize;
            double gridHeight = (gridBounds.getMaxY() - gridBounds.getMinY()) / hSize;
            GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
            SimpleFeatureSource grid = Oblongs.createGrid(gridBounds, gridWidth, gridHeight,
                    builder);
            gridFeature = grid.getFeatures();
        } catch (IOException e) {
            e.printStackTrace();
            return gridFeature;
        }

        return gridFeature;
    }


    /**
     * 根据边界生成网格
     *
     * @param hSize 高  几个网格
     * @param wSize 宽 几个网格
     * @param gridBounds 边界
     * @return {@link SimpleFeatureSource}
     */
    public static SimpleFeatureSource getSimpleFeatureByCreateGrids(int hSize, int wSize,
            ReferencedEnvelope gridBounds) {
        SimpleFeatureSource grid = null;
        try {
            double gridWidth = (gridBounds.getMaxX() - gridBounds.getMinX()) / wSize;
            double gridHeight = (gridBounds.getMaxY() - gridBounds.getMinY()) / hSize;
            GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
            grid = Oblongs.createGrid(gridBounds, gridWidth, gridHeight,
                    builder);
        } catch (Exception e) {
            e.printStackTrace();
            return grid;
        }
        return grid;
    }


}
