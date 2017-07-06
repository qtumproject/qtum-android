package com.pixelplex.qtum.ui.fragment.NewsFragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.News;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsFragment extends BaseFragment implements NewsFragmentView {

    NewsFragmentPresenterImpl mNewsFragmentPresenter;
    NewsAdapter mNewsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static NewsFragment newInstance() {

        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mNewsFragmentPresenter = new NewsFragmentPresenterImpl(this);
    }

    @Override
    protected NewsFragmentPresenterImpl getPresenter() {
        return mNewsFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void initializeViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                if (!mSwipeRefreshLayout.isRefreshing())
                    if (manager.findFirstCompletelyVisibleItemPosition() == 0)
                        mSwipeRefreshLayout.setEnabled(true);
                    else
                        mSwipeRefreshLayout.setEnabled(false);
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().onRefresh();
            }
        });
    }

    @Override
    public void updateNews(List<News> newsList) {
        mNewsAdapter = new NewsAdapter(newsList);
        mRecyclerView.setAdapter(mNewsAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setAdapterNull() {
        mNewsAdapter = null;
    }

    @Override
    public void startRefreshAnimation() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        ImageView mImageViewImage;
        @BindView(R.id.tv_title)
        TextView mTextViewTitle;
        @BindView(R.id.tv_date)
        TextView mTextViewDate;

        NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindNews(News news) {
            mTextViewTitle.setText(news.getTitle());
            mTextViewDate.setText(news.getDate());
            if (news.getImage() != null) {
                Picasso
                        .with(getActivity())
                        .load(news.getImage())
                        .error(R.drawable.ic_launcher)
                        .into(mImageViewImage);
            }
        }
    }

    class NewsHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_news_header)
        ImageView mImageViewImage;
        @BindView(R.id.tv_date_news_header)
        TextView mTextViewDate;
        @BindView(R.id.tv_title_news_header)
        TextView mTextViewTitle;
        @BindView(R.id.tv_short_text_news_header)
        TextView mTextViewShortText;

        NewsHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindNewsHeader(News news) {
            mTextViewTitle.setText(news.getTitle());
            mTextViewDate.setText(news.getDate());
            mTextViewShortText.setText(news.getShort());
            if (news.getImage() != null) {
                Picasso
                        .with(getActivity())
                        .load(news.getImage())
                        .error(R.drawable.ic_launcher)
                        .into(mImageViewImage);
            }
        }
    }

    public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<News> mNewsList;
        News mNews;

        NewsAdapter(List<News> newsList) {
            mNewsList = newsList;
        }

        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.item_news, parent, false);
                return new NewsHolder(view);
            } else if (viewType == TYPE_HEADER) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.item_header_news, parent, false);
                return new NewsHeaderHolder(view);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            mNews = mNewsList.get(position);
            if (holder instanceof NewsHolder) {
                ((NewsHolder) holder).bindNews(mNews);
            } else if (holder instanceof NewsHeaderHolder) {
                ((NewsHeaderHolder) holder).bindNewsHeader(mNews);
            }
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
}