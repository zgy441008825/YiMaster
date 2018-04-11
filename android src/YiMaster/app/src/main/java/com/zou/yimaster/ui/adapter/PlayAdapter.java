package com.zou.yimaster.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zou.yimaster.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zougaoyuan on 2018/3/28
 *
 * @author zougaoyuan
 */
public class PlayAdapter extends RecyclerView.Adapter<PlayAdapter.ViewHolder> {
    private static final String TAG = "PlayAdapter";
    private LayoutInflater inflater;
    private int currentShowPosition = -1;
    private boolean enable = false;
    private int itemSize;
    private List<Boolean> selectState;
    private IClickViewTouchCallback callback;

    public PlayAdapter(Context context, int itemSize) {
        this.itemSize = itemSize;
        inflater = LayoutInflater.from(context);
        initSelectList();
    }

    private void initSelectList() {
        selectState = new ArrayList<>();
        for (int i = 0; i < itemSize; i++)
            selectState.add(false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.layout_play_item, parent, false));
    }

    public void setCallback(IClickViewTouchCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.clickView.setValue(position);
//        holder.clickView.setEnabled(enable);
//        holder.clickView.setTag(R.layout.layout_play_item, position);
//        holder.clickView.setTag(R.id.ClickView, holder.clickView);
//        holder.clickView.show(selectState.get(position));
    }

    public void setCurrentShowPosition(int currentShowPosition) {
//        this.currentShowPosition = currentShowPosition;
//        notifyDataSetChanged();
        if (currentShowPosition < 0 || currentShowPosition >= selectState.size()) return;
        if (this.currentShowPosition < 0 || this.currentShowPosition >= selectState.size()) return;
        selectState.set(this.currentShowPosition, selectState.get(this.currentShowPosition));
        notifyItemChanged(this.currentShowPosition);
        selectState.set(currentShowPosition, selectState.get(currentShowPosition));
        notifyItemChanged(currentShowPosition);
        this.currentShowPosition = currentShowPosition;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
        notifyDataSetChanged();
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        currentShowPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemSize;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View clickView;

        View rootView;

        ViewHolder(View itemView) {
            super(itemView);
//            clickView = itemView.findViewById(R.id.ClickView);
            rootView = itemView;
//            clickView.setCallback(callback);
        }
    }

    public interface IClickViewTouchCallback {
        boolean onActionDown(int index);

        void onActionUp(int index);
    }

}
