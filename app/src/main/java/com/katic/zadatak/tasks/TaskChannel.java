package com.katic.zadatak.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.katic.zadatak.utils.Constants;
import com.katic.zadatak.models.Channel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TaskChannel extends AsyncTask<Void, Void, String>{

    private ChannelsListener channelsListener;

    public TaskChannel(ChannelsListener channelsListener) {
        this.channelsListener = channelsListener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Constants.baseUrl + Constants.channelsUrl)
                    .build();

            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // if result is null, call onFailed() method
        if (result == null){
            channelsListener.onFailed();
            return;
        }

        // try to return list of channels from result
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            Gson gson = new Gson();
            List<Channel> list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Channel>>(){}.getType());
            channelsListener.onChannelsGet(list);
        } catch (Exception e){
            // if exception occurred, call onFailed() method
            channelsListener.onFailed();
        }

    }

    public interface ChannelsListener {
        void onChannelsGet(List<Channel> channelList);
        void onFailed();
    }
}
