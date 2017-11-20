package org.qtum.wallet.ui.fragment.contract_confirm_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContractConfirmFooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(org.qtum.wallet.R.id.miner_value)
    FontTextView minerValue;

    public ContractConfirmFooterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String address) {
        minerValue.setText(address);
    }
}
