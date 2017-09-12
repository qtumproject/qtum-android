package org.qtum.wallet.ui.fragment.store_categories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.qstore.TestTokenObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoreCategoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.cost)
    TextView cost;

    public StoreCategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(TestTokenObject item) {
        icon.setImageResource(item.icon);
        title.setText(item.name);
        cost.setText(String.valueOf(item.cost));
    }
}
