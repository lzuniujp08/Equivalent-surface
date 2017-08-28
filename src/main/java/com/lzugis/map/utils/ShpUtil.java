package com.lzugis.map.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Shape文件操作类
 * 
 * @author lhy
 *
 */
public class ShpUtil {

	private static ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
	public static  String CRSStr = "EPSG:4326";
	/**
	 * 读取shp文件
	 * 
	 * @param file
	 *            shp文件路径
	 * @return ShapefileDataStore 数据源
	 */
	public static ShapefileDataStore getDataStore(File file) {

		ShapefileDataStore dataStore = null;
		try {
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("url", file.toURI().toURL());
			params.put("create spatial index", Boolean.TRUE);
			dataStore = (ShapefileDataStore) dataStoreFactory
					.createNewDataStore(params);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataStore;
	}
	
	
	/**
	 * 读取shp文件  FileDataStoreFinder 路径过长
	 * 
	 * @param file
	 *            shp文件路径
	 * @return FeatureSource 数据源
	 */
	public static FeatureSource getDataStoreByFinder(String path) {

		FeatureSource featureSource = null;
		try {
			File file = new File(path);
			FileDataStore store = FileDataStoreFinder.getDataStore(file);
			featureSource =  store.getFeatureSource();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return featureSource;
	}
	
	
	/**
	 * 读取shp文件
	 * 
	 * @param file
	 *            shp文件路径
	 * @return ShapefileDataStore 数据源
	 */
	public static ShapefileDataStore getDataStore(File file,String cs) {

		ShapefileDataStore dataStore = null;
		try {
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("url", file.toURI().toURL());
			params.put("create spatial index", Boolean.TRUE);
			params.put("charset",cs);
			dataStore = (ShapefileDataStore) dataStoreFactory
					.createNewDataStore(params);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataStore;
	}

	/**
	 * 读取shp文件
	 * 
	 * @param fileName
	 *            shp文件路径
	 * @return SimpleFeatureCollection 要素集合
	 */
	public static SimpleFeatureCollection getCollection(String fileName) {
		File file = null;
		file = new File(fileName);
		try {
			SimpleFeatureCollection sfc = getDataStore(file).getFeatureSource()
					.getFeatures();
			return sfc;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 读取shp文件
	 * 
	 * @param fileName
	 *            shp文件路径
	 * @return ContentFeatureSource
	 */
	public static ContentFeatureSource getFeatureSource(String fileName) {
		File file = null;
		file = new File(fileName);
		try {
			ContentFeatureSource sfc = getDataStore(file).getFeatureSource();
			return sfc;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 读取shp,转成geoJson,写到本地
	 * 
	 * @param shpPath
	 *            shp路径
	 * @param geoJsonPath
	 *            写geoJson的路径
	 */
	public static void shpToWriterGeoJson(String shpPath, String geoJsonPath) {
		String json = "";
		try {
			
			// 获取shp文件所有要素集合
			FeatureCollection<SimpleFeatureType, SimpleFeature> collection = getCollection(shpPath);
			FeatureIterator<SimpleFeature> features = collection.features();
			// 遍历集合
			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				FeatureJSON fjson = new FeatureJSON();
				StringWriter writer = new StringWriter();
				fjson.writeFeature(feature, writer);
				json = writer.toString() + ", \n" + json;

			}
			features.close();
			json = json.substring(0, json.lastIndexOf(","));
			String head = "{\"type\": \"FeatureCollection\"," + "\n"
					+ "\"features\": [";
			String end = " \n ] }";
			json = head + json + end;
			toFileByCS(new File(geoJsonPath), json, "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取shp,转成geoJson
	 * 
	 * @param shpPath
	 *            shp路径
	 * @return String geoJson
	 */
	public static String shpStringToGeoJson(String shpPath) {
		String json = "";
		try {

			// 获取shp文件所有要素集合
			FeatureCollection<SimpleFeatureType, SimpleFeature> collection = getCollection(shpPath);
			FeatureIterator<SimpleFeature> features = collection.features();
			// 遍历集合
			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				FeatureJSON fjson = new FeatureJSON();
				StringWriter writer = new StringWriter();
				fjson.writeFeature(feature, writer);
				json = writer.toString() + ", \n" + json;

			}
			features.close();
			json = json.substring(0, json.lastIndexOf(","));
			String head = "{\"type\": \"FeatureCollection\"," + "\n"
					+ "\"features\": [";
			String end = " \n ] }";
			json = head + json + end;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * geoJson字符串文件转shp文件
	 * 
	 * @param shpPath
	 *            shp文件保存路径
	 * @param shpName
	 *            shp文件名称
	 * @param geojsonFilePah
	 *            geoJson字符串文件
	 */
	public static void gsoJsonFileToShp(String shpPath, String shpName,
			String geojsonFilePah) {
		FileInputStream  fis = null;
		String geojson = null;
		String shapefilePath = shpPath + "//" + shpName + ".shp";
		try {
			fis = new FileInputStream(geojsonFilePah);
			// geojson读取工具
			FeatureJSON fjson = new FeatureJSON();
			// 获取要素集合
			SimpleFeatureCollection fs = (SimpleFeatureCollection) fjson
					.readFeatureCollection(fis);
			// 要素集合写成shp文件
			saveFeatureCollectionToShapefile(fs, shapefilePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}

	}

	/**
	 * geoJson字符串转shp文件
	 * 
	 * @param shpPath
	 *            shp文件保存路径
	 * @param shpName
	 *            shp文件名称
	 * @param geojson
	 *            geoJson字符串
	 */
	public static void gsoJsonToShp(String shpPath, String shpName,
			String geojson) {
		ByteArrayInputStream bis = null;
		String shapefilePath = shpPath + "//" + shpName + ".shp";
		try {
			// 字符串流
			bis = new ByteArrayInputStream(geojson.getBytes());
			// geojson读取工具

			FeatureJSON fjson = new FeatureJSON();
			// 获取要素集合
			SimpleFeatureCollection fs = (SimpleFeatureCollection) fjson
					.readFeatureCollection(bis);
			// 要素集合写成shp文件
			saveFeatureCollectionToShapefile(fs, shapefilePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 要素集合写成shp文件
	 * 
	 * @param ftColl
	 * @param shapefilePath
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	private static void saveFeatureCollectionToShapefile(
			SimpleFeatureCollection ftColl, String shapefilePath)
			throws IOException, URISyntaxException, Exception {
		// CRS
		CoordinateReferenceSystem sourceCrs = CRS.decode(CRSStr, true);
		
		// 设置参数
		File shapefile = new File(shapefilePath);
		URL url = shapefile.toURI().toURL();
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("url", url);
		params.put("create spatial index", Boolean.TRUE);
		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory
				.createNewDataStore(params);
        
		// 重构表头
		SimpleFeatureType TYPE = rewriteSchema(
				(SimpleFeatureType) ftColl.getSchema(), sourceCrs);
		newDataStore.createSchema(TYPE);
		newDataStore.forceSchemaCRS(sourceCrs);
		newDataStore.setCharset(Charset.forName("GB18030"));
		//newDataStore.setCharset(Charset.forName("GB2312"));
		
		// 重构集合
		ftColl = rewriteSimpleFeatureCollection(ftColl, TYPE);
		// 开始写
		Transaction transaction = new DefaultTransaction("create");
		String typeName = newDataStore.getTypeNames()[0];
		SimpleFeatureSource featureSource = newDataStore
				.getFeatureSource(typeName);
		if (featureSource instanceof FeatureStore) {
			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
			featureStore.setTransaction(transaction);
			try {
				featureStore.addFeatures(ftColl);
				transaction.commit();
			} catch (Exception problem) {
				problem.printStackTrace();
				transaction.rollback();
			} finally {
				transaction.close();
			}
		}

	}

	/**
	 * 重构 表头 属性 字段
	 * 
	 * @param schema
	 *            旧的 SimpleFeatureType
	 * @param crs
	 *            坐标系
	 * @return 新的 SimpleFeatureType
	 */
	private static SimpleFeatureType rewriteSchema(SimpleFeatureType schema,
			CoordinateReferenceSystem crs) {
		GeometryDescriptor geom = schema.getGeometryDescriptor();
		List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
		GeometryType geomType = null;
		List<AttributeDescriptor> attribs = new ArrayList<AttributeDescriptor>();
		int i =0;
		int k =0;
		for (AttributeDescriptor attrib : attributes) {
			i++;
			AttributeType type = attrib.getType();
			if (type instanceof GeometryType) {
				geomType = (GeometryType) type;
				k = i;
			} else {
				attribs.add(attrib);
			}
		}

		GeometryTypeImpl gt = new GeometryTypeImpl(new NameImpl("geometry"),
				geomType.getBinding(), crs, geomType.isIdentified(),
				geomType.isAbstract(), geomType.getRestrictions(),
				geomType.getSuper(), geomType.getDescription());

		GeometryDescriptor geomDesc = new GeometryDescriptorImpl(gt,
				new NameImpl("the_geom"), geom.getMinOccurs(),
				geom.getMaxOccurs(), geom.isNillable(), geom.getDefaultValue());

		attribs.add(0, geomDesc);

		SimpleFeatureType shpType = new SimpleFeatureTypeImpl(schema.getName(),
				attribs, geomDesc, schema.isAbstract(),
				schema.getRestrictions(), schema.getSuper(),
				schema.getDescription());
		return shpType;
	}

	/**
	 * 把元素集合 写成shp的元素集合
	 * 
	 * @param ftColl
	 * @return
	 */
	private static SimpleFeatureCollection rewriteSimpleFeatureCollection(
			SimpleFeatureCollection ftColl, SimpleFeatureType featureType) {
		    String geometryName = "geometry";
		    ListFeatureCollection collection = new ListFeatureCollection(featureType); 
	        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);  
		SimpleFeatureIterator itertor = ftColl.features();
		int j = 0;
		while (itertor.hasNext()) {
			j++;
			SimpleFeature feature = itertor.next();
			Collection<Property>  ps = feature.getProperties();
			 Object[] obj = new Object[ps.size()];
			 int i = 1;
			 Property geom =  feature.getProperty(geometryName);
			 obj[0] = geom.getValue();
			for(Property prop : ps){
				String name = prop.getName().toString();
				Object value =prop.getValue();
				if(!name.equals(geometryName)){
					obj[i] = value;
					i++;
				}
				
			}
	        SimpleFeature newfeature = featureBuilder.buildFeature(j+"", obj);
	        collection.add(newfeature);
		}
		itertor.close();
		return collection;
		//	Property propertyNew = new GeometryAttributeImpl(
		//property.getValue(), (GeometryDescriptor) descriptor,
		//feature.getIdentifier());
		
	}

	/**
	 * 根据编码 把字符串写入文件
	 * 
	 * @param saveFile
	 *            路径
	 * @param content
	 *            内容
	 * @param cs
	 *            编码
	 */
	public static void toFileByCS(File saveFile, String content, String cs) {
		File parent = saveFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		PrintWriter out = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(saveFile);
			if (cs.equals("")) {
				osw = new OutputStreamWriter(fos);
			}else{
				osw = new OutputStreamWriter(fos, cs);
			}
			out = new PrintWriter(osw);
			out.print(content);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把文件读成字符串
	 * 
	 * @param path
	 *            路径
	 * @param encoding
	 *            编码
	 * @return
	 * @throws Exception
	 */
	public static String readerFileToStrings(String path, String encoding)
			throws Exception {
		FileInputStream input = null;
		InputStreamReader insreader = null;
		BufferedReader bin = null;
		File flie = null;
		StringBuffer returnStr = null;
		try {
			flie = new File(path);
			returnStr = new StringBuffer();
			if (flie.exists() && flie.canRead()) {
				input = new FileInputStream(flie);
				if (encoding.equals("")) {
					insreader = new InputStreamReader(input);
				} else {
					insreader = new InputStreamReader(input, encoding);
				}

				bin = new BufferedReader(insreader);

				String line;
				while ((line = bin.readLine()) != null) {
					returnStr.append(line);
				}
				bin.close();
				insreader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("err!");
		} finally {
			if (bin != null) {
				try {
					bin.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (insreader != null) {
				try {
					insreader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return returnStr.toString();
	}
	
	/**
	 * 读字节
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static byte[] toBytes(InputStream input) throws Exception {
		byte[] data = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int read = 0;
			while ((read = input.read(b)) > 0) {
				byteOut.write(b, 0, read);
			}
			data = byteOut.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
		return data;
	}

	public static String getCRSStr() {
		return CRSStr;
	}

	public static void setCRSStr(String cRSStr) {
		CRSStr = cRSStr;
	}


}
