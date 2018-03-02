package com.yunshi.index.domain;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.Map;

/**
 * 基础mapping规则
 * Created by mk on 2017-11-29
 */
public class BasicMapping {

    public static XContentBuilder getXContentBuilderSet(String indexType, Map<String,Map<String,String>> mappingMap) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject()
                    .startObject(indexType)
                        .startObject("properties");
                            /*循环建立mapping，便于检索*/
                            for (String key:mappingMap.keySet()){
                                builder.startObject(key);
                                Map<String,String> fieldMap = mappingMap.get(key);
                                for (String fieldKey: fieldMap.keySet()) {
                                    builder.field(fieldKey,fieldMap.get(fieldKey));
                                }
                                builder.endObject();
                            }
                        builder.endObject()
                    .endObject()
            .endObject();
        return builder;
    }
}
