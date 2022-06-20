package com.manyun.common.core.domain;

import java.io.Serializable;
import com.manyun.common.core.constant.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
@ApiModel("通用返回属性")
@Data
public class R<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS = Constants.SUCCESS;

    /** 失败 */
    public static final int FAIL = Constants.FAIL;

    @ApiModelProperty(value = "状态码，200 就是成功", example = "状态码")
    private int code;

    @ApiModelProperty(value = "返回信息，一般状态码不是200,前台就可以展示", example = "返回信息")
    private String msg;

    @ApiModelProperty(value = "返回内容,一般查询才会有值", example = "返回信息")
    private T data;

    public R() {
    }

    public R(T data) {
        this.data = data;
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok()
    {
        return restResult(null, SUCCESS, null);
    }

    public static <T> R<T> ok(T data)
    {
        return restResult(data, SUCCESS, null);
    }

    public static <T> R<T> success(T data)
    {
        return new R<T>( SUCCESS, null,data);
    }

    public static <T> R<T> ok(T data, String msg)
    {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail()
    {
      return   new R<T>().restResult(null, FAIL, null);
        //return restResult();
    }

    public static <T> R<T> fail(String msg)
    {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data)
    {
        return restResult(data, FAIL, null);
    }

    public static <T> R<T> fail(T data, String msg)
    {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg)
    {
        return restResult(null, code, msg);
    }

/*    private static <T> R<T> restResult(T data, int code, String msg)
    {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }*/

    public static  <T> R<T> restResult(T data, int code, String msg){
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

}
