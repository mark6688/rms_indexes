package com.yunshi.index.dao;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by mk on 2017/11/22.
 */
public interface IEsBasicDao {

    /**
     * 创建索引以及创建mapping规则
     * @param indexName
     * @param indexType
     * @param builder
     * @return
     */
     boolean insertIndexDatabase(String indexName, String indexType, XContentBuilder builder);


    /**
     *  创建索引数据
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param map 索引数据
     * @return
     */
     boolean insertIndexData(String indexName, String indexType, Map <String, Object> map)throws Exception;

    /***
     * 根据业务id查询索引id 注：该方法主要应用于索引删除
     * @param indexName
     * @param indexType
     * @param id
     * @return
     */
     String queryIndexDataById(String indexName, String indexType, String id);

    /**
     * 根据ID集合查询索引ID
     * @param indexName
     * @param indexType
     * @param ids
     * @return
     */
     Map<String, Object> queryIndexDataByIds(String indexName, String indexType, List <String> ids);

    /**
     * 根据ID集合查询相关数据集合
     * @param indexName
     * @param indexType
     * @param ids
     * @return
     */
    Map<String, Object> queryDataByIds(String indexName, String indexType, List <String> ids);


    /**
     * 删除索引集合中的数据
     * @param indexName
     * @param indexType
     * @param ids
     * @return
     */
     boolean deleteIndexDataByIds(String indexName, String indexType, List<String> ids);

    /**
     * 根据索引和mapping更新es中的数据
     * @param indexName
     * @param indexType
     * @param id
     * @param setMap
     * @return
     */
     boolean updateIndexDataById(String indexName, String indexType, String id, Map <String, Object> setMap)throws Exception;

    /**
     * 根据索引和mapping批量更新es中的数据
     * @param indexName
     * @param indexType
     * @param ids
     * @param setMap
     * @return
     */
    boolean updateIndexDataByIds(String indexName, String indexType, List <String> ids, Map <String, Object> setMap)throws Exception;

    /**
     * 根据条件查询相关内容
     * @param bq 查询条件
     * @param queryMust 必须包含的查询条件
     * @param includes 包含的字段
     * @param excludes 不包含的字段
     * @return
     */
     Map<String,Object> queryDataByQuery(BoolQueryBuilder bq, Map <String, Object> queryMust, String[] includes, String[] excludes) throws Exception ;

}
