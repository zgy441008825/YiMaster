package com.zou.focus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zougy on 03.25.025
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> dataList;
    private List<Integer> answerList = new ArrayList<>();
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;

    public MyRecyclerViewAdapter(List<String> dataList, Context context) {
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    public MyRecyclerViewAdapter setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.layout_item, parent, false));
    }

    public void clear() {
        answerList.clear();
    }

    public boolean isOver() {
        return answerList.size() == dataList.size();
    }

    public boolean answerOne(int position) {
        int value = Integer.valueOf(dataList.get(position));
        if ((value - 1) == answerList.size()) {
            answerList.add(position);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rootView.setTag(R.id.textView, position);
        holder.textView.setText(dataList.get(position));
        holder.rootView.setSelected(answerList.contains(position));
        holder.rootView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
