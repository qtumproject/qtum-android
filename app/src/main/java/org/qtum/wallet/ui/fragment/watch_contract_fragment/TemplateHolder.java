package org.qtum.wallet.ui.fragment.watch_contract_fragment;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.utils.FontButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TemplateHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.bt_template)
    FontButton mButton;

    ContractTemplate mContractTemplate;
    int selectionColor;

    Drawable defaultDrawable;

    Handler handler;

    public TemplateHolder(View itemView, final OnTemplateClickListener listener, final int selectionColor) {
        super(itemView);
        handler = new Handler();
        this.selectionColor = selectionColor;
        ButterKnife.bind(this, itemView);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.updateSelection(getAdapterPosition());
                listener.onTemplateClick(mContractTemplate);
            }
        });
        defaultDrawable = mButton.getBackground();
    }

    public void onBind(ContractTemplate contractTemplate) {
        mButton.setText(contractTemplate.getName());
        mContractTemplate = contractTemplate;
        if (contractTemplate.isSelectedABI()) {
            mButton.setBackgroundResource(selectionColor);
        } else {
            mButton.setBackground(defaultDrawable);
        }
    }
}
