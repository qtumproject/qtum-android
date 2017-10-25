package org.qtum.wallet.ui.fragment.news_detail_fragment.dark;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jsoup.select.Elements;
import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.news_detail_fragment.ElementsAdapter;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagFigureViewHolder;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagHrViewHolder;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagIframeViewHolder;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagPViewHolder;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagUlViewHolder;
import org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders.TagViewHolder;

public class ElementsAdapterDark extends ElementsAdapter {

    ElementsAdapterDark(Elements elements) {
        super(elements);
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TAG_P:
                return new TagPViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_p_element, parent, false));
            case TYPE_TAG_FIGURE:
                return new TagFigureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_figure_element, parent, false));
            case TYPE_TAG_HR:
                return new TagHrViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_hr_element, parent, false));
            case TYPE_TAG_UL:
                return new TagUlViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_ul_element, parent, false),R.layout.item_tag_li_element);
            case TYPE_TAG_H:
                return new TagPViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_h_element, parent, false));
            case TYPE_TAG_IFRAME:
                return new TagIframeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_iframe_element, parent, false));
        }
        return null;
    }

}
