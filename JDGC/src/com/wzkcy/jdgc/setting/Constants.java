package com.wzkcy.jdgc.setting;

public class Constants {
	
	public static boolean Debug = true;
	public static boolean AppInitializeDialogShown = false;
	
	// 登录信息
	public static String UserName = "姓名";
	public static String UserID = "";
	public static String Account = ""; 
	public static String Password = "";
	
	public static boolean NewUntiRight = false;
	public static boolean NewProjectRight = false;
	public static boolean CompleteProjectRight = false;
	// PSTORE 应用序列号
	public static final String CLIENT_ID = "F08429053CBC2E84F9A9148B29870732";  // F08429053CBC2E84F9A9148B29870732
	public static String PAccount = ""; // 130938(吴国新)
	public static String PName = "";
	public static String PHost = "";
	public static String PIP = "";
	public static int PPort;
	// HTTP访问地址
	public static final String URL_HTTP = "http://127.0.0.1:6565/jdgc/";  //		http://218.75.26.59:8080/jdgc/      http://127.0.0.1:6565/jdgc/
	// FTP地址、端口、账号、密码
	public static String FTPHost = "192.168.1.5"; // 218.75.26.57 	192.168.1.5
	public static int FTPPort = 2121;
	public static String FTPUser = "anonymous";
	public static String FTPPassword = "123456";
	// 本地图片存储路径
	public static final String PICTURE_DIR_NAME = "照片信息";
	// 导出根目录
	public static final String EXPROT_DIR_NAME = "导出文件";
	public static final String IMPROT_DIR_NAME = "导入文件";
	public static final String EXPROT_DIR_DEFECTNAME = "缺陷照片";
	public static final String EXPROT_DIR_NORMALNAME = "普通照片";
	// 离线地图
	public static final String OFFLINE_MAP_ROOT_DIR = "离线地图";// 根目录
	public static final String MAP_LOCAL_DIR1 = "矢量";
	public static final String MAP_LOCAL_DIR2 = "影像";
	public static final String MAP_LOCAL_DIR3 = "2.5D";
	public static String MAP_LOCAL_FILE1 = "矢量.tpk";
	public static String MAP_LOCAL_FILE2 = "影像.tpk";
	public static String MAP_LOCAL_FILE3 = "2.5维.tpk";
	
	// 地图自动缓存目录
	public static final String OFFLINE_DIR = OFFLINE_MAP_ROOT_DIR ;
	public static final String CACHEPATH = Util.getAppRootPath() + "/" + OFFLINE_DIR + "/离线缓存";
	// geodatabase
	public static final String GEODATABASE_PATH1 = "离线地图/geodatabase/泰顺地名.geodatabase";
	public static final String GEODATABASE_PATH2 = "离线地图/geodatabase/泰顺标注.geodatabase";
	// 地图服务（外网）
//	public static final String MAP_SERVICE_PATH1 = "http://218.75.26.56:6080/arcgis/rest/services/pyditu/MapServer";
//	public static final String MAP_SERVICE_PATH1 = "http://www.go577.com/iserver/services/wzmap/wmts";
	
	public static final String MAP_SERVICE_PATH1 = "http://127.0.0.1:6080/arcgis/rest/services/TDT/gongan_公安矢量数据20150730/MapServer";
	public static final String MAP_SERVICE_PATH2 = "http://127.0.0.1:6080/arcgis/rest/services/TDT/gongan_公安影像2015/MapServer";
//	public static final String MAP_SERVICE_PATH1 = "http://218.75.26.56:6080/arcgis/rest/services/kfq_cgcs2000/shiliang_KFQ/MapServer"; 
//	public static final String MAP_SERVICE_PATH2 = "http://218.75.26.56:6080/arcgis/rest/services/kfq_cgcs2000/yingxiang_KFQ/MapServer";
	public static final String MAP_SERVICE_PATH3 = "http://218.75.26.56:6080/arcgis/rest/services/kfq_cgcs2000/erwuD_KFQ/MapServer";
	
	// 地名地址服务
//	public static final String MAP_SERVICE_DMDZ_WZ = "http://218.75.26.56:6080/arcgis/rest/services/wz_dmdz/wz_dmdz/FeatureServer/0";
	public static final String MAP_SERVICE_DMDZ_WZ = "http://127.0.0.1:6080/arcgis/rest/services/TDT_20160414/dmdz20160414/MapServer/0";
	// 路径规划服务
//	public static final String MAP_SERVICE_ROUTE_WZ = "http://218.75.26.56:6080/arcgis/rest/services/tstravel/network/NAServer/Route";
	public static final String MAP_SERVICE_ROUTE_WZ = "http://127.0.0.1:6080/arcgis/rest/services/TDT_20160414/ljfx20160414/NAServer/Route";
	
	public static final int MESSAGE_WHAT_BASE = 100;
	public static final int MESSAGE_WHAT_GET_POLICE_STATION_S = 101;
	public static final int MESSAGE_WHAT_GET_POLICE_STATION_F = 102;
	public static final int MESSAGE_WHAT_GET_NORMAL_STATION_S = 103;
	public static final int MESSAGE_WHAT_GET_NORMAL_STATION_F = 104;
	public static final int MESSAGE_WHAT_GET_POLICEMAN_S = 105;
	public static final int MESSAGE_WHAT_GET_POLICEMAN_F = 106;
	public static final int MESSAGE_WHAT_LOGIN_S = 107;
	public static final int MESSAGE_WHAT_LOGIN_F = 108;
	public static final int MESSAGE_WHAT_FIX_LOCATION_S = 109;
	public static final int MESSAGE_WHAT_FIX_LOCATION_F1 = 110;
	public static final int MESSAGE_WHAT_FIX_LOCATION_F2 = 111;
	public static final int MESSAGE_WHAT_GETQYGROUP_S = 112;
	public static final int MESSAGE_WHAT_GETQYGROUP_F1 = 113;
	public static final int MESSAGE_WHAT_GETQYGROUP_F2 = 114;
	public static final int MESSAGE_WHAT_GETJGROUP_S = 115;
	public static final int MESSAGE_WHAT_GETJGROUP_F1 = 116;
	public static final int MESSAGE_WHAT_GETJGROUP_F2 = 117;
	public static final int MESSAGE_WHAT_SEARCHBYKEY_S = 118;
	public static final int MESSAGE_WHAT_SEARCHBYKEY_F = 119;
	public static final int MESSAGE_WHAT_GET_ONLINE_POLICEMAN_S = 120;
	public static final int MESSAGE_WHAT_GET_ONLINE_POLICEMAN_F = 121;
	public static final int MESSAGE_WHAT_GET_POWERMAN_S = 122;
	public static final int MESSAGE_WHAT_GET_POWERMAN_F = 123;
	public static final int MESSAGE_WHAT_GET_CELLLOCATION_S = 124;
	public static final int MESSAGE_WHAT_GET_CELLLOCATION_EMPTY = 125;
	public static final int MESSAGE_WHAT_GET_CELLLOCATION_F = 126;
	
}
