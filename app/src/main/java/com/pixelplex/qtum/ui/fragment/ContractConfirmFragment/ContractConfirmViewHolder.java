package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class ContractConfirmViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)
    FontTextView name;

    @BindView(R.id.value)
    FontTextView value;

    public ContractConfirmViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ContractMethodParameter parameter) {
        name.setText(parameter.displayName);
        value.setText(parameter.value);
    }
}
