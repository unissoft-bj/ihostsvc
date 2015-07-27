package net.wyun.wmrecord.API;

import net.wyun.wmrecord.model.receptionmodel;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by guof on 5/18/2015.
 */
public interface receptionapi {
    @FormUrlEncoded
    @POST("/reception")
    public void getFeed(@Field("phone") String phone,@Field("mac") String mac,Callback<receptionmodel> response);
}
