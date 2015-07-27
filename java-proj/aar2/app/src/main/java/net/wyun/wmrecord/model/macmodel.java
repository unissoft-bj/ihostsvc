package net.wyun.wmrecord.model;

import com.google.gson.annotations.Expose;

/**
 * Created by guof on 6/15/2015.
 */
public class macmodel {
    @Expose
    private String mac;

    public String getMac(){
        return this.mac;
    }

    public void setMac(String value){
        this.mac = value;
    }
}
