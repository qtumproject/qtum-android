package com.pixelplex.qtum.ui.fragment.ProfileFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.utils.ThemeUtils;

import java.util.List;


public abstract class PrefAdapter extends RecyclerView.Adapter<PrefViewHolder> {

    protected List<SettingObject> settings;

    protected OnSettingClickListener listener;

    protected int resId;

    public PrefAdapter(final List<SettingObject> settings, OnSettingClickListener listener, int resId){
        this.settings = settings;
        this.listener = listener;
        this.resId = resId;
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }
}
