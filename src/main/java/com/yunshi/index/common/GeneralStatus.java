package com.yunshi.index.common;

import java.io.Serializable;

/**
 * 系统中的所有处理状态
 * @author mcxin
 *
 */
public enum GeneralStatus implements Serializable {
	//公共状态
	success("SUCCESS", "处理成功"),
	failure("FAILURE", "处理失败"),
	//公共错误返回状态
	input_error("INPUTPARAMETERINVALID","输入参数不合法"),
	inner_error("INNERERROR","系统内部错误");

	public String status; // 状态值
	public String detail; // 状态描述信息

	private GeneralStatus(String status, String detail) {
		this.status = status;
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "{status:" + this.status + ",detail:" + this.detail + "}";
	}
}
