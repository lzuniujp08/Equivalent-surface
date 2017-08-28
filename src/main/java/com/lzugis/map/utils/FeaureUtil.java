package com.lzugis.map.utils;

import java.util.List;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class FeaureUtil {

	public FeaureUtil() {
	}
	public static String feildsStatic = null;
	
	/**
	 * 根据字段 创建表头   
	 * @param layerName 名称
	 * @param feilds 以逗号分隔的每个里面  名称和类型以冒号分隔<br> 有三个很重要  1:crs 4326和3857两种 2.几何类型 Point,LineString,Polygon三种   3:属性 属性可无 类型有 String,int,double,float,boolean    <br>格式如下  "crs:4326,the_geom:Point,name:String"
	 * @return
	 */
	public static SimpleFeatureType  creatSimpleFeatureType(String layerName,String feilds){
		SimpleFeatureType type = null;
		
		SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        String tmp = null;
		b.setName( layerName );
		try {
		String[] feildsArray = null;
		if(feilds!=null&&feilds.contains(",")){
			feildsArray = feilds.split(",");
			for(String s :feildsArray){
				String[] keyValues = null;
				if(s!=null&&s.contains(":")){
					keyValues = s.split(":");
					String key = keyValues[0];
					String value = keyValues[1];
					if(key.equals("crs")){
						if(value.equals("4326")){
							b.setCRS( CRS.decode("EPSG:4326", true));
						}
						else if(value.equals("3857")){
							b.setCRS( CRS.decode("EPSG:3857", true));
						}
					}
					else{
						
						if(value.equals("String")){  
							b.add( key, String.class );
						}
						if(value.equals("int")){
							b.add( key, Integer.class );
						}
						if(value.equals("double")){
							b.add( key, Double.class );
						}
						if(value.equals("float")){
							b.add( key, Float.class );
						}
						if(value.equals("boolean")){
							b.add( key,Boolean.class );
						}
						if(value.equals("Point")){
							b.add( key, Point.class );
						}
						if(value.equals("LineString")){
							b.add(key, LineString.class );
						}
						if(value.equals("Polygon")){
							b.add( key, Polygon.class );
						}
						if(value.equals("MultiPolygon")){
							b.add( key, MultiPolygon.class );
						}
						
						if(tmp!=null){
							tmp =tmp +","+key;
						}
						else{
							tmp = 	key;
						}
					}
				}
			}
			type = b.buildFeatureType();
			feildsStatic = tmp;
		}

		}  catch (Exception e) {
			e.printStackTrace();
			 return type ;
		}
        if(!feilds.contains("crs")){
        	b.setCRS( DefaultGeographicCRS.WGS84 );
        }
		 return type ;
		 
	}
	/**
	 * 创建要素集合
	 * @param featureType 要素类型
	 * @param values 值   List<Map<String,Object>> values 
	 * @return
	 */
	public static SimpleFeatureCollection  creatSimpleFeature(SimpleFeatureType featureType,List<Map<String,Object>> values){
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
		 ListFeatureCollection collection = null;
		SimpleFeature feature = null;
		String[] feildsArray = null;
		try {
		if(feildsStatic!=null&&feildsStatic.contains(",")){
			feildsArray = feildsStatic.split(",");
			if(values!=null&&values.size()>0){
				collection = new ListFeatureCollection(featureType); 
				for(int i=0;i<values.size();i++){
					Map<String,Object> value = values.get(i);
					for(String key :feildsArray){
						if(value.containsKey(key)){
							builder.add( value.get(key) );
						}
						
					}
					feature = builder.buildFeature( "fid."+i );
					collection.add(feature);
				}
			}
			}
		}  catch (Exception e) {
			e.printStackTrace();
			 return collection ;
		}
		return collection;
	}
	
	
	
	/**
	 * 创建要素集合  根据字段和值
	 * @param layerName 名称
	 * @param feilds 以逗号分隔的每个里面  名称和类型以冒号分隔<br> 有三个很重要  1:crs 4326和3857两种 2.几何类型 Point,LineString,Polygon三种   3:属性 属性可无 类型有 String,int,double,float,boolean    <br>格式如下  "crs:4326,the_geom:Point,name:String"
	 * @param values 值   List<Map<String,Object>> values 
	 * @return
	 */
	public static SimpleFeatureCollection  creatSimpleFeatureByFeilds(String layerName,String feilds,List<Map<String,Object>> values){
		SimpleFeatureType  featureType = creatSimpleFeatureType(layerName,feilds);
		if(featureType==null){
			return null;
		}
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
		 ListFeatureCollection collection = null;
		SimpleFeature feature = null;
		String[] feildsArray = null;
		try {
		if(feildsStatic!=null&&feildsStatic.contains(",")){
			feildsArray = feildsStatic.split(",");
			if(values!=null&&values.size()>0){
				collection = new ListFeatureCollection(featureType); 
				for(int i=0;i<values.size();i++){
					Map<String,Object> value = values.get(i);
					for(String key :feildsArray){
						if(value.containsKey(key)){
							builder.add( value.get(key) );
						}
						
					}
					feature = builder.buildFeature( "fid."+i );
					collection.add(feature);
				}
			}
			}
		}  catch (Exception e) {
			e.printStackTrace();
			 return collection ;
		}
		return collection;
	}
	
	/**
	 * 根据要素集合 创建数据源
	 * @param collection 要素集合
	 * @return 数据源
	 */
	public static FeatureSource  creatFeatureSourceByCollection(SimpleFeatureCollection collection){
		FeatureSource source = null;
		try {
			source = new CollectionFeatureSource(collection);
		} catch (Exception e) {
			e.printStackTrace();
			return source;
		}
		return source;
	}
	 
	

}
