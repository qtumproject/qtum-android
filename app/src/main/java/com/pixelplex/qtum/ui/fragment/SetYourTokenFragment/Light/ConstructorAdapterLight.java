package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.Light;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.ConstructorAdapter;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.InputViewHolder;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.OnValidateParamsListener;

import java.util.List;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class ConstructorAdapterLight extends ConstructorAdapter {

    public ConstructorAdapterLight(List<ContractMethodParameter> params, OnValidateParamsListener listener) {
        super(params, listener);
    }

    @Override
    public InputViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InputViewHolderLight(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_constructor_input_light, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(InputViewHolder holder, int position) {
        holder.bind(params.get(position),position == getItemCount()-1);
    }
}
