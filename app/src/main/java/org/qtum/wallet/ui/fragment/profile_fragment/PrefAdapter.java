package org.qtum.wallet.ui.fragment.profile_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class PrefAdapter extends RecyclerView.Adapter<PrefViewHolder> {

    protected List<SettingObject> settings;

    protected OnSettingClickListener listener;

    protected int resId;

    @Override
    public int getItemViewType(int position) {
        if (position < settings.size() - 1) {
            if (settings.get(position).getSectionNumber() == settings.get(position + 1).getSectionNumber()) {
                return 0; //divider
            } else {
                return 1; //section
            }
        } else {
            return 1; //section
        }
    }

    public PrefAdapter(final List<SettingObject> settings, OnSettingClickListener listener, int resId) {
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
