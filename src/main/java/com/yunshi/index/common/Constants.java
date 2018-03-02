package com.yunshi.index.common;

/**
 *公共标识类
 */
public class Constants {

	private Constants(){}

	/**
	 * 编码格式
	 */
	public static final String CODED_FORMAT = "UTF-8";

	public static final String DATA = "data";

	public static final String ERROR = "error";

	public static final int NO = 0;
	public static final int YES = 1;

	/*ES索引,只在初始化创建mapping规则的时候用到了*/
	//public static final String INDEXNAME = "onair_resource";
	/*当前页数*/
	public static final String CURRENTPAGE = "currentPage";
	/*每页显示的条数*/
	public static final String PAGENUM = "pageNum";
	/*搜索条件字段*/
	public static final String QUERYMAP = "queryMap";
	/*必须的搜索条件*/
	public static final String QUERYMUST = "queryMust";
	/*排序字段*/
	public static final String ORDERWORD = "orderWord";
	/*排序值(升序或降序)*/
	public static final String ORDERVALUE = "orderValue";
	/*降序*/
	public static final String DESC = "desc";
	/*升序*/
	public static final String ASC = "asc";
	/*开始时间*/
	public static final String STARTTIME = "startTime";
	/*结束时间*/
	public static final String ENDTIME = "endTime";
	/** 关键字 */
	public static final String KEYWORD = "keywords";
	/*ES索引key*/
	public static final String ESINDEX = "esIndex";
	/*ES索引类型*/
	public static final String ESTYPE = "esType";
	/*ESmapping规则*/
	public static final String MAPPINGMAP = "mappingMap";
	/*存入ES的数据集合*/
	public static final String DATEMAP = "dateMap";
	/*更新ES的数据集合*/
	public static final String SETMAP = "setMap";
	/*返包含的字段标识*/
	public static final String INCLUDES = "includes";
	/*返不包含的字段标识*/
	public static final String EXCLUDES = "excludes";
	/*外网地址*/
	public static final String HTTPWAN = "httpWan";

}
