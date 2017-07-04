package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mikhaellopez.hfrecyclerview.HFRecyclerView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;

import java.util.List;


class ContractConfirmAdapter extends HFRecyclerView<ContractMethodParameter> {

    private String mineAddress;
    private String fee;

    private OnValueClick clickListener;

    ContractConfirmAdapter(List<ContractMethodParameter> params, String mineAddress, String fee, OnValueClick clickListener){
        super(params,false, true);
        this.mineAddress = mineAddress;
        this.fee = fee;
        this.clickListener = clickListener;
    }

    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ContractConfirmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_confirm_list_item, parent, false), clickListener);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return null; //not use
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new ContractConfirmFooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_confirm_list_footer, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ContractConfirmViewHolder) {
            ((ContractConfirmViewHolder) holder).bind(getItem(position));
        } else if(holder instanceof ContractConfirmFooterViewHolder) {
            ((ContractConfirmFooterViewHolder)holder).bind(mineAddress, fee);
        }
    }
}
