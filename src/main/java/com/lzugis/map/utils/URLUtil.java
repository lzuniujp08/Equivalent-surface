package com.lzugis.map.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class URLUtil {

	public URLUtil() {
	}

	/**
	 * 当前项目路径下
	 * 
	 * @param fileParent
	 *            项目路径下 文件前面路径 (如果是 直接项目下文件 传null)
	 * @param fileName
	 *            项目路径下 +文件前面路径+文件名 = 完整路径
	 * @return
	 */
	public static String getCurrnPath(String fileParent, String fileName) {
		File directory = new File(".");
		String fn = null;
		try {
			fn = directory.getCanonicalPath();
			if (fileParent != null) {
				fn = fn + File.separator + fileParent + File.separator
						+ fileName;
			} else {
				fn = fn + File.separator + fileName;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return fn;
		}
		return fn;
	}

	/**
	 * 根据类加载器获取路径,并且获取资源文件的字节流
	 * 
	 * @param getclass
	 *            类class
	 * @param fileName
	 *            文件名
	 * @return 路径
	 */
	public static InputStream  getClassFileInputStreamPathByClassLoader(Class<? extends Object> getclass,
			String fileName) {
		InputStream  is = null;
		try {
			if (getclass != null) {
				if (getclass.getClassLoader() != null&&getclass.getClassLoader().getResourceAsStream(fileName)!=null) {
					is = getclass.getClassLoader().getResourceAsStream(fileName);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return is;
		}
		return is;
	}
	
	
	/**
	 * 根据类加载器获取路径
	 * 
	 * @param getclass
	 *            类class
	 * @param fileName
	 *            文件名
	 * @return 路径
	 */
	public static String getClassFilePathByClassLoader(Class<? extends Object> getclass,
			String fileName) {
		String path = null;
		try {
			if (getclass != null) {
				if (getclass.getClassLoader() != null&&getclass.getClassLoader().getResource(fileName)!=null) {
					path = getclass.getClassLoader().getResource(fileName)
							.toString();

				}
				if (path != null && path.contains("file:/")) {
					path = path.replaceAll("file:/", "");
				}

				if (path != null && path.contains("jar:")) {
					path = path.replaceAll("jar:", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return path;
		}
		return path;
	}

	/**
	 * 根据类加载器获取项目跟路径
	 * 
	 * @param getclass
	 *            类class
	 * @param projectName
	 *            项目名称
	 * @return 路径
	 */
	public static String getRootPathByClassLoader(Class<? extends Object> getclass,
			String projectName) {
		String path = null;
		try {
			if (getclass != null) {
				if (getclass.getClassLoader() != null&&getclass.getClassLoader().getResource("")!=null) {
					path = getclass.getClassLoader().getResource("")
							.toString();

				}
				if (path != null && path.contains("file:/")) {
					path = path.replaceAll("file:/", "");
				}
				
				if (path != null && path.contains("file://")) {
					path = path.replaceAll("file://", "");
				}
				
				if (path != null && path.contains(projectName)) {
					path = path.substring(0, path.indexOf(projectName))+projectName;
				}
				
				if (path != null && path.contains("jar:")) {
					path = path.replaceAll("jar:", "");
				}
				
				if (path != null && path.contains("%20")) {
					path = path.replaceAll("%20", " ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return path;
		}
		return path;
	}
	
	/**
	 * 根据类加载器获取项目跟路径
	 * 
	 * @param getclass
	 *            类class
	 * @param projectName
	 *            项目名称
	 * @return 路径
	 */
	public static String getRootPathByClassLoader(String projectName) {
		String path = null;
		try {
			if (Object.class != null) {
				if (Object.class.getClassLoader() != null&&Object.class.getClassLoader().getResource("")!=null) {
					path = Object.class.getClassLoader().getResource("")
							.toString();

				}
				if (path != null && path.contains("file:/")) {
					path = path.replaceAll("file:/", "");
				}
				
				if (path != null && path.contains("file://")) {
					path = path.replaceAll("file://", "");
				}
				if (path != null && path.contains("null")) {
					path = path.replaceAll("null", "");
				}
				if (path != null && path.contains(projectName)) {
					path = path.substring(0, path.indexOf(projectName))+projectName;
				}
				
				if (path != null && path.contains("jar:")) {
					path = path.replaceAll("jar:", "");
				}
				
				if (path != null && path.contains("%20")) {
					path = path.replaceAll("%20", " ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return path;
		}
		return path;
	}
	
	/**
	 * 获取jar包路径
	 * @param getclass 类class
	 * @return jar包路径
	 */
	public static String getJarClassFilePath(Class<? extends Object> getclass) {
		String jarFilePath = null;
		try {
			jarFilePath = getclass.getProtectionDomain().getCodeSource()
					.getLocation().getFile();
			jarFilePath = java.net.URLDecoder.decode(jarFilePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return jarFilePath;
		}
		return jarFilePath;
	}
	
	/**
	 * 根据类来获取路径
	 * @param getclass class
	 * @param fileName  名称 
	 * @return
	 */
	public static String getClassFilePathByClass(Class<? extends Object> getclass,
			String fileName) {
		String path = null;
		try {
			if (getclass != null) {
				if(getclass.getResource(fileName)!=null){
					path = getclass.getResource(fileName)
							.toString();
				}
				if (path != null && path.contains("file:/")) {
					path = path.replaceAll("file:/", "");
				}

				if (path != null && path.contains("jar:")) {
					path = path.replaceAll("jar:", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return path;
		}
		return path;
	}

}
