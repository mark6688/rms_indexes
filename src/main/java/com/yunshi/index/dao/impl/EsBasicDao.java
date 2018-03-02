package com.yunshi.index.dao.impl;

import com.yunshi.index.common.Constants;
import com.yunshi.index.dao.IEsBasicDao;
import com.yunshi.index.dao.esdb.ElasticSearchJdbc;
import com.yunshi.index.domain.ParamVO;
import com.yunshi.index.util.StringUtil;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mk on 2017/11/22.
 */
@Repository
public class EsBasicDao implements IEsBasicDao {

    private static final Logger logger = Logger.getLogger("EsBasicDao");

    @Autowired
    ElasticSearchJdbc elasticSearchJdbc;

    /**
     * 创建索引以及创建mapping规则
     * @param indexName
     * @param indexType
     * @param builder
     * @return
     */
    @Override
    public boolean insertIndexDatabase(String indexName, String indexType, XContentBuilder builder){
        logger.info("=====EsBasicDao.insertIndexDatabase,进入创建index和mapping的方法=====");
        Client client = elasticSearchJdbc.getClient();
        logger.info("=====EsBasicDao.insertIndexDatabase,创建的client====="+client);
        /*** 判断是否存在该索引文件（库） ***/
        IndicesExistsResponse inrp = client.admin().indices().prepareExists(indexName).execute().actionGet();
        boolean flag = inrp.isExists();
        if (!flag) {
            /**** 创建索引文件（库） ****/
            client.admin().indices().prepareCreate(indexName).execute().actionGet();
        }
        /**** 创建索引类型（表） *****/
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(indexType).source(builder);
        PutMappingResponse ps = client.admin().indices().putMapping(mapping).actionGet();
        logger.info("=====EsBasicDao.insertIndexDatabase,方法结束====="+ps.isAcknowledged());
        return ps.isAcknowledged();
    }

    /**
     *  创建索引数据
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param map 索引数据
     * @return
     */
    @Override
    public boolean insertIndexData(String indexName, String indexType, Map<String,Object> map)throws Exception {
        boolean flag = false;
        Client client = elasticSearchJdbc.getClient();
        if(map.size()>0) {
            XContentBuilder source = XContentFactory.jsonBuilder().startObject();
            for (Map.Entry <String, Object> entry : map.entrySet()) {
                source.field(entry.getKey(), entry.getValue());
            }
            source.endObject();

            IndexResponse response = client.prepareIndex(indexName, indexType).setSource(source).get();
            RestStatus status = response.status();
            if(status.getStatus() == 201){
                flag = true;
            }
        }
        return flag;
    }

    /***
     * 根据业务id查询索引id 注：该方法主要应用于索引删除
     * @param indexName
     * @param indexType
     * @param id
     * @return
     */
    @Override
    public String queryIndexDataById(String indexName, String indexType, String id) {
        Client client = elasticSearchJdbc.getClient();
        SearchRequestBuilder requestBuilder = client.prepareSearch(indexName);
        requestBuilder.setTypes(indexType);
        requestBuilder.setPostFilter(QueryBuilders.termQuery(ParamVO.RESULTID, id));
        SearchResponse response = requestBuilder.get();
        SearchHits hits = response.getHits();
        String esId = "";
        for (SearchHit hit : hits) {
            esId = hit.getId();
        }
        return esId;
    }

    /**
     * 根据ID集合查询索引ID
     * @param indexName
     * @param indexType
     * @param ids
     * @return
     */
    @Override
    public Map<String, Object> queryIndexDataByIds(String indexName, String indexType, List<String> ids){
        Map<String, Object> listStrIds = new HashMap<String, Object>();
        Client client = elasticSearchJdbc.getClient();
        SearchRequestBuilder requestBuilder = client.prepareSearch(indexName);
        requestBuilder.setTypes(indexType);
        requestBuilder.setPostFilter(QueryBuilders.termsQuery(ParamVO.RESULTID, ids));
        SearchResponse response = requestBuilder.get();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            listStrIds.put(String.valueOf(hit.getSource().get(ParamVO.RESULTID)),hit.getId());
        }
        return listStrIds;
    }

    /**
     * 根据ID集合查询相关数据集合
     * @param indexName
     * @param indexType
     * @param ids
     * @return
     */
    @Override
    public Map<String, Object> queryDataByIds(String indexName, String indexType, List<String> ids){
        Map<String, Object> listStrIds = new HashMap<String, Object>();
        List<Map<String,Object>> list = new ArrayList <Map<String,Object>>();
        Client client = elasticSearchJdbc.getClient();
        SearchRequestBuilder requestBuilder = client.prepareSearch(indexName);
        requestBuilder.setTypes(indexType);
        requestBuilder.setPostFilter(QueryBuilders.termsQuery(ParamVO.RESULTID, ids));
        SearchResponse response = requestBuilder.get();
        SearchHits hits = response.getHits();
        long count = hits.getTotalHits();
        for (SearchHit hit : hits) {
            listStrIds.put(String.valueOf(hit.getSource().get(ParamVO.RESULTID)),hit.getId());
            Map<String, Object> map = hit.getSource();
            list.add(map);
        }
        listStrIds.put(ParamVO.TOTAL, count);
        listStrIds.put(ParamVO.RESULT, list);
        return listStrIds;
    }

    /**
     * 删除索引集合中的数据
     * @param indexName
     * @param indexType
     * @param ids
     * @return
     */
    @Override
    public boolean deleteIndexDataByIds(String indexName, String indexType, List<String> ids) {
        Client client = elasticSearchJdbc.getClient();
        BoolQueryBuilder bq = QueryBuilders.boolQuery();
        bq.must(QueryBuilders.matchQuery(ParamVO.RESULTID, ids));
        bq.must(QueryBuilders.matchQuery("_type", indexType));
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client).filter(bq).source(indexName).get();

        long deleted = response.getDeleted();
        if(deleted >0){
            return true;
        }
        return false;
    }

    /**
     * 根据索引和mapping更新es中的数据
     * @param indexName
     * @param indexType
     * @param id
     * @param setMap
     * @return
     */
    @Override
    public boolean updateIndexDataById(String indexName, String indexType, String id, Map<String,Object> setMap) throws Exception{
        Client client = elasticSearchJdbc.getClient();
        UpdateRequest request = new UpdateRequest();
        request.index(indexName);
        request.type(indexType);
        request.id(id);
        request.doc(setMap);
        UpdateResponse uresponse = client.update(request).get();
        client.admin().indices().refresh(new RefreshRequest(indexName));
        RestStatus status = uresponse.status();
        if(status.getStatus() == 200){
            return true;
        }
        return false;
    }

    /**
     * 根据索引和mapping更新es中的数据
     * @param indexName
     * @param indexType
     * @param ids
     * @param setMap
     * @return
     */
    @Override
    public boolean updateIndexDataByIds(String indexName, String indexType, List<String> ids, Map<String,Object> setMap) throws Exception{
        Client client = elasticSearchJdbc.getClient();
        UpdateRequest request = new UpdateRequest();
        request.index(indexName);
        request.type(indexType);
        request.doc(setMap);
        RestStatus status = null;
        for (String id : ids) {
            request.id(id);
            UpdateResponse uresponse = client.update(request).get();
            client.admin().indices().refresh(new RefreshRequest(indexName));
             status = uresponse.status();
        }
        if(status.getStatus() == 200){
            return true;
        }
        return false;
    }

    /**
     * 根据条件查询相关内容
     * @param bq 查询条件
     * @param queryMust 必须包含的查询条件
     * @param includes 包含的字段
     * @param excludes 不包含的字段
     * @return
     */
    @Override
    public Map<String,Object> queryDataByQuery(BoolQueryBuilder bq, Map<String,Object> queryMust, String[] includes, String[] excludes) throws Exception {
        Map<String,Object> rstMap = new HashMap<String,Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Client client = elasticSearchJdbc.getClient();
        SearchRequestBuilder srb = client.prepareSearch(String.valueOf(queryMust.get(Constants.ESINDEX)));
        srb.setTypes(String.valueOf(queryMust.get(Constants.ESTYPE)));
        //设置返回字段
        srb.setFetchSource(includes,excludes);
        //排序方式 desc降序 asc升序
        if(!StringUtil.isEmpty(queryMust.get(Constants.ORDERWORD))) {
            if (queryMust.get(Constants.ORDERVALUE).equals(Constants.ASC)) {
                srb.addSort(String.valueOf(queryMust.get(Constants.ORDERWORD)), SortOrder.ASC);
            } else {
                srb.addSort(String.valueOf(queryMust.get(Constants.ORDERWORD)), SortOrder.DESC);
            }
        }
        srb.setQuery(bq);
        int start = (Integer.parseInt(String.valueOf(queryMust.get(Constants.CURRENTPAGE))) - 1) * Integer.parseInt(String.valueOf(queryMust.get(Constants.PAGENUM)));
        SearchResponse response = srb.setFrom(start).setSize(Integer.parseInt(String.valueOf(queryMust.get(Constants.PAGENUM)))).execute().get();
        SearchHits shs = response.getHits();
        long count = shs.getTotalHits();
        for (SearchHit hit : shs) {
            Map<String, Object> map = hit.getSource();
            list.add(map);
        }
        rstMap.put(ParamVO.TOTAL, count);
        rstMap.put(ParamVO.RESULT, list);
        return rstMap;
    };

}
