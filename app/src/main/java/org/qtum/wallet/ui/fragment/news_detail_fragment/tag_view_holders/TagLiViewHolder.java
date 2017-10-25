package org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import org.jsoup.nodes.Element;
import org.qtum.wallet.R;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;


public class TagLiViewHolder extends TagViewHolder{

    @BindView(R.id.tv_text)
    FontTextView mTextView;

    public TagLiViewHolder(View itemView) {
        super(itemView);
    }

    public void bindElement(Element element){
        mTextView.setText(Html.fromHtml(element.html()));
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
