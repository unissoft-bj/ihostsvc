package net.wyun.wmrecord.API;


import net.wyun.wmrecord.model.macmodel;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by guof on 6/15/2015.
 */
public interface macapi {
    @GET("/mac")
    public void getFeed(Callback<macmodel> response);
}

