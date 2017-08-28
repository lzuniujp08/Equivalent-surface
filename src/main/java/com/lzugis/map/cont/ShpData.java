package com.lzugis.map.cont;

import java.io.File;
import java.io.IOException;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;

import com.lzugis.map.utils.GeoJSONUtil;

public class ShpData {

	public ShpData() {
	}

	@SuppressWarnings("rawtypes")
	public static FeatureSource shpfeatureSourcel = null;
	public static FeatureSource geojsonfeatureSourcel = null;
	public static FileDataStore shpstorel = null;
	public static FileDataStore geojsonstorel = null;
	
	private static String shpName ="citycode.shp";
	private static String geojsonName ="yx.geojson";
	private static String shpCS ="GBk";
	private static String geojsonCS ="utf-8";
	private static String projectName ="ssdyxdraw";
	static {
		String rootUrl = System.getProperty("user.dir");
		
		try {
//			File shpfilel = new File(rootUrl+"/shp/"+shpName);
//			if(!shpfilel.exists()){
//				System.err.println("获取 shpfilel 出错! ShpData类");
//			}
//			shpstorel = ShpUtil.getDataStore(shpfilel, shpCS);
			
			File geojsonfilel = new File(rootUrl+"/shp/"+geojsonName);
			if(!geojsonfilel.exists()){
				System.err.println("获取 geojsonfilel 出错! ShpData类");
			}
			geojsonfeatureSourcel = GeoJSONUtil.readGeoJsonByGeojsonToFeatureSource(rootUrl+"/shp/"+geojsonName);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("获取 FeatureSource 出错! ShpData类");
		}
	}

	/**
	 * 根据 cql获取要素集
	 * @param cql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static FeatureCollection getFeatureSourceByGeoJson(String cql) {
		FeatureCollection fc = null;
		
		
		if (geojsonfeatureSourcel != null) {
			try {
				if (cql != null) {
					Filter filter = CQL.toFilter(cql);
					fc = geojsonfeatureSourcel.getFeatures(filter);
				} else {
					fc = geojsonfeatureSourcel.getFeatures();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CQLException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("获取 getFeatures 出错! ShpData类");
		}
		return fc;
	}

	/**
	 * 根据 cql获取要素集
	 * @param cql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static FeatureCollection getFeatureSourceByShp(String cql) {
		FeatureCollection fc = null;
		
		
		if (shpstorel != null) {
			try {
				if (cql != null) {
					Filter filter = CQL.toFilter(cql);
					fc = shpstorel.getFeatureSource().getFeatures(filter);
				} else {
					fc = shpstorel.getFeatureSource().getFeatures();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CQLException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("获取 getFeatures 出错! ShpData类");
		}
		return fc;
	}
	
	
	/**
	 * 根据 cql获取要素集
	 * @param shpName
	 * @param cql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static FeatureCollection getFeatureSourceByNameByGeoJson(String shpName,String cql) {
		FeatureCollection fc = null;
		if (geojsonfeatureSourcel != null) {
			try {
				if (cql != null) {
					Filter filter = CQL.toFilter(cql);
					fc = geojsonfeatureSourcel.getFeatures(filter);
				} else {
					fc = geojsonfeatureSourcel.getFeatures();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CQLException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("获取 getFeatures 出错! ShpData类");
		}
		return fc;
	}
	
	/**
	 * 根据 cql获取要素集
	 * @param shpName
	 * @param cql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static FeatureCollection getFeatureSourceByNameByShp(String shpName,String cql) {
		FeatureCollection fc = null;
		if (shpstorel != null) {
			try {
				if (cql != null) {
					Filter filter = CQL.toFilter(cql);
					fc = shpstorel.getFeatureSource().getFeatures(filter);
				} else {
					fc = shpstorel.getFeatureSource().getFeatures();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CQLException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("获取 getFeatures 出错! ShpData类");
		}
		return fc;
	}
	
	
	
}
