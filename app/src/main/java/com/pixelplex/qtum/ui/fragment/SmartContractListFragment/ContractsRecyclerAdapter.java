package com.pixelplex.qtum.ui.fragment.SmartContractListFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.SmartContractsManager.ContractTemplate;

import java.util.List;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class ContractsRecyclerAdapter extends RecyclerView.Adapter<ContractViewHolder> {

    List<ContractTemplate> list;

    ContractSelectListener listener;

    public ContractsRecyclerAdapter(List<ContractTemplate> list, ContractSelectListener listener){
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ContractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContractViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_contract_list_item,parent,false),listener);
    }

    @Override
    public void onBindViewHolder(ContractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
