package com.lzugis.map.factory;

import com.lzugis.map.cont.ParamData;
import com.lzugis.map.gis.draw.impl.DrawMap_Contour_LegendImpl;
import com.lzugis.map.gis.draw.impl.DrawMap_Grid_StationImpl;
import com.lzugis.map.gis.draw.impl.DrawMap_Grid_Station_LegendImpl;
import com.lzugis.map.cont.MapType;
import com.lzugis.map.gis.draw.DrawMap;
import com.lzugis.map.gis.draw.impl.DrawMap_GridImpl;
import com.lzugis.map.gis.draw.impl.DrawMap_Grid_LegendImpl;
import com.lzugis.map.gis.draw.impl.DrawMap_Web_Polygon;

public class DrawMapFactory {


	/**
	 * 根据参数对象 生成地图绘制对象
	 * @param params 参数Json
	 * @return
	 */
	public static DrawMap read(ParamData pd){
		DrawMap dm = null;
		try {
			//grid 网格
			if(pd.getMapType().toLowerCase().equals(MapType.GRID)){
				dm = new DrawMap_GridImpl();
			}
			//grid_station 网格_带站点值
			else if(pd.getMapType().toLowerCase().equals(MapType.GRID_STATION)){
				dm = new DrawMap_Grid_StationImpl();
			}
			//grid_legend 网格_带图例标题
			else if(pd.getMapType().toLowerCase().equals(MapType.GRID_LEGEND)){
				dm = new DrawMap_Grid_LegendImpl();
			}
			//grid_station_legend 网格_带站点值_带图例标题
			else if(pd.getMapType().toLowerCase().equals(MapType.GRID_STATION_LEGEND)){
				dm = new DrawMap_Grid_Station_LegendImpl();
			}
			//web_polygon
			else if(pd.getMapType().toLowerCase().equals(MapType.WEB_POLYGON)){
				dm = new DrawMap_Web_Polygon();
			}
			//contour_legend
			else if(pd.getMapType().toLowerCase().equals(MapType.CONTOUR_LEGEND)){
				dm = new DrawMap_Contour_LegendImpl();
			}
			else{
				System.err.println("解析DrawMap 出错,未找到合适的类型-DataType");
				return null;
			}
		} catch (Exception e) {
			System.err.println("解析DrawMap 出错");
			return null;
		}
		return dm;
		
	}
	
}
