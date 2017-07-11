package com.pixelplex.qtum.ui.fragment.ProfileFragment.Dark;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.ui.fragment.ProfileFragment.OnSettingClickListener;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.PrefAdapter;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.PrefViewHolder;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.SettingObject;

import java.util.List;

/**
 * Created by kirillvolkov on 07.07.17.
 */

public class PrefAdapterDark extends PrefAdapter {
    public PrefAdapterDark(List<SettingObject> settings, OnSettingClickListener listener, int resId) {
        super(settings, listener, resId);
    }

    @Override
    public PrefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrefViewHolder(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(PrefViewHolder holder, int position) {
        holder.bind(settings.get(position));
    }
}
