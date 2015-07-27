package net.wyun.wmrecord.API;

import net.wyun.wmrecord.model.wmsmodel;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by guof on 5/11/2015.
 */
public interface wmsapi {
    @GET("/wms")
    public void getFeed(Callback<wmsmodel> response);
}
