package com.tyky.debugger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyky.debugger.R;
import com.tyky.debugger.bean.HistoryUrlInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvHistoryInfoAdapter extends RecyclerView.Adapter<RvHistoryInfoAdapter.ViewHolder> {

    List<HistoryUrlInfo> historyUrlInfoList = new ArrayList<>();

    public RvHistoryInfoAdapter( List<HistoryUrlInfo> infos) {
        historyUrlInfoList.addAll(infos);
    }


    public void setTextOnClickListener(View.OnClickListener listener) {

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
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            HistoryUrlInfo info = historyUrlInfoList.get(position);
            viewHolder.tvUrl.setText(info.getUrl());
            viewHolder.ivArrow.setOnClickListener(v -> {
                info.delete();
                historyUrlInfoList.remove(position);
                notifyItemRemoved(position);
            });
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
