package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mikhaellopez.hfrecyclerview.HFRecyclerView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;

import java.util.List;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class ContractConfirmAdapter extends HFRecyclerView<ContractMethodParameter> {

    String minerAddress;
    String free;

    public ContractConfirmAdapter(List<ContractMethodParameter> params, String minerAddress, String free){
        super(params,false, true);
        this.minerAddress = minerAddress;
        this.free = free;
    }

    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ContractConfirmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_confirm_list_item, parent, false));
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
            ((ContractConfirmFooterViewHolder)holder).bind(minerAddress, free);
        }
    }
}
