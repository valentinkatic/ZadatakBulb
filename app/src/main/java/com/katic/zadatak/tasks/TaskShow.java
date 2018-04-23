package com.katic.zadatak.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.katic.zadatak.utils.Constants;
import com.katic.zadatak.models.Channel;
import com.katic.zadatak.models.Show;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TaskShow extends AsyncTask<Integer, Void, String> {

    private ShowsListener showsListener;

    public TaskShow(ShowsListener showsListener) {
        this.showsListener = showsListener;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Constants.baseUrl + "show-" + ints[0] + ".json")
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
            showsListener.onFailed();
            return;
        }

        // try to return channel and show objects from result
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject channelObj = jsonObject.getJSONObject("channel");
            JSONObject showObj = jsonObject.getJSONObject("show");
            Gson gson = new Gson();
            Show show = gson.fromJson(showObj.toString(), Show.class);
            Channel channel = gson.fromJson(channelObj.toString(), Channel.class);
            showsListener.onShowGet(channel, show);
        } catch (Exception e){
            // if exception occurred, call onFailed() method
            showsListener.onFailed();
        }

    }

    public interface ShowsListener {
        void onShowGet(Channel mChannel, Show mShow);
        void onFailed();
    }
}
