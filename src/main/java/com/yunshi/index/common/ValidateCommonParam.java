package com.yunshi.index.common;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 该类是验证页面输入的json格式是否正确以及公共方法封装调用
 * 
 * @author huangaigang
 * @Description: validate方法验证, 返回true为通过, 反之return false.
 * @version： v1.0
 * @date 2015-11-29 23:06:20
 */
@Component
public class ValidateCommonParam {
	private static final Logger log = Logger.getLogger(ValidateCommonParam.class);

	/**
	 * 返回参数错误的验证信息
	 */
	public ResponseObject parameterError(Object o) {
		log.error("输入参数不合法！" + o);
		return new ResponseObject(GeneralStatus.input_error.status,
				GeneralStatus.input_error.detail, o);
	}

	/**
	 * 执行失败
	 */
	public void executeError(ResponseObject resObj) {
		resObj.setCode(GeneralStatus.inner_error.status);
		resObj.setMessage(GeneralStatus.inner_error.detail);
	}

	/**
	 * 执行成功
	 */
	public void executeSuccess(ResponseObject resObj) {
		resObj.setCode(GeneralStatus.success.status);
		resObj.setMessage(GeneralStatus.success.detail);
	}

	/**
	 * 执行失败带数据
	 */
	public void executeError(ResponseObject resObj,Object obj) {
		resObj.setCode(GeneralStatus.inner_error.status);
		resObj.setMessage(GeneralStatus.inner_error.detail);
		resObj.setData(obj);
	}

	/**
	 * 执行成功带数据
	 */
	public void executeSuccess(ResponseObject resObj,Object obj) {
		resObj.setCode(GeneralStatus.success.status);
		resObj.setMessage(GeneralStatus.success.detail);
		resObj.setData(obj);
	}

}
