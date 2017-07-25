package com.pixelplex.qtum.ui.fragment.WatchContractFragment;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.utils.FontButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TemplateHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.bt_template)
    FontButton mButton;

    ContractTemplate mContractTemplate;

    public TemplateHolder(View itemView, final OnTemplateClickListener listener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTemplateClick(mContractTemplate);
            }
        });
    }

    public void onBind(ContractTemplate contractTemplate){
        mButton.setText(contractTemplate.getName());
        mContractTemplate = contractTemplate;
    }

}
