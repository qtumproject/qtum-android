package com.pixelplex.qtum.ui.fragment.TemplatesFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;

import java.util.List;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class TemplatesRecyclerAdapter extends RecyclerView.Adapter<TemplateViewHolder> {

    List<ContractTemplate> list;

    TemplateSelectListener listener;

    public TemplatesRecyclerAdapter(List<ContractTemplate> list, TemplateSelectListener listener){
        this.list = list;
        this.listener = listener;
    }

    @Override
    public TemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TemplateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_contract_list_item,parent,false),listener);
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
