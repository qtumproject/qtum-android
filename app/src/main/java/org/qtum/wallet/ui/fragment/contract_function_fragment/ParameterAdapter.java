package org.qtum.wallet.ui.fragment.contract_function_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.qtum.wallet.model.contract.ContractMethodParameter;

import java.util.List;


public class ParameterAdapter extends RecyclerView.Adapter<ParameterViewHolder> {

    List<ContractMethodParameter> params;
    int mResId;

    public List<ContractMethodParameter> getParams() {
        return params;
    }

    public ParameterAdapter(List<ContractMethodParameter> params, int resId) {
        this.params = params;
        mResId = resId;
    }

    @Override
    public ParameterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParameterViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(ParameterViewHolder holder, int position) {
        holder.bind(params.get(position), position == getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return params.size();
    }
}
