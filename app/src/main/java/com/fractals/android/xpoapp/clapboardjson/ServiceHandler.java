package com.fractals.android.xpoapp.clapboardjson;


import android.content.Entity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ServiceHandler {

    static String responce = null;

    public String makeservicecall(String url) {
        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            HttpGet httpGet = new HttpGet(url);
            httpResponse = defaultHttpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            responce= EntityUtils.toString(httpEntity);


        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responce;
    }
}