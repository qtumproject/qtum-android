package org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Element;
import org.qtum.wallet.R;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;


public class TagFigureViewHolder extends TagViewHolder{

    @BindView(R.id.tv_figcaption)
    FontTextView mTextViewFigcaption;
    @BindView(R.id.iv_image)
    ImageView mImageView;

    public TagFigureViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindElement(Element element) {
        mTextViewFigcaption.setText(element.select("figcaption").text());
        Picasso.with(mImageView.getContext())
                .load(element.select("img").attr("src"))
                .into(mImageView);
    }
}
