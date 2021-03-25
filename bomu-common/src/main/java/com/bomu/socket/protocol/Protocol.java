package com.bomu.socket.protocol;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.MetaDataBase;

public class Protocol {
    protected final Logger logger = LogManager.getLogger(getClass());

    public MetaData meta;
    public ResponseData data;

    public static String META = "meta";
    public static String DATA = "data";

    public Protocol() {
    }

    public Protocol(MetaData meta, JSONObject data) {
        this.meta = meta;
        this.data = ResponseData.ok(data);
    }

    public Protocol(MetaData meta, ResponseData data) {
        this.meta = meta;
        this.data = data;
    }

    public Protocol(MetaData meta) {
        this.meta = meta;
    }

    public MetaData getMeta() {
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }

    public MetaDataBase.MetaData getMetaData() {
        MetaDataBase.MetaData.Builder response = MetaDataBase.MetaData.newBuilder();
        response.setSys(meta.getSys());
        response.setModule(meta.getModule());
        response.setDirect(meta.getDirect());
        response.setData(data.toJSONString());
        return response.build();
    }

    @Override
    public String toString() {
        return "Protocol{" +
                ", meta=" + meta.toString() +
                ", data=" + data.toJSONString() +
                '}';
    }
}
