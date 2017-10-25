package org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import org.jsoup.nodes.Element;
import org.qtum.wallet.R;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;


public class TagIframeViewHolder extends TagViewHolder{

    @BindView(R.id.tv_tag_p)
    FontTextView mTextView;

    public TagIframeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindElement(Element element) {
        //String s = URLDecoder.decode(element);
        mTextView.setText(Html.fromHtml(element.html()));
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
