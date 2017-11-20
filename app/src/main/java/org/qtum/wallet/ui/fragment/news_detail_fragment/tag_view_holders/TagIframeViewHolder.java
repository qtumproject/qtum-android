package org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders;

import android.view.View;

import org.jsoup.nodes.Element;
import org.qtum.wallet.R;
import org.qtum.wallet.utils.FontTextView;

import java.net.URLDecoder;

import butterknife.BindView;

public class TagIframeViewHolder extends TagViewHolder {

    @BindView(R.id.tv_tag_p)
    FontTextView mTextView;

    public TagIframeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindElement(Element element) {
        String attr = element.attr("src");
        attr = attr.substring(attr.indexOf("url=") + 4);
        attr = attr.substring(0, attr.indexOf("&image="));
        String url = URLDecoder.decode(attr);
        mTextView.setText(url);
    }
}
