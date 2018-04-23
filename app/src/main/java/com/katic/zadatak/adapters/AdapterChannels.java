package com.katic.zadatak.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.katic.zadatak.utils.Constants;
import com.katic.zadatak.R;
import com.katic.zadatak.models.Channel;
import com.katic.zadatak.models.Show;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChannels extends RecyclerView.Adapter<AdapterChannels.ViewHolder> {

    private Context context;
    private List<Channel> channelList;
    private ChannelListener channelListener;

    public AdapterChannels(Context context, List<Channel> channelList) {
        this.context = context;
        this.channelList = channelList;
    }

    public void setChannelListener(ChannelListener channelListener) {
        this.channelListener = channelListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_channel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(channelList.get(position));
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Calendar c = Calendar.getInstance();
        private SimpleDateFormat sdf;
        private LinearLayout parent_view;
        private ImageView iv_channel;
        private TextView tv_current_show, tv_next_show;

        private int showId = -1;

        public ViewHolder(View itemView) {
            super(itemView);
            parent_view = itemView.findViewById(R.id.parent_view);
            iv_channel = itemView.findViewById(R.id.iv_channel);
            tv_current_show = itemView.findViewById(R.id.tv_current_show);
            tv_next_show = itemView.findViewById(R.id.tv_next_show);
            sdf = new SimpleDateFormat(context.getString(R.string.channel_time_pattern), Locale.getDefault());
        }

        private void bind(final Channel channel) {
            Picasso.get()
                    .load(Constants.baseUrl + channel.getLogo())
                    .into(iv_channel);

            parent_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    channelListener.onChannelClicked(showId);
                }
            });

            // if channel has no shows, stop with execution of program
            if (channel.getShows() == null){
                tv_current_show.setText(context.getString(R.string.no_info));
                tv_next_show.setText(context.getString(R.string.no_info));
                return;
            }

            // if show on position 0 is not set or has id -1, show no info text
            if (channel.getShows().get(0) == null || channel.getShows().get(0).getShowId() == -1){
                tv_current_show.setText(context.getString(R.string.no_info));
                showId = -1;
            } else {
                tv_current_show.setText(channel.getShows().get(0).getTitle());
                showId = channel.getShows().get(0).getShowId();
            }

            // if show on position 1 is not set or has id -1, show no info text
            if (channel.getShows().get(1) == null || channel.getShows().get(1).getShowId() == -1){
                tv_next_show.setText(context.getString(R.string.no_info));
            } else {
                Show nextShow = channel.getShows().get(1);
                c.setTimeInMillis(nextShow.getStartTime() * 1000);
                tv_next_show.setText(String.format(Locale.getDefault(), "%s | %s", sdf.format(c.getTime()), nextShow.getTitle()));
            }
        }
    }

    public interface ChannelListener {
        void onChannelClicked(int ID);
    }

}
