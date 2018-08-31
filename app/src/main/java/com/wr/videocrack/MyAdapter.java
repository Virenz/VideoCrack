package com.wr.videocrack;

/**
 * Created by wb-weirui on 2016/1/26.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {
    interface OnRecyclerViewListener {
        void onItemClick(int position, TableInfo posinfo);
        boolean onItemLongClick(int position);
    }

    public OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = MyAdapter.class.getSimpleName();
    private List<TableInfo> list;


    public MyAdapter()
    {
        list = new ArrayList<>();
    }

    public MyAdapter(Context context, List<TableInfo> list) {
        this.list = list;
    }

    public void setListTableInfo(List<TableInfo> list)
    {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TableInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        TableInfoViewHolder holder = (TableInfoViewHolder) viewHolder;
        holder.position = i;
        TableInfo info = list.get(i);
        holder.nameTv.setText(info.getTitle());
        holder.ageTv.setText(info.getContent());
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class TableInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public View rootView;
        public TextView nameTv;
        public TextView ageTv;
        public int position;

        public TableInfoViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.recycler_view_test_item_person_name_tv);
            ageTv = (TextView) itemView.findViewById(R.id.recycler_view_test_item_person_age_tv);
            rootView = itemView.findViewById(R.id.recycler_view_test_item_person_view);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position, list.get(position));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(null != onRecyclerViewListener){
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }

}
