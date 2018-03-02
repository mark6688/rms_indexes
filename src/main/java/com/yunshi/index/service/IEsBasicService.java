package com.yunshi.index.service;

import java.util.List;
import java.util.Map;

/**
 * ES操作管理
 * Created by mk on 2017/11/28.
 */
public interface IEsBasicService {

    /**
     * 创建索引以及创建mapping规则
     * @return
     */
    boolean insertDefaultIndexDatabase()throws Exception;


    /**
     * 创建索引以及创建mapping规则
     * @param indexName
     * @param indexType
     * @param mappingMap
     * @return
     */
    boolean insertIndexDatabase(String indexName, String indexType,  Map<String,Map<String,String>> mappingMap)throws Exception;


    /**
     *  创建索引数据
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param map 索引数据
     * @return
     */
    boolean insertIndexData(String indexName, String indexType, Map<String, Object> map)throws Exception;

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
    Map<String, Object> queryIndexDataByIds(String indexName, String indexType, List<String> ids);

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
     * 根据索引和mapping更新es中的数据
     * @param indexName
     * @param indexType
     * @param ids
     * @param setMap
     * @return
     */
    boolean updateIndexDataByIds(String indexName, String indexType, List<String> ids, Map <String, Object> setMap)throws Exception;

    /**
     * 根据索引和mapping更新es中的数据
     * @param indexName
     * @param indexType
     * @param id
     * @param setMap
     * @return
     */
    //boolean updateIndexDataById(String indexName, String indexType, String id, Map <String, Object> setMap)throws Exception;

    /**
     * 根据索引和mapping批量更新es中的数据
     * @param indexName
     * @param indexType
     * @param ids
     * @param setMap
     * @return
     */
    //boolean updateIndexDataByIds(String indexName, String indexType, List <String> ids, Map <String, Object> setMap)throws Exception;

    /**
     * 根据text查询分词结果
     * @param esIndex
     * @param type
     * @param text
     * @return
     */
    Map<String, Object> queryAnalyzerByText(String esIndex,String type,String text)throws Exception;


    /**
     * 根据条件查询相关内容
     * @param queryMap 可选查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
    Map<String,Object> queryDataByQuery(Map<String,Map<String, Object>> queryMap,Map<String,Object> queryMust) throws Exception ;

    /**
     * 根据条件查询文稿相关内容
     * @param queryMap 查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
     Map<String,Object> queryCatalogueByQuery(Map<String, Object> queryMap, Map<String,Object> queryMust) throws Exception ;

    /**
     * 根据条件查询文稿相关内容
     * @param queryMap 查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
     Map<String,Object> queryMaterialByQuery(Map<String, Object> queryMap, Map<String,Object> queryMust) throws Exception ;

    /**
     * 根据条件查询审核相关内容
     * @param queryMap 查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
    Map<String,Object> queryCheckMsgByQuery(Map<String, Object> queryMap, Map<String,Object> queryMust) throws Exception ;

    /**
     * 根据条件查询分类管理相关内容
     * @param queryMap 查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
    Map<String,Object> queryCategoryByQuery(Map<String, Object> queryMap, Map<String,Object> queryMust) throws Exception ;

}
