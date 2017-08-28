package com.lzugis.map.utils;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.DefaultGridFeatureBuilder;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.Grids;
import org.geotools.grid.oblong.Oblongs;
public class GridUtil {

	public GridUtil() {
	}
	
	/**
	 * 根据数据源 生成网格
	 * @param simpleFeatureSource 数据源
	 * @param len 生成多少个 
	 * @return 生成的要素集合
	 */
	public static SimpleFeatureCollection createGrids(SimpleFeatureSource simpleFeatureSource,int len){
		 ReferencedEnvelope gridBounds;
		 SimpleFeatureCollection pGridFeatureColl = null;
		try {
			gridBounds = simpleFeatureSource.getBounds();
			 double gridWidth = (gridBounds.getMaxX()-gridBounds.getMinX())/len;
			 SimpleFeatureSource grid = Grids.createSquareGrid(gridBounds, gridWidth);
			 pGridFeatureColl =grid.getFeatures();
		} catch (IOException e) {
			e.printStackTrace();
			return pGridFeatureColl;
		}
	      
		return pGridFeatureColl;
	}
	
	/**
	 * 根据边界 生成网格
	 * @param ReferencedEnvelope 边界
	 * @param len 生成多少个 
	 * @return 生成的要素集合
	 */
	public static SimpleFeatureCollection createGrids(ReferencedEnvelope gridBounds,int len){
		 SimpleFeatureCollection pGridFeatureColl = null;
		try {
			 double gridWidth = (gridBounds.getMaxX()-gridBounds.getMinX())/len;
			 SimpleFeatureSource grid = Grids.createSquareGrid(gridBounds, gridWidth);
			 pGridFeatureColl =grid.getFeatures();
		} catch (IOException e) {
			e.printStackTrace();
			return pGridFeatureColl;
		}
	      
		return pGridFeatureColl;
	}

	
	/**
	 * 根据边界 生成网格
	 * @param ReferencedEnvelope 边界
	 * @param hlen 高  几个网格
	 * @param wlen 宽 几个网格
	 * @return 生成的要素集合
	 */
	public static SimpleFeatureCollection createGrids(ReferencedEnvelope gridBounds,int hlen,int wlen){
		 SimpleFeatureCollection pGridFeatureColl = null;
		try {
			 double gridWidth = (gridBounds.getMaxX()-gridBounds.getMinX())/wlen;
			 double gridHeight = (gridBounds.getMaxY()-gridBounds.getMinY())/hlen;
			 GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
		     SimpleFeatureSource grid = Oblongs.createGrid(gridBounds, gridWidth, gridHeight, builder);//生成grid,width.height的单位为度。
			 pGridFeatureColl =grid.getFeatures();
		} catch (IOException e) {
			e.printStackTrace();
			return pGridFeatureColl;
		}
	      
		return pGridFeatureColl;
	}
	
	
	/**
	 * 根据边界 生成网格
	 * @param hlen 高  每个多少度 
	 * @param wlen 宽 每个多少度
	 * @param ReferencedEnvelope 边界
	 * @return 生成的要素集合
	 */
	public static SimpleFeatureCollection createGrids(int hlen,int wlen,ReferencedEnvelope gridBounds){
		 SimpleFeatureCollection pGridFeatureColl = null;
		try {
			 double gridWidth = (gridBounds.getMaxX()-gridBounds.getMinX())/wlen;
			 double gridHeight = (gridBounds.getMaxY()-gridBounds.getMinY())/hlen;
			 GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
		     SimpleFeatureSource grid = Oblongs.createGrid(gridBounds, gridWidth, gridHeight, builder);//生成grid,width.height的单位为度。
			 pGridFeatureColl =grid.getFeatures();
		} catch (IOException e) {
			e.printStackTrace();
			return pGridFeatureColl;
		}
	      
		return pGridFeatureColl;
	}
	
	
	
	/**
	 * 根据边界 生成网格
	 * @param hlen 高  每个多少度 
	 * @param wlen 宽 每个多少度
	 * @param ReferencedEnvelope 边界
	 * @return 生成的要素集合
	 */
	public static SimpleFeatureSource getSimpleFeatureSourceByCreateGrids(int hlen,int wlen,ReferencedEnvelope gridBounds){
		 SimpleFeatureSource grid = null;
		try {
			 double gridWidth = (gridBounds.getMaxX()-gridBounds.getMinX())/wlen;
			 double gridHeight = (gridBounds.getMaxY()-gridBounds.getMinY())/hlen;
			 GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
		      grid = Oblongs.createGrid(gridBounds, gridWidth, gridHeight, builder);//生成grid,width.height的单位为度。
		} catch (Exception e) {
			e.printStackTrace();
			return grid;
		}
	      
		return grid;
	}
	
	

	/**
	 * 根据边界 生成网格
	 * @param ReferencedEnvelope 边界
	 * @param hlen 高  每个多少度 
	 * @param wlen 宽 每个多少度
	 * @return 生成的要素集合
	 */
	public static SimpleFeatureSource getSimpleFeatureSourceByCreateGrids(ReferencedEnvelope gridBounds,int hlen,int wlen){
		 SimpleFeatureSource grid = null;
		try {
			 GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
		      grid = Oblongs.createGrid(gridBounds, wlen, hlen, builder);//生成grid,width.height的单位为度。
		} catch (Exception e) {
			e.printStackTrace();
			return grid;
		}
	      
		return grid;
	}
	
	 
}
