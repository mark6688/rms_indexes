package com.yunshi.index.web.api;

import com.yunshi.index.common.Constants;
import com.yunshi.index.common.ResponseObject;
import com.yunshi.index.common.ValidateCommonParam;
import com.yunshi.index.domain.ParamVO;
import com.yunshi.index.service.IEsBasicService;
import com.yunshi.index.util.JSONUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ES操作管理
 * Created by mk on 2017/11/28.
 */
@Controller
@RequestMapping(value="api/esBasic")
public class EsBasicController {

    private static final Logger logger = Logger.getLogger("ValidateCommonParam");
    @Autowired
    private IEsBasicService iEsBasicService;
    @Autowired
    private ValidateCommonParam validateCommonParam;

    /**
     * 创建索引以及创建mapping规则(初始化调用方法)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/insertDefaultIndexDatabase")
    public ResponseObject insertDefaultIndexDatabase() {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("======1.进入默认方法：====");
            boolean str = iEsBasicService.insertDefaultIndexDatabase();
            if(str){
                validateCommonParam.executeSuccess(resObj);
            }else{
                validateCommonParam.executeError(resObj);
            }
        }catch (Exception e){
            e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : insertDefaultIndexDatabase 处理异常！");
        }
        return resObj;
    }

    /**
     * 创建索引以及创建mapping规则
     * @param strJson 入参(包括：esIndex、esType、mappingMap)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/insertIndexDatabase")
    public ResponseObject insertIndexDatabase(@RequestBody String strJson) {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : insertIndexDatabase 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            /*判断ES索引和mapping名称以及mapping规则参数是否存在*/
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(Constants.MAPPINGMAP)) {
                errorMap.put(Constants.MAPPINGMAP, "mappingMap is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.MAPPINGMAP));
            }
            Map<String,Map<String,String>> mappingMap = (Map<String,Map<String,String>>)jsonMap.get(Constants.MAPPINGMAP);
            if (mappingMap.isEmpty()) {
                errorMap.put(Constants.MAPPINGMAP, "mappingMap is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.MAPPINGMAP));
            }

           boolean str = iEsBasicService.insertIndexDatabase(esIndex,esType,mappingMap);
            if(str){
                validateCommonParam.executeSuccess(resObj);
            }else{
                validateCommonParam.executeError(resObj);
            }
        }catch (Exception e){
            e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : insertIndexDatabase 处理异常！");
        }
        return resObj;
    }

    /**
     *  创建索引数据
     * @param strJson 入参(包括：esIndex、esType、dateMap)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/insertIndexData")
    public ResponseObject insertIndexData(@RequestBody String strJson) throws Exception {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : insertIndexData 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            /*判断ES索引和mapping名称和dataMap参数是否存在*/
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(Constants.DATEMAP)) {
                errorMap.put(Constants.DATEMAP, "dateMap is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.DATEMAP));
            }
            Map<String,Object> dateMap = (Map<String,Object>)jsonMap.get(Constants.DATEMAP);
            if (dateMap.isEmpty()) {
                errorMap.put(Constants.DATEMAP, "dateMap is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.DATEMAP));
            }

            boolean str = iEsBasicService.insertIndexData(esIndex,esType,dateMap);
            if(str){
                validateCommonParam.executeSuccess(resObj);
            }else{
                validateCommonParam.executeError(resObj);
            }
        }catch (Exception e){
        	e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : insertIndexData 处理异常！");
        }
        return resObj;
    }

    /**
     * 删除索引集合中的数据(支持批量删除)
     * @param strJson 入参(包括：esIndex、esType、ids)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/deleteIndexDataByIds")
    public ResponseObject deleteIndexDataByIds(@RequestBody String strJson) {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : deleteIndexDataByIds 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            /*判断ES索引和mapping名称和id参数是否存在*/
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(ParamVO.RESULTIDS)) {
                errorMap.put(ParamVO.RESULTID, "ids is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }
            List<String> ids = (List<String>)jsonMap.get(ParamVO.RESULTIDS);
            if (ids.isEmpty()) {
                errorMap.put(ParamVO.RESULTID, "ids is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }

            boolean str = iEsBasicService.deleteIndexDataByIds(esIndex,esType,ids);
            if(str){
                validateCommonParam.executeSuccess(resObj);
            }else{
                validateCommonParam.executeError(resObj);
            }
        }catch (Exception e){
        	e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : deleteIndexDataByIds 处理异常！");
        }
        return resObj;
    }

     /**
     * 根据索引和mapping更新es中的数据（内部先调用删除的方法，再调用插入方法，完成更新）
     * @param strJson 入参(包括：esIndex、esType、id、setMap)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/updateIndexDataById")
    public ResponseObject updateIndexDataById(@RequestBody String strJson) throws Exception {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : updateIndexDataById 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            //*判断ES索引和mapping名称和id和setMap参数是否存在*//
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(ParamVO.RESULTID)) {
                errorMap.put(ParamVO.RESULTID, "id is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }
            String id = String.valueOf(jsonMap.get(ParamVO.RESULTID));
            if (id.isEmpty()) {
                errorMap.put(ParamVO.RESULTID, "id is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }
            if (!jsonMap.containsKey(Constants.SETMAP)) {
                errorMap.put(Constants.SETMAP, "setMap is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.SETMAP));
            }
            Map<String,Object> setMap = (Map<String,Object>)jsonMap.get(Constants.SETMAP);
            if (setMap.isEmpty()) {
                errorMap.put(Constants.SETMAP, "setMap is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.SETMAP));
            }

            boolean str = iEsBasicService.updateIndexDataById(esIndex,esType,id,setMap);
            if(str){
                validateCommonParam.executeSuccess(resObj);
            }else{
                validateCommonParam.executeError(resObj);
            }
        }catch (Exception e){
        	e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : updateIndexDataById 处理异常！");
        }
        return resObj;
    }

    /**
     * 根据索引和mapping更新es中的数据（内部先调用删除的方法，再调用插入方法，完成更新）
     * @param strJson 入参(包括：esIndex、esType、ids、setMap)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/updateIndexDataByIds")
    public ResponseObject updateIndexDataByIds(@RequestBody String strJson) throws Exception {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : updateIndexDataByIds 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            //*判断ES索引和mapping名称和id和setMap参数是否存在*//
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(ParamVO.RESULTIDS)) {
                errorMap.put(ParamVO.RESULTIDS, "ids is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }
            List<String> ids = (List<String>)jsonMap.get(ParamVO.RESULTIDS);
            if (ids.isEmpty()) {
                errorMap.put(ParamVO.RESULTIDS, "ids is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }
            if (!jsonMap.containsKey(Constants.SETMAP)) {
                errorMap.put(Constants.SETMAP, "setMap is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.SETMAP));
            }
            Map<String,Object> setMap = (Map<String,Object>)jsonMap.get(Constants.SETMAP);
            if (setMap.isEmpty()) {
                errorMap.put(Constants.SETMAP, "setMap is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.SETMAP));
            }

            boolean str = iEsBasicService.updateIndexDataByIds(esIndex,esType,ids,setMap);
            if(str){
                validateCommonParam.executeSuccess(resObj);
            }else{
                validateCommonParam.executeError(resObj);
            }
        }catch (Exception e){
            e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : updateIndexDataByIds 处理异常！");
        }
        return resObj;
    }

  /*  *//**
     * 根据索引和mapping更新es中的数据
     * @param strJson 入参(包括：esIndex、esType、id、setMap)
     * @return
     *//*
    @ResponseBody
    @RequestMapping("v1/updateIndexDataById")
    public ResponseObject updateIndexDataById(@RequestBody String strJson) throws Exception {
        ResponseObject resObj = new ResponseObject();
        try {
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            *//*判断ES索引和mapping名称和id和setMap参数是否存在*//*
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(ParamVO.RESULTID)) {
                errorMap.put(ParamVO.RESULTID, "id is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }
            String id = String.valueOf(jsonMap.get(ParamVO.RESULTID));
            if (id.isEmpty()) {
                errorMap.put(ParamVO.RESULTID, "id is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }
            if (!jsonMap.containsKey(Constants.SETMAP)) {
                errorMap.put(Constants.SETMAP, "setMap is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.SETMAP));
            }
            Map<String,Object> setMap = (Map<String,Object>)jsonMap.get(Constants.SETMAP);
            if (setMap.isEmpty()) {
                errorMap.put(Constants.SETMAP, "setMap is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.SETMAP));
            }

            boolean str = iEsBasicService.updateIndexDataById(esIndex,esType,id,setMap);
            if(str){
                validateCommonParam.executeSuccess(resObj);
            }else{
                validateCommonParam.executeError(resObj);
            }
        }catch (Exception e){
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : updateIndexDataById 处理异常！");
        }
        return resObj;
    }

    *//**
     * 根据索引和mapping批量更新es中的数据
     * @param strJson 入参(包括：esIndex、esType、ids、setMap)
     * @return
     *//*
    @ResponseBody
    @RequestMapping("v1/updateIndexDataByIds")
    public ResponseObject updateIndexDataByIds(@RequestBody String strJson) throws Exception {

        ResponseObject resObj = new ResponseObject();
        try {
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            *//*判断ES索引和mapping名称和ids和setMap参数是否存在*//*
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(ParamVO.RESULTIDS)) {
                errorMap.put(ParamVO.RESULTIDS, "ids is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTIDS));
            }
            List<String> ids = (List<String>)jsonMap.get(ParamVO.RESULTIDS);
            if (ids.isEmpty()) {
                errorMap.put(ParamVO.RESULTIDS, "ids is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTIDS));
            }
            if (!jsonMap.containsKey(Constants.SETMAP)) {
                errorMap.put(Constants.SETMAP, "setMap is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.SETMAP));
            }
            Map<String,Object> setMap = (Map<String,Object>)jsonMap.get(Constants.SETMAP);
            if (setMap.isEmpty()) {
                errorMap.put(Constants.SETMAP, "setMap is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.SETMAP));
            }

            boolean str = iEsBasicService.updateIndexDataByIds(esIndex,esType,ids,setMap);
            if(str){
                validateCommonParam.executeSuccess(resObj);
            }else{
                validateCommonParam.executeError(resObj);
            }
        }catch (Exception e){
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : updateIndexDataByIds 处理异常！");
        }
        return resObj;
    }*/

    /***
     * 根据业务id查询索引id 注：该方法主要应用于索引删除
     * @param strJson 入参(包括：esIndex、esType、id)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/queryIndexDataById")
    public ResponseObject queryIndexDataById(@RequestBody String strJson) {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : queryIndexDataById 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            /*判断ES索引和mapping名称以及id参数是否存在*/
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(ParamVO.RESULTID)) {
                errorMap.put(ParamVO.RESULTID, "id is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }
            String id = String.valueOf(jsonMap.get(ParamVO.RESULTID));
            if (id.isEmpty()) {
                errorMap.put(ParamVO.RESULTID, "id is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTID));
            }

            String esId = iEsBasicService.queryIndexDataById(esIndex,esType,id);
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put(ParamVO.RESULTID,esId);
            if(!esId.equals("")) {
                validateCommonParam.executeSuccess(resObj,resultMap);
            }else{
                validateCommonParam.executeError(resObj,resultMap);
            }
        }catch (Exception e){
        	e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : queryIndexByBId 处理异常！");
        }
        return resObj;
    }

    /**
     * 根据ID集合查询索引ID
     * @param strJson 入参(包括：esIndex、esType、ids)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/queryIndexDataByIds")
    public ResponseObject queryIndexDataByIds(@RequestBody String strJson) {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : queryIndexDataByIds 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            /*判断ES索引和mapping名称以及ids参数是否存在*/
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(ParamVO.RESULTIDS)) {
                errorMap.put(ParamVO.RESULTIDS, "ids is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTIDS));
            }
            List<String> ids = (List<String>)jsonMap.get(ParamVO.RESULTIDS);
            if (ids.isEmpty()) {
                errorMap.put(ParamVO.RESULTIDS, "ids is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.RESULTIDS));
            }

            Map<String, Object> esIds = iEsBasicService.queryIndexDataByIds(esIndex,esType,ids);
            if(esIds.size()>Constants.NO) {
                validateCommonParam.executeSuccess(resObj,esIds);
            }else{
                validateCommonParam.executeError(resObj,esIds);
            }
        }catch (Exception e){
        	e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : queryIndexByBIds 处理异常！");
        }
        return resObj;
    }

    /**
     * 根据输入内容，返回ik分词之后的结果
     * @param strJson 入参(包括：esIndex、esType、type、text)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/queryAnalyzerByText")
    public ResponseObject queryAnalyzerByText(@RequestBody String strJson) {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : queryAnalyzerByText 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            /*判断ES索引和mapping名称以及esId参数是否存在*/
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            //分词的类型
            if (!jsonMap.containsKey(ParamVO.TYPE)) {
                errorMap.put(ParamVO.TYPE, "type is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.TYPE));
            }
            String type = String.valueOf(jsonMap.get(ParamVO.TYPE));
            if (type.isEmpty()) {
                errorMap.put(ParamVO.TYPE, "type is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.TYPE));
            }

            //分词的内容
            if (!jsonMap.containsKey(ParamVO.TEXT)) {
                errorMap.put(ParamVO.RESULTIDS, "text is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.TEXT));
            }
            String text = String.valueOf(jsonMap.get(ParamVO.TEXT));
            if (text.isEmpty()) {
                errorMap.put(ParamVO.RESULTIDS, "text is empty!");
                return validateCommonParam.parameterError(errorMap.get(ParamVO.TEXT));
            }

            Map<String, Object> analyzerText = iEsBasicService.queryAnalyzerByText(esIndex,type,text);
            if(analyzerText.size()>Constants.NO) {
                validateCommonParam.executeSuccess(resObj,analyzerText);
            }else{
                validateCommonParam.executeError(resObj,analyzerText);
            }
        }catch (Exception e){
        	e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : queryAnalyzerByText 处理异常！");
        }
        return resObj;
    }

    /**
     * 根据条件查询相关内容可以随意查询，但是要做规则
     * @param strJson 入参(包括：esIndex、esType、bq、queryMust、includes、excludes)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/queryDataByQuery")
    public ResponseObject queryDataByQuery(@RequestBody String strJson) throws Exception {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : queryDataByQuery 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            /*判断ES索引和mapping名称以及esId参数是否存在*/
            if (!jsonMap.containsKey(Constants.ESINDEX)) {
                errorMap.put(Constants.ESINDEX, "esIndex is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }
            String esIndex = String.valueOf(jsonMap.get(Constants.ESINDEX));
            if (esIndex.isEmpty()) {
                errorMap.put(Constants.ESINDEX, "esIndex is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESINDEX));
            }

            if (!jsonMap.containsKey(Constants.ESTYPE)) {
                errorMap.put(Constants.ESTYPE, "esType is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }
            String esType = String.valueOf(jsonMap.get(Constants.ESTYPE));
            if (esType.isEmpty()) {
                errorMap.put(Constants.ESTYPE, "esType is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.ESTYPE));
            }

            if (!jsonMap.containsKey(Constants.QUERYMAP)) {
                errorMap.put(Constants.QUERYMAP, "queryMap is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.QUERYMAP));
            }
            Map<String,Map<String, Object>> queryMap = (Map<String,Map<String, Object>>)jsonMap.get(Constants.QUERYMAP);
            if (queryMap.isEmpty()) {
                errorMap.put(Constants.QUERYMAP, "queryMap is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.QUERYMAP));
            }
            if (!jsonMap.containsKey(Constants.QUERYMUST)) {
                errorMap.put(Constants.QUERYMUST, "queryMust is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.QUERYMUST));
            }
            Map<String,Object> queryMust = (Map<String,Object>)jsonMap.get(Constants.QUERYMUST);
            if (queryMust.isEmpty()) {
                errorMap.put(Constants.QUERYMUST, "queryMust is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.QUERYMUST));
            }

            //将索引和mapping标识添加到必要的查询条件中
            queryMust.put(Constants.ESINDEX,esIndex);
            queryMust.put(Constants.ESTYPE,esType);

            Map<String,Object> resultMap = iEsBasicService.queryDataByQuery(queryMap,queryMust);
            if(resultMap.size() >Constants.NO){
                validateCommonParam.executeSuccess(resObj,resultMap);
            }else{
                validateCommonParam.executeError(resObj,resultMap);
            }
        }catch (Exception e){
        	e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : queryDataByQuery 处理异常！");
        }
        return resObj;
    }

    /**
     * 根据条件查询文稿相关内容
     * @param strJson 入参(包括：esIndex、esType、bq、queryMust、includes、excludes)
     * @return
     */
    @ResponseBody
    @RequestMapping("v1/queryCatalogueByQuery")
    public ResponseObject queryCatalogueByQuery(@RequestBody String strJson) throws Exception {
        ResponseObject resObj = new ResponseObject();
        try {
            logger.info("class : EsBasicController --> method : queryCatalogueByQuery 接收的参数："+strJson);
            Map<String, Object> jsonMap = JSONUtils.json2map(strJson);
            Map<String, Object> errorMap = new HashMap<String, Object>();
            /*判断ES索引和mapping名称以及esId参数是否存在*/
            if (!jsonMap.containsKey(Constants.QUERYMAP)) {
                errorMap.put(Constants.QUERYMAP, "queryMap is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.QUERYMAP));
            }
            Map<String, Object> queryMap = (Map<String, Object>)jsonMap.get(Constants.QUERYMAP);
            if (queryMap.isEmpty()) {
                errorMap.put(Constants.QUERYMAP, "queryMap is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.QUERYMAP));
            }
            if (!jsonMap.containsKey(Constants.QUERYMUST)) {
                errorMap.put(Constants.QUERYMUST, "queryMust is Non-existent!");
                return validateCommonParam.parameterError(errorMap.get(Constants.QUERYMUST));
            }
            Map<String,Object> queryMust = (Map<String,Object>)jsonMap.get(Constants.QUERYMUST);
            if (queryMust.isEmpty()) {
                errorMap.put(Constants.QUERYMUST, "queryMust is empty!");
                return validateCommonParam.parameterError(errorMap.get(Constants.QUERYMUST));
            }

            Map<String,Object> resultMap = iEsBasicService.queryCatalogueByQuery(queryMap,queryMust);
            if(resultMap.size() >Constants.NO){
                validateCommonParam.executeSuccess(resObj,resultMap);
            }else{
                validateCommonParam.executeError(resObj,resultMap);
            }
        }catch (Exception e){
        	e.printStackTrace();
            validateCommonParam.executeError(resObj);
            logger.error("class : EsBasicController --> method : queryCatalogueByQuery 处理异常！");
        }
        return resObj;
    }

}
