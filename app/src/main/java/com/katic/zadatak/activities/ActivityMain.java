package com.katic.zadatak.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.katic.zadatak.utils.Constants;
import com.katic.zadatak.R;
import com.katic.zadatak.adapters.AdapterChannels;
import com.katic.zadatak.models.Channel;
import com.katic.zadatak.tasks.TaskChannel;

import java.util.List;

public class ActivityMain extends AppCompatActivity implements TaskChannel.ChannelsListener, AdapterChannels.ChannelListener{

    private List<Channel> channels;
    private RecyclerView rv;
    private LinearLayout ll_no_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        rv = findViewById(R.id.rv);
        ll_no_item = findViewById(R.id.ll_no_item);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        TaskChannel taskChannel = new TaskChannel(this);
        taskChannel.execute();
    }

    // initializing channels adapter
    private void initAdapter(){
        ll_no_item.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);

        AdapterChannels adapterChannels = new AdapterChannels(this, channels);
        rv.setAdapter(adapterChannels);
        adapterChannels.setChannelListener(this);
    }

    // adapter interface
    @Override
    public void onChannelClicked(int showId) {
        if (showId == -1){
            Toast.makeText(this, getString(R.string.no_info), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ActivityShowDetails.class);
        intent.putExtra(Constants.SHOW_ID, showId);
        startActivity(intent);
    }

    // task interface
    @Override
    public void onChannelsGet(List<Channel> channelList) {
        channels = channelList;
        initAdapter();
    }

    // task interface
    @Override
    public void onFailed() {
        ll_no_item.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
    }

    // initialization of toolbar
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
