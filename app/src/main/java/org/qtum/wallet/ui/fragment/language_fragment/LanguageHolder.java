package org.qtum.wallet.ui.fragment.language_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.qtum.wallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class LanguageHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_single_string)
    protected
    TextView mTextViewLanguage;
    @BindView(R.id.iv_check_indicator)
    protected
    ImageView mImageViewCheckIndicator;
    @BindView(R.id.ll_single_item)
    protected
    LinearLayout mLinearLayoutAddress;

    public LanguageHolder(View itemView, final OnLanguageIntemClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLanguageIntemClick(getAdapterPosition());
            }
        });
        ButterKnife.bind(this, itemView);
    }
}