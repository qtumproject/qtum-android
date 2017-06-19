package com.pixelplex.qtum.ui.fragment.TemplatesFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.model.ContractTemplate;
import com.pixelplex.qtum.utils.DateCalculator;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class TemplateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    FontTextView title;

    @BindView(R.id.date)
    FontTextView date;

    @BindView(R.id.contract_type)
    FontTextView contractType;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    public TemplateViewHolder(View itemView, final TemplateSelectListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectContract(title.getText().toString());
            }
        });
    }

    public void bind(ContractTemplate contract) {
        title.setText(contract.getName());

        date.setText(DateCalculator.getDate(contract.getDate()));

        contractType.setText(contract.getContractType().toUpperCase());
    }
}
