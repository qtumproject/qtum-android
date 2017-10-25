package org.qtum.wallet.ui.fragment.news_detail_fragment;

import android.support.v7.widget.RecyclerView;

import org.jsoup.select.Elements;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagViewHolder;


public abstract class ElementsAdapter extends RecyclerView.Adapter<TagViewHolder>{

    private Elements mElements;

    private final String TAG_P = "p";
    protected final int TYPE_TAG_P = 1;
    private final String TAG_FIGURE = "figure";
    protected final int TYPE_TAG_FIGURE = 2;
    private final String TAG_HR = "hr";
    protected final int TYPE_TAG_HR = 3;
    private final String TAG_UL = "ul";
    protected final int TYPE_TAG_UL = 4;
    private final String TAG_H1 = "h1";
    private final String TAG_H2 = "h2";
    private final String TAG_H3 = "h3";
    private final String TAG_H4 = "h4";
    protected final int TYPE_TAG_H = 5;
    private final String TAG_IFRAME = "iframe";
    protected final int TYPE_TAG_IFRAME = 6;

    public ElementsAdapter(Elements elements){
        mElements = elements;
    }

    @Override
    public int getItemViewType(int position) {
        if(mElements.get(position).tag().getName().equals(TAG_P)){
            return TYPE_TAG_P;
        } else if(mElements.get(position).tag().getName().equals(TAG_FIGURE)){
            return TYPE_TAG_FIGURE;
        } else if(mElements.get(position).tag().getName().equals(TAG_HR)){
            return TYPE_TAG_HR;
        } else if(mElements.get(position).tag().getName().equals(TAG_UL)){
            return TYPE_TAG_UL;
        } else if (mElements.get(position).tag().getName().equals(TAG_H1) || mElements.get(position).tag().getName().equals(TAG_H2) ||
                mElements.get(position).tag().getName().equals(TAG_H3) || mElements.get(position).tag().getName().equals(TAG_H4)){
            return TYPE_TAG_H;
        } else if (mElements.get(position).tag().getName().equals(TAG_IFRAME)){
            return TYPE_TAG_IFRAME;
        }
        return TYPE_TAG_P;
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.bindElement(mElements.get(position));
    }

    @Override
    public int getItemCount() {
        return mElements.size();
    }
}
