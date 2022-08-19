package com.tyky.debugger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyky.debugger.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvModuleAdapter extends RecyclerView.Adapter<RvModuleAdapter.ViewHolder> {

    String[] array = null;

    public RvModuleAdapter(String[] data) {
        this.array = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_module, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = array[position];

        holder.setModuleName(name);
    }

    @Override
    public int getItemCount() {
        return array.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModuleName = itemView.findViewById(R.id.tvModule);
        }

        public void setModuleName(String name) {
            tvModuleName.setText(name + "模块");
        }
    }

}
