package net.wyun.wmrecord.model;

import com.google.gson.annotations.Expose;

/**
 * Created by guof on 5/11/2015.
 */
public class wmsmodel {
    @Expose
    private String wms;

    @Expose
    private String version;

    public String getWms(){
        return this.wms;
    }

    public String getVersion(){
        return this.version;
    }

    public void setWms(String value){
        this.wms = value;
    }

    public void setVersion(String value){
        this.version = value;
    }
}