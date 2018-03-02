package com.yunshi.index.domain;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * 文稿mapping规则
 * Created by mk on 2017/11/21.
 */
public class IndexCatalogue {

    public static XContentBuilder getXContentBuilderCataloguSet(String indexType) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject()
                    .startObject(indexType)
                        .startObject("properties")
                            .startObject(ParamVO.RESULTID).field("type", "string").endObject()
                            .startObject(Catalogue.TITLE).field("type", "string").field("analyzer", "ik_smart").endObject()
                            .startObject(Catalogue.CDETAIL).field("type", "string").field("analyzer", "ik_smart").field("store", "yes").endObject()
                            .startObject(Catalogue.TEMPLATEID).field("type", "string").endObject()
                            .startObject(Catalogue.SRC).field("type", "string").endObject()
                            .startObject(Catalogue.SLTHBS).field("type", "string").field("store", "yes").endObject()
                            .startObject(Catalogue.STATUS).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Catalogue.STATUSDETAIL).field("type", "string").field("store", "yes").endObject()
                            .startObject(Catalogue.MIDS).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Catalogue.CALLBACKURL).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Catalogue.COMPANYGROUP).field("type", "string").endObject()
                            .startObject(Catalogue.COMPANYID).field("type", "string").endObject()
                            .startObject(Catalogue.COMPANYNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Catalogue.DEPARTMENTID).field("type", "string").endObject()
                            .startObject(Catalogue.DEPARTMENTNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Catalogue.CUSERID).field("type", "string").endObject()
                            .startObject(Catalogue.CUSERNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Catalogue.CTIME).field("type", "date") .field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(Catalogue.CTIMESTAMP).field("type", "long").endObject()
                            .startObject(Catalogue.ISDEL).field("type", "integer").endObject()
                            .startObject(Catalogue.UUSERID).field("type", "string").endObject()
                            .startObject(Catalogue.UUSERNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(Catalogue.UTIME).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(Catalogue.UTIMESTAMP).field("type", "long").endObject()
                        .endObject()
                    .endObject()
            .endObject();
        return builder;
    }
}
