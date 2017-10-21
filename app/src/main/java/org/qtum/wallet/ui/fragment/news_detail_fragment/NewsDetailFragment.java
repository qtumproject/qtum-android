package org.qtum.wallet.ui.fragment.news_detail_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.ButterKnife;


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

        ElementsAdapter(Elements elements){
            mElements = elements;
        }

        @Override
        public int getItemViewType(int position) {
            //if(mElements.get(position).tag().getName().equals(TAG_P)){
                return TYPE_TAG_P;
            //}
            //return super.getItemViewType(position);
        }

        @Override
        public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case TYPE_TAG_P:
                    return new TagPViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_p_element, parent, false));
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

    class TagPViewHolder extends TagViewHolder{

        @BindView(R.id.tv_tag_p)
        TextView mTextView;

        public TagPViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindElement(Element element) {
            mTextView.setText(element.text());
        }
    }

}
