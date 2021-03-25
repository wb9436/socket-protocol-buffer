package com.bomu.socket.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import protocol.MetaDataBase;

public class MetaData {
    private String sys;
    private String module;
    private String direct;
    private String GToken;

    public MetaData() {
    }

    public MetaData(String sys, String module, String direct) {
        this.sys = sys;
        this.module = module;
        this.direct = direct;
    }

    public MetaData(String sys, String module, String direct, String GToken) {
        this.sys = sys;
        this.module = module;
        this.direct = direct;
        this.GToken = GToken;
    }

    public MetaData(MetaDataBase.MetaData request) {
        this.sys = request.getSys();
        this.module = request.getModule();
        this.direct = request.getDirect();
        this.GToken = request.getGToken();
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getGToken() {
        return GToken;
    }

    public void setGToken(String GToken) {
        this.GToken = GToken;
    }

    public MetaData putDirect(String direct) {
        this.direct = direct;
        return this;
    }

    @JSONField(serialize = false)
    public String getSysModule() {
        StringBuffer sb = new StringBuffer();
        sb.append(sys).append(module);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "sys='" + sys + '\'' +
                ", module='" + module + '\'' +
                ", direct='" + direct + '\'' +
                '}';
    }

}
