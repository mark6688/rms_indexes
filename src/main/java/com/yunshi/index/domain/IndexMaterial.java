package com.yunshi.index.domain;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * 文稿mapping规则
 * Created by mk on 2017/11/21.
 */
public class IndexMaterial {

    public static XContentBuilder getXContentBuilderMaterialSet(String indexType) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject()
                    .startObject(indexType)
                        .startObject("properties")
                            .startObject(ParamVO.RESULTID).field("type", "string").endObject()
                            .startObject(Material.NAME).field("type", "string").field("analyzer", "ik_smart").endObject()
                            .startObject(Material.MDETAIL).field("type", "string").field("analyzer", "ik_smart").field("store", "yes").endObject()
                            .startObject(Material.DURATION).field("type", "string").endObject()
                            .startObject(Material.SIZE).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.RATE).field("type", "string").field("store", "yes").endObject()
                            .startObject(Material.VIDEOCODE).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.AUDIOCODE).field("type", "string").field("store", "yes").endObject()
                            .startObject(Material.WIDTH).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.HEIGHT).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.FMT).field("type", "string").endObject()
                            .startObject(Material.TYPE).field("type", "string").endObject()
                            .startObject(Material.STATUS).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.HTTPWAN).field("type", "string").endObject()
                            .startObject(Material.IMAGEPATH).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.TEMPLATEID).field("type", "string").endObject()
                            .startObject(Material.SRC).field("type", "string").endObject()
                            .startObject(Material.CTIME).field("type", "date") .field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(Material.CTIMESTAMP).field("type", "long").endObject()
                            .startObject(Material.ISDEL).field("type", "integer").endObject()
                            .startObject(Material.CUSERID).field("type", "string").endObject()
                            .startObject(Material.DOCIDS).field("type", "string").endObject()
                            .startObject(Material.CUSERNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.UUSERID).field("type", "string").endObject()
                            .startObject(Material.UUSERNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.DEPARTMENTID).field("type", "string").endObject()
                            .startObject(Material.DEPARTMENTNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Material.UTIME).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(Material.UTIMESTAMP).field("type", "long").endObject()
                            .startObject(Material.TRANSCODES).field("type", "string").field("index", "not_analyzed").endObject()
                        .endObject()
                    .endObject()
            .endObject();
        return builder;
    }
}
