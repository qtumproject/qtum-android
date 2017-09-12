package org.qtum.wallet.ui.fragment.addresses_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 06.07.17.
 */


public class AddressHolder extends RecyclerView.ViewHolder {

    @BindView(org.qtum.wallet.R.id.tv_single_string)
    protected TextView mTextViewAddress;
    @BindView(org.qtum.wallet.R.id.iv_check_indicator)
    protected ImageView mImageViewCheckIndicator;
    @BindView(org.qtum.wallet.R.id.ll_single_item)
    protected LinearLayout mLinearLayoutAddress;

    protected int defaultTextColor, selectedTextColor;

    protected AddressHolder(View itemView, final OnAddressClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddressClick(getAdapterPosition());
            }
        });
        ButterKnife.bind(this, itemView);
    }
}
