package gis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lzugis.map.cont.LegendType;
import com.lzugis.map.cont.MapType;
import com.lzugis.map.gis.MainInterface;
import com.lzugis.map.param.MapParam;

public class Test {

	public Test() {
//		System.out.println(System.getProperty("user.dir"));
	}

	@org.junit.Test
    public void testDrawMap(){
		MapParam mpa = new MapParam();
	
		//数据
		Map<String, Object> mp = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		mp.put("x",102.7333 );
		mp.put("y", 24.35);
		mp.put("v", 15);
		mp.put("sName", "广西1");
		datas.add(mp);
		mp = new HashMap<String, Object>();
		mp.put("x", 102.169);
		mp.put("y", 24.2281);
		mp.put("v", 21);
		mp.put("sName", "广西2");
		datas.add(mp);
		mp = new HashMap<String, Object>();
		mp.put("x", 101.8069);
		mp.put("y", 23.8281);
		mp.put("v", 22);
		mp.put("sName", "广西3");
		datas.add(mp);
		mp = new HashMap<String, Object>();
		mp.put("x", 101.6069);
		mp.put("y", 24.1281);
		mp.put("v", 25);
		mp.put("sName", "广西4");
		datas.add(mp);
		mpa.setDatas(datas);
		
		//色带
		List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
		mp = new HashMap<String, Object>();
		mp.put("minvalue","-999" );
		mp.put("color", "245,200,200");
		mp.put("maxvalue","19.2" );
		mp.put("caption","<19.2" );
		rs.add(mp);
		mp = new HashMap<String, Object>();
		mp.put("minvalue","19.2" );
		mp.put("color", "245,183,48");
		mp.put("maxvalue","21.7" );
		mp.put("caption","19.2~21.7" );
		rs.add(mp);
		mp = new HashMap<String, Object>();
		mp.put("minvalue","21.7" );
		mp.put("color", "245,20,48");
		mp.put("maxvalue","25" );
		mp.put("caption","21.7~25" );
		rs.add(mp);
		mp = new HashMap<String, Object>();
		mp.put("minvalue","25" );
		mp.put("color", "0,183,48");
		mp.put("maxvalue","40" );
		mp.put("caption","25~40" );
		rs.add(mp);
		mp = new HashMap<String, Object>();
		mp.put("minvalue","40" );
		mp.put("color", "0,255,255");
		mp.put("maxvalue","999" );
		mp.put("caption",">40" );
		rs.add(mp);
		mpa.setRenderColors(rs);
		
		//citycode
		List<String> citys = new ArrayList<String>();
		citys.add("530400");
		mpa.setCitycodeList(citys);
		
		mpa.setDataTime("20170327");
		mpa.setTitle("20470327预报");
		mpa.setDataType("grid");
		mpa.setFeatureType("tmp");
		mpa.setLegendType(LegendType.L);
		
		//类型
//		mpa.setMapType(MapType.GRID);
		//mpa.setMapType(MapType.GRID_STATION);
		//mpa.setMapType(MapType.GRID_STATION_LEGEND);
		
		//mpa.setMapType(MapType.GRID_LEGEND);  //格点  时间长
		mpa.setMapType(MapType.WEB_POLYGON);  //前台展示用
//		mpa.setMapType(MapType.CONTOUR_LEGEND); //等值面
		
		boolean  rsult = MainInterface.readParamToDrawMap(mpa);
		System.err.println(rsult);
	}
	
}
