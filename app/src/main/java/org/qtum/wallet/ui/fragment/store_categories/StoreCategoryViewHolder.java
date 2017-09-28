package org.qtum.wallet.ui.fragment.store_categories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.QstoreContractType;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoreCategoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.cost)
    TextView cost;

    OnCategoryClickListener mListener;
    QstoreContractType mQstoreContractType;

    public StoreCategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(mQstoreContractType.getType());
            }
        });
    }

    public void bind(QstoreContractType item, OnCategoryClickListener listener) {
        mListener = listener;
        mQstoreContractType = item;
        icon.setImageResource(item.getIcon());
        title.setText(item.getType());
        cost.setText(String.valueOf(item.getCount()));
    }

    public interface OnCategoryClickListener{
        void onClick(String type);
    }
}
