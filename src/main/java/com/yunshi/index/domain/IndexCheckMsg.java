package com.yunshi.index.domain;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * 审核mapping规则
 * Created by mk on 2017/11/21.
 */
public class IndexCheckMsg {

    public static XContentBuilder getXContentBuilderCataloguSet(String indexType) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject()
                    .startObject(indexType)
                        .startObject("properties")
                            .startObject(ParamVO.RESULTID).field("type", "string").endObject()
                            .startObject(CheckMsg.NAME).field("type", "string").field("analyzer", "ik_smart").endObject()
                            .startObject(CheckMsg.PATH).field("type", "string").field("store", "yes").endObject()
                            .startObject(CheckMsg.SRC).field("type", "string").endObject()
                            .startObject(CheckMsg.STATUS).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.CMID).field("type", "string").field("store", "yes").endObject()
                            .startObject(CheckMsg.STATUS).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.SUBMITTIME).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(CheckMsg.SUBMITNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.CHECKIDS).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.CHECKMESSAGE).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.CHECKTIME).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(CheckMsg.CHECKLEVEL).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.CHECKID).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.CHECKNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.COMPANYGROUP).field("type", "string").endObject()
                            .startObject(CheckMsg.COMPANYID).field("type", "string").endObject()
                            .startObject(CheckMsg.COMPANYNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.DEPARTMENTID).field("type", "string").endObject()
                            .startObject(CheckMsg.DEPARTMENTNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.CUSERID).field("type", "string").endObject()
                            .startObject(CheckMsg.CUSERNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.CTIME).field("type", "date") .field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(CheckMsg.CTIMESTAMP).field("type", "long").endObject()
                            .startObject(CheckMsg.ISDEL).field("type", "integer").endObject()
                            .startObject(CheckMsg.UUSERID).field("type", "string").endObject()
                            .startObject(CheckMsg.UUSERNAME).field("type", "string").field("index", "not_analyzed").endObject()
                            .startObject(CheckMsg.UTIME).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                            .startObject(CheckMsg.UTIMESTAMP).field("type", "long").endObject()
                        .endObject()
                    .endObject()
            .endObject();
        return builder;
    }
}
