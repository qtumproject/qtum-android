package org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.jsoup.nodes.Element;

import butterknife.ButterKnife;


public abstract class TagViewHolder extends RecyclerView.ViewHolder{

    public TagViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    abstract public void bindElement(Element element);

}
