package org.qtum.wallet.ui.fragment.watch_contract_fragment;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.model.ContractTemplate;

import java.util.List;

public class TemplatesAdapter extends RecyclerView.Adapter<TemplateHolder> {

    List<ContractTemplate> mContractTemplates;
    OnTemplateClickListener mListener;
    int resId;
    int selectionColor;

    public TemplatesAdapter(List<ContractTemplate> contractTemplates, OnTemplateClickListener listener, int resId, int selectionColor) {
        mContractTemplates = contractTemplates;
        mListener = listener;
        this.resId = resId;
        this.selectionColor = selectionColor;
    }

    @Override
    public TemplateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(resId, parent, false);
        return new TemplateHolder(view, mListener, selectionColor);
    }

    @Override
    public void onBindViewHolder(TemplateHolder holder, int position) {
        holder.onBind(mContractTemplates.get(position));
    }

    public void setSelection(int position) {
        for (int i = 0; i < mContractTemplates.size(); i ++){
            mContractTemplates.get(i).setSelectedABI(i == position);
        }
        notifyDataSetChanged();
    }

    public void setSelection(String templateName) {
        for (ContractTemplate item : mContractTemplates){
            item.setSelectedABI(templateName.equals(item.getName()));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mContractTemplates.size();
    }
}
