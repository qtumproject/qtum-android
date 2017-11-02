package org.qtum.wallet.ui.fragment.news_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.ui.fragment.news_detail_fragment.NewsDetailFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class NewsFragment extends BaseFragment implements NewsView {

    private NewsPresenter mNewsFragmentPresenter;
    protected NewsAdapter mNewsAdapter;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private NetworkStateReceiver mNetworkStateReceiver;
    private NetworkStateListener mNetworkStateListener;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, NewsFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mNewsFragmentPresenter = new NewsPresenterImpl(this, new NewsInteractorImpl(getContext()));
    }

    @Override
    protected NewsPresenter getPresenter() {
        return mNewsFragmentPresenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNetworkStateReceiver = getMainActivity().getNetworkReceiver();
        mNetworkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                getPresenter().onNetworkStateChanged(networkConnectedFlag);
            }
        };
        mNetworkStateReceiver.addNetworkStateListener(mNetworkStateListener);
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
    public void setAdapterNull() {
        mNewsAdapter = null;
    }

    @Override
    public void startRefreshAnimation() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_description)
        TextView mTextViewDescription;
        @BindView(R.id.tv_title)
        TextView mTextViewTitle;
        @BindView(R.id.tv_date)
        TextView mTextViewDate;

        NewsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFragment newsDetailFragment = NewsDetailFragment.newInstance(getContext(),getAdapterPosition());
                    openFragment(newsDetailFragment);
                }
            });
            ButterKnife.bind(this, itemView);
        }

        void bindNews(News news) {
            mTextViewTitle.setText(news.getTitle());
            mTextViewDate.setText(news.getFormattedPubDate());
            mTextViewDescription.setText(news.getDocument().select("p").get(0).text());
        }
    }


    public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<News> mNewsList;
        News mNews;
        private @LayoutRes int mResId;

        public NewsAdapter(List<News> newsList, @LayoutRes int resId) {
            mNewsList = newsList;
            mResId = resId;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(mResId, parent, false);
            return new NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            mNews = mNewsList.get(position);
            ((NewsHolder) holder).bindNews(mNews);

        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNetworkStateReceiver.removeNetworkStateListener(mNetworkStateListener);
        setAdapterNull();
    }

    @Override
    public void stopRefreshRecyclerAnimation() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
