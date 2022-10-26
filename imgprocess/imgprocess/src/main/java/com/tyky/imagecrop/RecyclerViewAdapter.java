package com.tyky.imagecrop;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<String> datalist=new ArrayList<>();

    private View.OnClickListener listener=null;

    public RecyclerViewAdapter(View.OnClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(),R.layout.recycleritem,null);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        TextView textView=view.findViewById(R.id.optionitem);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ((TextView)(holder.itemView.findViewById(R.id.optionitem))).setText(datalist.get(position));



    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public void setDatalist(List<String> datalist){
        this.datalist=datalist;
        notifyDataSetChanged();
    }

}
