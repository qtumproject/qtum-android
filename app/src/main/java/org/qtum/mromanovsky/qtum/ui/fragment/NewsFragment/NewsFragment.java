package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.News;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsFragment extends BaseFragment implements NewsFragmentView {

    public static final int LAYOUT = R.layout.fragment_news;

    NewsFragmentPresenterImpl mNewsFragmentPresenter;
    NewsAdapter mNewsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static NewsFragment newInstance() {
        NewsFragment newsFragment = new NewsFragment();
        return newsFragment;
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
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
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
    public void setAdapterNull(){
        mNewsAdapter = null;
    }

    @Override
    public void startRefreshAnimation() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public class NewsHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_image)
        ImageView mImageViewImage;
        @BindView(R.id.tv_title)
        TextView mTextViewTitle;
        @BindView(R.id.tv_date)
        TextView mTextViewDate;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bindNews(News news){
            mTextViewTitle.setText(news.getTitle());
            mTextViewDate.setText(news.getDate());
            if(news.getImage()!=null) {
                Picasso
                        .with(getActivity())
                        .load(news.getImage())
                        .error(R.drawable.ic_launcher)
                        .into(mImageViewImage);
            }
        }
    }

    public class NewsAdapter extends RecyclerView.Adapter<NewsHolder>{

        private List<News> mNewsList;
        News mNews;

        public NewsAdapter(List<News> newsList){
            mNewsList = newsList;
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_news, parent, false);
            return new NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            mNews = mNewsList.get(position);
            holder.bindNews(mNews);
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
}
