package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixelplex.qtum.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoreTokenViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.token_name)
    TextView tokenName;

    @BindView(R.id.token_type)
    TextView tokenType;

    @BindView(R.id.token_cost)
    TextView tokenCost;


    public StoreTokenViewHolder(final View itemView, final StoreItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setClickable(true);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(item);
            }
        });
    }

    TestTokenObject item;

    public void bind(TestTokenObject item){
        this.item = item;
        icon.setImageResource(item.icon);
        tokenName.setText(item.name);
        tokenType.setText(item.type);
        tokenCost.setText(String.valueOf(item.cost));
    }
}
