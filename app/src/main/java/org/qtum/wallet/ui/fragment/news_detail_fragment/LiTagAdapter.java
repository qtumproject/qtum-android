package org.qtum.wallet.ui.fragment.news_detail_fragment;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jsoup.select.Elements;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagLiViewHolder;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagViewHolder;

public class LiTagAdapter extends RecyclerView.Adapter<TagViewHolder> {

    Elements mElements;
    @LayoutRes
    int mResId;

    public LiTagAdapter(Elements elements, @LayoutRes int resId) {
        mElements = elements;
        mResId = resId;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagLiViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false));
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
