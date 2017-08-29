package com.pixelplex.qtum.ui.fragment.set_your_token_fragment;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.pixelplex.qtum.model.contract.ContractMethodParameter;

import java.util.List;


public abstract class ConstructorAdapter extends RecyclerView.Adapter<InputViewHolder> {

    protected List<ContractMethodParameter> params;
    protected OnValidateParamsListener listener;

    public List<ContractMethodParameter> getParams(){
        return params;
    }

    public ConstructorAdapter(List<ContractMethodParameter> params, OnValidateParamsListener listener) {
        this.params = params;
        this.listener = listener;
    }

    public boolean validateMethods(){
        for (ContractMethodParameter p : params){
            if(TextUtils.isEmpty(p.getValue())){
                return false;
            }
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return params.size();
    }
}
