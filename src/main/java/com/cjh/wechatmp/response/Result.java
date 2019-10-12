
package com.cjh.wechatmp.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjh.wechatmp.enums.ErrorEnum;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;

/**
 * 响应数据
 */
public class Result extends HashMap<String, Object> {

    public static Result failure() {
        return failure(ErrorEnum.FAIL.getCode(), ErrorEnum.FAIL.getName());
    }

    public static Result failure(String msg) {
        return failure(ErrorEnum.FAIL.getCode(), msg);
    }

    public static Result failure(ErrorEnum status) {
        return failure(status.getCode(), status.getName());
    }

    public static Result failure(int code, String msg) {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    public static Result success() {
        return success(ErrorEnum.OK.getCode(), ErrorEnum.OK.getName());
    }

    public static Result success(String msg) {
        return success(ErrorEnum.OK.getCode(), msg);
    }

    private static Result success(int code, String msg) {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", Maps.newHashMap());
        return result;
    }

    /**
     * 设置data数据
     */
    public <T> Result setData(T data) {
        this.put("data", data);
        return this;
    }

    /**
     * 获取data数据
     */
    public <T> T getData(Class<T> clazz) {
        return (T) this.get("data");
    }

    /**
     * data加数据
     */
    private void putData(String key, Object value) {
        ((HashMap) super.get("data")).put(key, value);
    }

    /**
     * data加分页数据
     */
    public <T> Result addData(IPage<T> page) {
        this.putData("page", page);
        return this;
    }

    /**
     * data加list
     */
    public <T> Result addData(List<T> list) {
        this.putData("list", list);
        return this;
    }

    /**
     * data加单个对象数据
     */
    public <T> Result addData(T info) {
        this.putData("info", info);
        return this;
    }
}
