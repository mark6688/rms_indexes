package com.yunshi.index.domain;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * 分类mapping规则
 * Created by mk on 2017/11/21.
 */
public class IndexCategroy {

    public static XContentBuilder getXContentBuilderCategroySet(String indexType) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject()
                    .startObject(indexType)
                        .startObject("properties")
                            .startObject(ParamVO.RESULTID).field("type", "string").endObject()
                            .startObject(ParamVO.RESULTID).field("type", "string").endObject()
                            .startObject(Categroy.CATEID).field("type", "string").endObject()
                            .startObject(Categroy.CATENAME).field("type", "string").endObject()
                            .startObject(Categroy.PARENTID).field("type", "string").endObject()
                            .startObject(Categroy.LEVEL).field("type", "string").endObject()
                            .startObject(Categroy.COMPANYGROUP).field("type", "string").endObject()
                            .startObject(Categroy.COMPANYID).field("type", "string").endObject()
                            .startObject(Categroy.COMPANYNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Categroy.DEPARTMENTID).field("type", "string").endObject()
                            .startObject(Categroy.DEPARTMENTNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Categroy.CUSERID).field("type", "string").endObject()
                            .startObject(Categroy.CUSERNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Categroy.CTIME).field("type", "date") .field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(Categroy.CTIMESTAMP).field("type", "long").endObject()
                            .startObject(Categroy.ISDEL).field("type", "integer").endObject()
                            .startObject(Categroy.UUSERID).field("type", "string").endObject()
                            .startObject(Categroy.UUSERNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Categroy.UTIME).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(Categroy.UTIMESTAMP).field("type", "long").endObject()
                        .endObject()
                    .endObject()
            .endObject();
        return builder;
    }
}
