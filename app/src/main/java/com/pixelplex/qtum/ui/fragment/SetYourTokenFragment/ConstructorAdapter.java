package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;

import java.util.List;


public class ConstructorAdapter extends RecyclerView.Adapter<InputViewHolder> {

    List<ContractMethodParameter> params;

    public List<ContractMethodParameter> getParams(){
        return params;
    }

    public ConstructorAdapter(List<ContractMethodParameter> params) {
        this.params = params;
    }

    @Override
    public InputViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InputViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_constructor_input,parent, false));
    }

    @Override
    public void onBindViewHolder(InputViewHolder holder, int position) {
        holder.bind(params.get(position),position == getItemCount()-1);
    }

    @Override
    public int getItemCount() {
        return params.size();
    }
}
