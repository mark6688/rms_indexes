package com.yunshi.index.util;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.List;
import java.util.Map;

/**
 * ES分词相关方法
 * Created by mk on 2017/11/23.
 */
public class EsUtil {

    private EsUtil(){}

    public static void paramSearch(String query,BoolQueryBuilder bq, Map<String, Object> param, String strKey){
        switch (query){
            case "match":
                paramMatchSearch(bq,param,strKey);
                break;
            case "one":
                paramOneSearch(bq,param,strKey);
                break;
            case "fuzzy":
                paramFuzzySearch(bq,param,strKey);
                break;
        }
    }

    /**
     * @Title: paramMatchSearch
     * @Description: (分词 搜索)
     * @param bq
     * @param param
     * @param strKey
     */
    public static void paramMatchSearch(BoolQueryBuilder bq, Map<String, Object> param, String strKey) {
        if (param.containsKey(strKey)) {
            String str = String.valueOf(param.get(strKey));
            if(!str.isEmpty()){
                BoolQueryBuilder bq2 = QueryBuilders.boolQuery();
                bq2.should(QueryBuilders.matchQuery(strKey, str));
                bq2.should(QueryBuilders.matchQuery(strKey, str));
                //bq.must(QueryBuilders.matchQuery(strKey, str));
                bq.must(bq2);
            }
        }
    }

    /**
     * @Title: paramMultiMatchQuery
     * @Description: (匹配相关字段)
     * @param bq
     * @param param
     * @param strKey
     */
    public static void paramMultiMatchQuery(BoolQueryBuilder bq, Map<String, Object> param, String strKey, List<String> query) {
        if (param.containsKey(strKey)) {
            String str = String.valueOf(param.get(strKey));
            if(!str.isEmpty()) {
                BoolQueryBuilder bq2 = QueryBuilders.boolQuery();
                for (String quStr : query) {
                    bq2.should(QueryBuilders.matchQuery(quStr, str));
                }
                bq.must(bq2);
            }
        }
    }

    /**
     * @Title: paramOneSearch
     * @Description: (封装单个查询条件)
     * @param bq
     * @param param
     * @param strKey
     */
    public static void paramOneSearch(BoolQueryBuilder bq, Map<String, Object> param, String strKey) {
        if (param.containsKey(strKey)) {
            String str = String.valueOf(param.get(strKey));
            if(!str.isEmpty()) {
                bq.must(QueryBuilders.termQuery(strKey, str));
            }
        }
    }

    /**
     * @Title: paramFuzzySearch
     * @Description: (模糊检索)
     * @param bq
     * @param param
     * @param strKey
     */
    public static void paramFuzzySearch(BoolQueryBuilder bq, Map<String, Object> param, String strKey) {
        if (param.containsKey(strKey)) {
            String str = String.valueOf(param.get(strKey));
            if(!str.isEmpty()) {
                bq.must(QueryBuilders.matchQuery(strKey, str));

            }
        }
    }

    /**
     * @Title: paramStartEndSearch
     * @Description: (查询指定字段的区间)
     * @param bq
     * @param param
     * @param strStartKey
     * @param strEndKey
     * @param strSearchKey
     * @author lyh
     * @date 2017-9-12 上午11:31:31
     */
    public static void paramStartEndSearch(BoolQueryBuilder bq, Map<String, Object> param, String strStartKey, String strEndKey, String strSearchKey) {
        if (param.containsKey(strStartKey) && param.containsKey(strEndKey)) {
            strStartKey = String.valueOf(param.get(strStartKey));
            strEndKey = String.valueOf(param.get(strEndKey));
            if(!strStartKey.isEmpty() && !strEndKey.isEmpty()) {
                bq.must(QueryBuilders.rangeQuery(strSearchKey).gte(strStartKey).lte(strEndKey));
            }
        } else if (param.containsKey(strStartKey)) {
            strStartKey = String.valueOf(param.get(strStartKey));
            if(!strStartKey.isEmpty()) {
                bq.must(QueryBuilders.rangeQuery(strSearchKey).gte(strStartKey));
            }
        } else if (param.containsKey(strEndKey)) {
            strEndKey = String.valueOf(param.get(strEndKey));
            if(!strEndKey.isEmpty()) {
                bq.must(QueryBuilders.rangeQuery(strSearchKey).lte(strEndKey));
            }
        }
    }
}
