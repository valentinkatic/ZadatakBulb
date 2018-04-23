package com.katic.zadatak.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.katic.zadatak.utils.Constants;
import com.katic.zadatak.R;
import com.katic.zadatak.models.Channel;
import com.katic.zadatak.models.Image;
import com.katic.zadatak.models.Show;
import com.katic.zadatak.tasks.TaskShow;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityShowDetails extends AppCompatActivity implements TaskShow.ShowsListener {

    private Channel channel;
    private Show show;

    private ImageView iv_show_img;
    private TextView tv_show_title, tv_show_info, tv_show_description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        initToolbar();

        iv_show_img = findViewById(R.id.iv_show_img);
        tv_show_title = findViewById(R.id.tv_show_title);
        tv_show_info = findViewById(R.id.tv_show_info);
        tv_show_description = findViewById(R.id.tv_show_description);

        int showId = getIntent().getIntExtra(Constants.SHOW_ID, -1);

        // if showId is -1, stop with execution of program
        if (showId == -1){
            onFailed();
            return;
        }

        // we send showId as only parameter for task execution
        TaskShow taskShow = new TaskShow(this);
        taskShow.execute(showId);
    }

    // update view with downloaded data
    private void updateView() {
        for (Image i : show.getImages()) {
            if (i.getImageType().equals("still") && i.getImageUrl().substring(i.getImageUrl().length() - 7, i.getImageUrl().length()).equals("_XL.jpg")) {
                Picasso.get()
                        .load(Constants.baseUrl + i.getImageUrl())
                        .into(iv_show_img);
                break;
            }
        }
        tv_show_title.setText(show.getTitle());
        tv_show_description.setText(show.getDescription());

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(show.getStartTime() * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.show_time_pattern), Locale.getDefault());
        tv_show_info.setText(String.format(Locale.getDefault(), "%s | %s", sdf.format(c.getTime()), channel.getTitle()));
    }

    // task interface
    @Override
    public void onShowGet(Channel mChannel, Show mShow) {
        channel = mChannel;
        show = mShow;
        updateView();
    }

    // task interface
    @Override
    public void onFailed() {
        tv_show_title.setText(getString(R.string.no_info));
    }

    // initialization of toolbar
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.title_live));
        ImageView iv_back_button = findViewById(R.id.iv_back_button);
        iv_back_button.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);

        iv_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
