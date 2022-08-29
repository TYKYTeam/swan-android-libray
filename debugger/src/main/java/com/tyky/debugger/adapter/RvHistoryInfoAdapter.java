package com.tyky.debugger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyky.debugger.R;
import com.tyky.debugger.SettingActivity;
import com.tyky.debugger.bean.HistoryUrlInfo;
import com.tyky.webviewBase.event.UrlLoadEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvHistoryInfoAdapter extends RecyclerView.Adapter<RvHistoryInfoAdapter.ViewHolder> {

    List<HistoryUrlInfo> historyUrlInfoList = new ArrayList<>();

    private View nodataView;
    private SettingActivity settingActivity;
    public RvHistoryInfoAdapter( List<HistoryUrlInfo> infos,View view,SettingActivity settingActivity) {
        historyUrlInfoList.addAll(infos);
        nodataView = view;
        this.settingActivity = settingActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_history, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        showOrHideNoDataView();

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            HistoryUrlInfo info = historyUrlInfoList.get(position);
            String url = info.getUrl();
            viewHolder.tvUrl.setText(url);
            viewHolder.tvUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new UrlLoadEvent(url));
                    settingActivity.finish();
                }
            });

            viewHolder.ivArrow.setOnClickListener(v -> {
                info.delete();
                int index = historyUrlInfoList.indexOf(info);
                historyUrlInfoList.remove(info);
                notifyItemRemoved(index);
                //删除完数据，显示对应的view
                showOrHideNoDataView();
            });
        }
    }

    private void showOrHideNoDataView() {
        if (getItemCount() > 0) {
            //有数据，不显示暂无数据
            nodataView.setVisibility(View.GONE);
        }else {
            if (nodataView.getVisibility() == View.GONE) {
                nodataView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return historyUrlInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUrl;
        public ImageView ivArrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            ivArrow = itemView.findViewById(R.id.ivArrow);
        }
    }

}
