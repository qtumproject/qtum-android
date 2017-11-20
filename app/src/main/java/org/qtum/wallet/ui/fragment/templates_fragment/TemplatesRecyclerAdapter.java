package org.qtum.wallet.ui.fragment.templates_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.qtum.wallet.model.ContractTemplate;

import java.util.List;

public class TemplatesRecyclerAdapter extends RecyclerView.Adapter<TemplateViewHolder> {

    private List<ContractTemplate> list;
    private TemplateSelectListener listener;
    private int mResId;

    public TemplatesRecyclerAdapter(List<ContractTemplate> list, TemplateSelectListener listener, int resId) {
        this.list = list;
        this.listener = listener;
        this.mResId = resId;
    }

    @Override
    public TemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TemplateViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(TemplateViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
