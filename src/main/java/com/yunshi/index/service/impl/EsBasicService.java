package com.yunshi.index.service.impl;

import com.yunshi.index.common.Constants;
import com.yunshi.index.dao.IEsBasicDao;
import com.yunshi.index.dao.esdb.ElasticSearchJdbc;
import com.yunshi.index.domain.*;
import com.yunshi.index.service.IEsBasicService;
import com.yunshi.index.util.EsUtil;
import com.yunshi.index.util.HttpUtilHelper;
import com.yunshi.index.util.JSONUtils;
import com.yunshi.index.util.StringUtil;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ES操作管理
 * Created by mk on 2017/11/28.
 */
@Service
public class EsBasicService implements IEsBasicService {

    @Autowired
    private IEsBasicDao iEsBasicDao;
    @Autowired
    private ElasticSearchJdbc elasticSearchJdbc;

    /**
     * 创建索引以及创建mapping规则(初始化进行创建的方法)
     * @return
     */
    @Override
    public boolean insertDefaultIndexDatabase()throws Exception {
        boolean str = false;
       /* XContentBuilder catalogueBuilder = IndexCatalogue.getXContentBuilderCataloguSet(Catalogue.CATALOGUE);
        str = iEsBasicDao.insertIndexDatabase(Constants.INDEXNAME, Catalogue.CATALOGUE,catalogueBuilder);
        if (str){
            XContentBuilder materialBuilder = IndexMaterial.getXContentBuilderMaterialSet(Material.MATERIAL);
           str = iEsBasicDao.insertIndexDatabase(Constants.INDEXNAME, Material.MATERIAL,materialBuilder);
           if(str){
               XContentBuilder checkMsgBuilder = IndexCheckMsg.getXContentBuilderCataloguSet(CheckMsg.CHECKMSG);
               str = iEsBasicDao.insertIndexDatabase(Constants.INDEXNAME, CheckMsg.CHECKMSG,checkMsgBuilder);
           }
        }*/

        XContentBuilder materialBuilder = IndexMaterial.getXContentBuilderMaterialSet(Material.MATERIAL);
        str = iEsBasicDao.insertIndexDatabase(elasticSearchJdbc.getIndexname(), Material.MATERIAL,materialBuilder);
        if (str){
            XContentBuilder CategroyBuilder = IndexCategroy.getXContentBuilderCategroySet(Categroy.CATEGORY);
            str = iEsBasicDao.insertIndexDatabase(elasticSearchJdbc.getIndexname(), Categroy.CATEGORY,CategroyBuilder);
        }
        return str;
    }

    /**
     * 创建索引以及创建mapping规则
     * @param indexName
     * @param indexType
     * @param mappingMap
     * @return
     */
    @Override
    public boolean insertIndexDatabase(String indexName, String indexType, Map<String,Map<String,String>> mappingMap)throws Exception {
        XContentBuilder builder =  BasicMapping.getXContentBuilderSet(indexType,mappingMap);
        return iEsBasicDao.insertIndexDatabase(indexName,indexType,builder);
    }

    /**
     *  创建索引数据
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param map 索引数据
     * @return
     */
    @Override
    public boolean insertIndexData(String indexName, String indexType, Map<String, Object> map) throws Exception {
        return iEsBasicDao.insertIndexData(indexName,indexType,map);
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
        return iEsBasicDao.queryIndexDataById(indexName,indexType,id);
    }

    /**
     * 根据ID集合查询索引ID
     * @param indexName
     * @param indexType
     * @param ids
     * @return
     */
    @Override
    public Map <String, Object> queryIndexDataByIds(String indexName, String indexType, List<String> ids) {
        return iEsBasicDao.queryIndexDataByIds(indexName,indexType,ids);
    }

    /**
     * 删除索引集合中的数据
     * @param indexName
     * @param indexType
     * @param ids
     * @return
     */
    @Override
    public boolean deleteIndexDataByIds(String indexName, String indexType,List<String> ids) {
        return iEsBasicDao.deleteIndexDataByIds(indexName,indexType,ids);
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
    public boolean updateIndexDataById(String indexName, String indexType, String id, Map <String, Object> setMap) throws Exception {
        //将id放入list中
        List<String> ids = new ArrayList <>();
        ids.add(id);
        //根据mongo中的id删除es中的内容
        iEsBasicDao.deleteIndexDataByIds(indexName,indexType,ids);
        //向ES中插入新的数据
        return iEsBasicDao.insertIndexData(indexName,indexType,setMap);
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
    public boolean updateIndexDataByIds(String indexName, String indexType, List<String> ids, Map <String, Object> setMap) throws Exception {
        boolean str = false;
        //1.根据ids查询出所有数据
        Map<String,Object> result =  iEsBasicDao.queryDataByIds(indexName,indexType,ids);
        //2.根据mongo中的id删除es中的内容
        iEsBasicDao.deleteIndexDataByIds(indexName,indexType,ids);
        //3.向ES中插入新的数据
        List<Map<String,Object>> results = (List<Map<String,Object>>)result.get(ParamVO.RESULT);
        for (Map<String,Object> resultMap:results) {
            //将需要修改的map进行替换
            resultMap.putAll(setMap);
            str = iEsBasicDao.insertIndexData(indexName,indexType,resultMap);
        }
        return str;
    }

    /**
     * 根据索引和mapping更新es中的数据
     * @param indexName
     * @param indexType
     * @param id
     * @param setMap
     * @return
     */
   /* public boolean updateIndexDataById(String indexName, String indexType, String id, Map <String, Object> setMap) throws Exception {
        return iEsBasicDao.updateIndexDataById(indexName,indexType,id,setMap);
    }*/

    /**
     * 根据索引和mapping批量更新es中的数据
     * @param indexName
     * @param indexType
     * @param ids
     * @param setMap
     * @return
     */
   /* public boolean updateIndexDataByIds(String indexName, String indexType, List <String> ids, Map <String, Object> setMap) throws Exception {
        return iEsBasicDao.updateIndexDataByIds(indexName,indexType,ids,setMap);
    }*/

    /**
     * 根据text查询分词结果
     * @param esIndex
     * @param type
     * @param text
     * @return
     */
    @Override
    public Map<String, Object> queryAnalyzerByText(String esIndex,String type,String text)throws Exception{
        String esurl = elasticSearchJdbc.getAnalyzer_serverip();
        String url = esurl+"/"+esIndex+"/_analyze?analyzer="+type+"&pretty=true&text="+text;
        //通过http调用ES
        String result = HttpUtilHelper.executeGet(url,null,null,null);
        Map <String, Object> resultlist = JSONUtils.json2map(result);
        return resultlist;
    };

    /**
     * 根据条件查询相关内容
     * @param queryMap 可选查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
    @Override
    public Map <String, Object> queryDataByQuery(Map<String,Map<String, Object>> queryMap,Map<String,Object> queryMust) throws Exception {

        BoolQueryBuilder bq = QueryBuilders.boolQuery();
        //判断要拼接的查询条件，并进行条件的拼装
        for (String key:queryMap.keySet()) {
            Map<String,Object> map = queryMap.get(key);
            for (String key1:map.keySet()) {
                EsUtil.paramSearch(key,bq,map,key1);
            }
        }
        String[] includes = new String[]{};
        String[] excludes = new String[]{};
        return iEsBasicDao.queryDataByQuery(bq,queryMust,includes,excludes);
    }

    /**
     * 根据条件查询文稿相关内容
     * @param queryMap 查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
    @Override
    public Map<String,Object> queryCatalogueByQuery(Map<String, Object> queryMap, Map<String,Object> queryMust) throws Exception {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();

        /*** 页面条件字段 ***/
        //删除状态
        bq.must(QueryBuilders.termQuery(Catalogue.ISDEL, Constants.NO));
        //文稿标题
        EsUtil.paramFuzzySearch(bq, queryMap, Catalogue.TITLE);
        //文稿模板
        EsUtil.paramMatchSearch(bq, queryMap,Catalogue.CDETAIL);
        //文稿状态
        EsUtil.paramOneSearch(bq, queryMap, Catalogue.STATUS);
        // 关键字搜索
        List<String> stringList = new ArrayList<String>();
        stringList.add(Catalogue.TITLE);
        stringList.add(Catalogue.CDETAIL);
        stringList.add(Catalogue.SRC);
        EsUtil.paramMultiMatchQuery(bq, queryMap, Constants.KEYWORD,stringList);
        /*上传时间*/
        EsUtil.paramStartEndSearch(bq, queryMap, Constants.STARTTIME, Constants.ENDTIME,Catalogue.CTIME);

        //添加必须的条件
        queryMust.put(Constants.ESINDEX,String.valueOf(queryMap.get(Constants.ESINDEX)));
        queryMust.put(Constants.ESTYPE,String.valueOf(queryMap.get(Constants.ESTYPE)));

        List<String> incloudsList = (List<String>)queryMust.get("inclouds");
        String[] inclouds = (String[])incloudsList.toArray(new String[incloudsList.size()]);

        List<String> excloudsList = (List<String>)queryMust.get("exclouds");
        String[] exclouds = (String[])excloudsList.toArray(new String[excloudsList.size()]);

        return iEsBasicDao.queryDataByQuery(bq,queryMust,inclouds,exclouds);
    };

    /**
     * 根据条件查询文稿相关内容
     * @param queryMap 查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
    @Override
    public Map<String,Object> queryMaterialByQuery(Map<String, Object> queryMap, Map<String,Object> queryMust) throws Exception {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();

        /*** 页面条件字段 ***/
        //删除状态
        bq.must(QueryBuilders.termQuery(Material.ISDEL, Constants.NO));
        //素材标题
        EsUtil.paramFuzzySearch(bq, queryMap, Material.NAME);
        //素材模板
        EsUtil.paramMatchSearch(bq, queryMap,Material.MDETAIL);
        //素材状态
        EsUtil.paramOneSearch(bq, queryMap, Material.STATUS);
        
        EsUtil.paramOneSearch(bq, queryMap, Material.RESULTID);

        EsUtil.paramOneSearch(bq, queryMap, Material.CATEID);

		 /*上传时间*/
        EsUtil.paramStartEndSearch(bq, queryMap, Constants.STARTTIME, Constants.ENDTIME, Material.CTIME);
        //添加必须的条件
        queryMust.put(Constants.ESINDEX,(String)queryMap.get(Constants.ESINDEX));
        queryMust.put(Constants.ESTYPE,(String)queryMap.get(Constants.ESTYPE));

        //返回值字段控制
        List<String> incloudsList = (List<String>)queryMust.get("inclouds");
        String[] inclouds = (String[])incloudsList.toArray(new String[incloudsList.size()]);

        List<String> excloudsList = (List<String>)queryMust.get("exclouds");
        String[] exclouds = (String[])excloudsList.toArray(new String[excloudsList.size()]);

        return iEsBasicDao.queryDataByQuery(bq,queryMust,inclouds,exclouds);
    };

    /**
     * 根据条件查询审核相关内容
     * @param queryMap 查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
    @Override
    public Map<String,Object> queryCheckMsgByQuery(Map<String, Object> queryMap, Map<String,Object> queryMust) throws Exception {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();

        /*** 页面条件字段 ***/
        //删除状态
        bq.must(QueryBuilders.termQuery(CheckMsg.ISDEL, Constants.NO));
        //审核名称
        EsUtil.paramFuzzySearch(bq, queryMap, CheckMsg.NAME);
        //审核状态
        EsUtil.paramOneSearch(bq, queryMap, CheckMsg.STATUS);
        //审核类型
        EsUtil.paramOneSearch(bq, queryMap, CheckMsg.TYPE);
        //审核来源
        EsUtil.paramFuzzySearch(bq, queryMap, CheckMsg.SRC);
        //审核级别
        EsUtil.paramOneSearch(bq, queryMap, CheckMsg.CHECKLEVEL);

        // 关键字搜索
        List<String> stringList = new ArrayList<String>();
        stringList.add(CheckMsg.NAME);
        stringList.add(CheckMsg.SRC);
        EsUtil.paramMultiMatchQuery(bq, queryMap, Constants.KEYWORD,stringList);
		 /*上传时间*/
        EsUtil.paramStartEndSearch(bq, queryMap, Constants.STARTTIME, Constants.ENDTIME, CheckMsg.CTIME);
        //添加必须的条件
        queryMust.put(Constants.ESINDEX,(String)queryMap.get(Constants.ESINDEX));
        queryMust.put(Constants.ESTYPE,(String)queryMap.get(Constants.ESTYPE));


        List<String> incloudsList = (List<String>)queryMust.get("inclouds");
        String[] inclouds = (String[])incloudsList.toArray(new String[incloudsList.size()]);

        List<String> excloudsList = (List<String>)queryMust.get("exclouds");
        String[] exclouds = (String[])excloudsList.toArray(new String[excloudsList.size()]);

        return iEsBasicDao.queryDataByQuery(bq,queryMust,inclouds,exclouds);
    };

    /**
     * 根据条件查询分类管理相关内容
     * @param queryMap 查询条件
     * @param queryMust 必须包含的查询条件
     * @return
     */
    @Override
    public Map<String,Object> queryCategoryByQuery(Map<String, Object> queryMap, Map<String,Object> queryMust) throws Exception {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();

        //删除状态
        bq.must(QueryBuilders.termQuery(Categroy.ISDEL, Constants.NO));

        //id
        bq.must(QueryBuilders.termQuery(Categroy.CATEID, queryMap.get(Categroy.CATEID)));

		 /*上传时间*/
        EsUtil.paramStartEndSearch(bq, queryMap, Constants.STARTTIME, Constants.ENDTIME, Categroy.CTIME);
        //添加必须的条件
        queryMust.put(Constants.ESINDEX,(String)queryMap.get(Constants.ESINDEX));
        queryMust.put(Constants.ESTYPE,(String)queryMap.get(Constants.ESTYPE));

        List<String> incloudsList = (List<String>)queryMust.get("inclouds");
        String[] inclouds = (String[])incloudsList.toArray(new String[incloudsList.size()]);

        List<String> excloudsList = (List<String>)queryMust.get("exclouds");
        String[] exclouds = (String[])excloudsList.toArray(new String[excloudsList.size()]);

        Map<String,Object> result = new HashMap<String,Object>();

        if(!StringUtil.isEmpty(queryMap.get(Categroy.CATEID)) && !queryMap.get(Categroy.CATEID).equals("-1")){
            Map<String,Object> esresult = iEsBasicDao.queryDataByQuery(bq,queryMust,inclouds,exclouds);
            List<Map<String,Object>> esresults = (List<Map<String,Object>>)esresult.get("result");
            Map<String,Object> category = esresults.get(0);
            if (category == null) {
                return category;
            }
            Map<String, Object> categoryMap = new HashMap<String, Object>();
            categoryMap.put("cateId", category.get(Categroy.CATEID));
            categoryMap.put("cateName", category.get(Categroy.CATENAME));
            categoryMap.put("parentId", category.get(Categroy.PARENTID));
            categoryMap.put("level", category.get(Categroy.LEVEL));
            result.put("category", categoryMap);
        }

        BoolQueryBuilder bq1 = QueryBuilders.boolQuery();
        //删除状态
        bq1.must(QueryBuilders.termQuery(Categroy.ISDEL, Constants.NO));
        //父id
        bq1.must(QueryBuilders.termQuery(Categroy.PARENTID+".keyword", queryMap.get(Categroy.CATEID)));
		 /*上传时间*/
        EsUtil.paramStartEndSearch(bq1, queryMap, Constants.STARTTIME, Constants.ENDTIME, Categroy.CTIME);
        Map<String,Object> results = iEsBasicDao.queryDataByQuery(bq1,queryMust,inclouds,exclouds);

        if (results.size()>0){
            result.put("subCategories", results.get(ParamVO.RESULT));
            result.put("subTotal", results.get(ParamVO.TOTAL));
        }

        return result;
    };
}
