package com.yunshi.index.domain;

/**
 * 素材表
 * Created by mk on 2017/11/15.
 */
public class Material extends BasicObject {
	
    /** 表名*/
    public static final String MATERIAL = "material";
   /**  素材名称*/
    public static final String NAME = "name";
    /** 素材时长	*/
    public static final String DURATION = "duration";
    /** 素材大小	*/
    public static final String SIZE = "size";
    /**  素材码率	*/
    public static final String RATE = "rate";
    /** 素材帧率	*/
    public static final String FRAME = "frame";
    /** 视频编码格式	*/
    public static final String VIDEOCODE = "videoCode";
    /**  音频编码格式	*/
    public static final String AUDIOCODE = "audioCode";
    /** 分辨率（宽）*/
    public static final String WIDTH = "width";
    /** 分辨率（长）*/
    public static final String HEIGHT = "height";
    /** 素材格式	*/
    public static final String FMT = "fmt";
    /** 素材类型（video，audio，img，doc）*/

   /** 素材状态（处理中，处理失败，处理成功）*/
    public static final String STATUS = "status";
    /** 素材状态（处理中，处理失败，处理成功）*/
    public static final String STATUSDETAIL="statusDetail";
   /** 素材地址*/
    public static final String HTTPWAN = "httpWan";
    /** 素材预览图*/
    public static final String IMAGEPATH = "imagePath";
    /** 素材模板	*/
    public static final String MDETAIL = "mDetail";
    /** 素材模板ID*/
    public static final String TEMPLATEID = "templateId";
    /**  素材来源	*/
    public static final String SRC = "src";
    /** 文稿ID*/
    public static final String DOCIDS = "docIds";
    /** 转码列表	*/
    public static final String TRANSCODES = "transcodes";
   /** 主键非下划线的id	*/
    public static final String RESULTID = "id";
   /** 分类id	*/
    public static final String CATEID = "cateId";



}
