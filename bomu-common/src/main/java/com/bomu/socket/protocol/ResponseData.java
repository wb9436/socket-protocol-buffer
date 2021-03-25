package com.bomu.socket.protocol;

import com.alibaba.fastjson.JSONObject;

/**
 * Socket 响应数据（默认返回200成功）
 */
public class ResponseData extends JSONObject {

    public ResponseData() {
        put("code", 200);
        put("msg", "成功");
    }

    @Override
    public ResponseData put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 放置错误信息
     *
     * @param msg 错误信息
     * @return
     */
    public ResponseData putError(String msg) {
        super.put("code", 201);
        super.put("msg", msg);
        return this;
    }

    /**
     * 放置错误信息
     *
     * @param code 错误码
     * @param msg  错误信息
     * @return
     */
    public ResponseData putError(int code, String msg) {
        super.put("code", code);
        super.put("msg", msg);
        return this;
    }

    /**
     * 放置错误信息
     *
     * @param error 错误数据（包含code和msg）
     * @return
     */
    public ResponseData putError(JSONObject error) {
        super.put("code", error.getIntValue("code"));
        super.put("msg", error.getString("msg"));
        return this;
    }

    /**
     * 返回错误信息
     *
     * @param msg 错误信息
     * @return
     */
    public static ResponseData error(String msg) {
        ResponseData resp = new ResponseData();
        resp.put("code", 201);
        resp.put("msg", msg);
        return resp;
    }

    /**
     * 返回错误信息
     *
     * @param code 错误码
     * @param msg  错误信息
     * @return
     */
    public static ResponseData error(int code, String msg) {
        ResponseData resp = new ResponseData();
        resp.put("code", code);
        resp.put("msg", msg);
        return resp;
    }

    /**
     * 返回错误信息
     *
     * @param error 错误数据（包含code和msg）
     * @return
     */
    public static ResponseData error(JSONObject error) {
        ResponseData resp = new ResponseData();
        resp.put("code", error.getIntValue("code"));
        resp.put("msg", error.getString("msg"));
        return resp;
    }

    /**
     * 直接返回成功
     *
     * @return
     */
    public static ResponseData ok() {
        ResponseData resp = new ResponseData();
        return resp;
    }

    /**
     * 返回成功，并修改成功提示
     *
     * @param msg
     * @return
     */
    public static ResponseData ok(String msg) {
        ResponseData resp = new ResponseData();
        resp.put("msg", msg);
        return resp;
    }

    /**
     * 返回成功，并携带成功信息
     *
     * @param data
     * @return
     */
    public static ResponseData ok(JSONObject data) {
        ResponseData resp = new ResponseData();
        resp.putAll(data);
        return resp;
    }

    /**
     * 返回成功，修改成功提示并携带成功信息
     *
     * @param msg
     * @param data
     * @return
     */
    public static ResponseData ok(String msg, JSONObject data) {
        ResponseData resp = new ResponseData();
        resp.put("msg", msg);
        resp.putAll(data);
        return resp;
    }


}
