package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class ContractConfirmFooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.miner_value)
    FontTextView minerValue;

    @BindView(R.id.free_value)
    FontTextView freeValue;

    public ContractConfirmFooterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String address, String free) {
        minerValue.setText(address);
        freeValue.setText(String.format("%s QTUM",free));
    }
}
