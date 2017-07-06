package com.pixelplex.qtum.ui.fragment.ProfileFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.utils.ThemeUtils;

import java.util.List;


public class PrefAdapter extends RecyclerView.Adapter<PrefViewHolder> {

    List<SettingObject> settings;

    OnSettingClickListener listener;

    int resId;

    public PrefAdapter(final List<SettingObject> settings, OnSettingClickListener listener, int resId){
        this.settings = settings;
        this.listener = listener;
        this.resId = resId;

    }

    @Override
    public PrefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrefViewHolder(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(PrefViewHolder holder, int position) {
        holder.bind(settings.get(position));
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }
}
