package org.qtum.wallet.ui.fragment.news_detail_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.FontTextView;

import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public abstract class NewsDetailFragment extends BaseFragment implements NewsDetailView{

    private NewsDetailPresenter mNewsDetailPresenter;
    private static final String NEWS_POSITION = "news_position";

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    public static BaseFragment newInstance(Context context, int newsPosition) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context,NewsDetailFragment.class) ;
        args.putInt(NEWS_POSITION, newsPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void createPresenter() {
        mNewsDetailPresenter = new NewsDetailPresenterImpl(this, new NewsDetailInteractorImpl(getContext()));
    }

    @Override
    protected NewsDetailPresenter getPresenter() {
        return mNewsDetailPresenter;
    }


    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public int getNewsPosition() {
        return getArguments().getInt(NEWS_POSITION);
    }

    @Override
    public void setupElements(Elements elements) {
        ElementsAdapter elementsAdapter = new ElementsAdapter(elements);
        mRecyclerView.setAdapter(elementsAdapter);
    }

    class ElementsAdapter extends RecyclerView.Adapter<TagViewHolder>{

        private Elements mElements;

        private final String TAG_P = "p";
        private final int TYPE_TAG_P = 1;
        private final String TAG_FIGURE = "figure";
        private final int TYPE_TAG_FIGURE = 2;
        private final String TAG_HR = "hr";
        private final int TYPE_TAG_HR = 3;
        private final String TAG_UL = "ul";
        private final int TYPE_TAG_UL = 4;
        private final String TAG_H1 = "h1";
        private final String TAG_H2 = "h2";
        private final String TAG_H3 = "h3";
        private final String TAG_H4 = "h4";
        private final int TYPE_TAG_H = 5;
        private final String TAG_IFRAME = "iframe";
        private final int TYPE_TAG_IFRAME = 6;

        ElementsAdapter(Elements elements){
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
        public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case TYPE_TAG_P:
                    return new TagPViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_p_element, parent, false));
                case TYPE_TAG_FIGURE:
                    return new TagFigureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_figure_element, parent, false));
                case TYPE_TAG_HR:
                    return new TagHrViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_hr_element, parent, false));
                case TYPE_TAG_UL:
                    return new TagUlViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_ul_element, parent, false));
                case TYPE_TAG_H:
                    return new TagPViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_h_element, parent, false));
                case TYPE_TAG_IFRAME:
                    return new TagIframeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_iframe_element, parent, false));
            }
            return null;
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

    abstract class TagViewHolder extends RecyclerView.ViewHolder{

        public TagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        abstract public void bindElement(Element element);

    }

    class TagHrViewHolder extends TagViewHolder{

        public TagHrViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindElement(Element element) {

        }
    }

    class TagPViewHolder extends TagViewHolder{

        @BindView(R.id.tv_tag_p)
        FontTextView mTextView;

        public TagPViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindElement(Element element) {
            mTextView.setText(Html.fromHtml(element.html()));
            mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    class TagFigureViewHolder extends TagViewHolder{

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
            Picasso.with(getContext())
                    .load(element.select("img").attr("src"))
                    .into(mImageView);
        }
    }

    class TagUlViewHolder extends TagViewHolder{

        @BindView(R.id.recycler_view)
        RecyclerView mRecyclerView;

        public TagUlViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindElement(Element element) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            LiTagAdapter liTagAdapter = new LiTagAdapter(element.children());
            mRecyclerView.setAdapter(liTagAdapter);
        }

    }

    class LiTagAdapter extends RecyclerView.Adapter<TagViewHolder>{

        Elements mElements;

        LiTagAdapter(Elements elements){
            mElements = elements;
        }

        @Override
        public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LiTagViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_li_element, parent, false));
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

    class LiTagViewHolder extends TagViewHolder{

        @BindView(R.id.tv_text)
        FontTextView mTextView;

        public LiTagViewHolder(View itemView) {
            super(itemView);
        }

        public void bindElement(Element element){
            mTextView.setText(Html.fromHtml(element.html()));
            mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    class TagIframeViewHolder extends TagViewHolder{

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
}
