package net.wyun.wmrecord.model;

import com.google.gson.annotations.Expose;

/**
 * Created by guof on 5/18/2015.
 */
public class receptionmodel {
    @Expose
    private String phone;
    @Expose
    private String mac;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
