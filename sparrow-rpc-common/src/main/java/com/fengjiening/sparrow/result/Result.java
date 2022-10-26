package com.fengjiening.sparrow.result;


import com.fengjiening.sparrow.server.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *   接口返回数据格式
 * @author fengjiening
 */

@Data
@ApiModel(value="接口返回对象", description="接口返回对象")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@ApiModelProperty(value = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	@ApiModelProperty(value = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	@ApiModelProperty(value = "返回代码")
	private String code = CommonConstant.SC_OK;

	/**
	 * 返回数据对象 data
	 */
	@ApiModelProperty(value = "返回数据对象")
	private T data;

	/**
	 * 时间戳
	 */
	@ApiModelProperty(value = "时间戳")
	private long timestamp = System.currentTimeMillis();

	public Result() {

	}


	public Result<T> success(String message) {
		this.message = message;
		this.code = CommonConstant.SC_OK;
		this.success = true;
		return this;
	}


	public static Result<Object> ok() {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK);
		r.setMessage("成功");
		return r;
	}

	public static Result<Object> ok(String msg) {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK);
		r.setMessage(msg);
		return r;
	}

	public static  Result<Object> ok(Object data) {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK);
		r.setData(data);
		return r;
	}
	public static  Result<Object> ok(Object data,String message) {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK);
		r.setData(data);
		r.setMessage(message);
		return r;
	}

	public static Result<Object> ok(String code,Object data) {
		Result<Object> r = new Result<Object>();
		r.setCode(code);
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK);
		r.setData(data);
		return r;
	}

	public static Result<Object> error(String msg) {
		return error(CommonConstant.SC_500, msg);
	}


	public static Result<Object> error(String code, String msg) {
		Result<Object> r = new Result<Object>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}

}
