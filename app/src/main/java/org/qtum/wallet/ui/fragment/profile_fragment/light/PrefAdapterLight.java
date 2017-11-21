package org.qtum.wallet.ui.fragment.profile_fragment.light;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.qtum.wallet.ui.fragment.profile_fragment.OnSettingClickListener;
import org.qtum.wallet.ui.fragment.profile_fragment.PrefAdapter;
import org.qtum.wallet.ui.fragment.profile_fragment.PrefViewHolder;
import org.qtum.wallet.ui.fragment.profile_fragment.SettingObject;

import java.util.List;

public class PrefAdapterLight extends PrefAdapter {
    public PrefAdapterLight(List<SettingObject> settings, OnSettingClickListener listener, int resId) {
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
