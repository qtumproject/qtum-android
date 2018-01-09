package org.qtum.wallet.ui.fragment.overview_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.addresses_detail_fragment.AddressesDetailFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class OverviewFragment extends BaseFragment implements OverviewView{

    OverviewPresenter mOverviewPresenter;
    public static String POSITION = "position";

    @BindView(R.id.recycler_view_overview)
    protected
    RecyclerView mRecyclerViewOverview;
    protected OverviewAdapter mOverviewAdapter;

    public static Fragment newInstance(Context context, int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        Fragment fragment = Factory.instantiateDefaultFragment(context, OverviewFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mOverviewPresenter = new OverviewPresenterImpl(this, new OverviewIteractorImpl(getContext()));
    }

    @Override
    protected OverviewPresenter getPresenter() {
        return mOverviewPresenter;
    }

    @Override
    public int getPosition() {
        return getArguments().getInt(AddressesDetailFragment.POSITION);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerViewOverview.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    class OverviewViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        TextView mTextViewTitle;
        @BindView(R.id.tv_value)
        TextView mTextViewValue;

        public OverviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bindOverview(Pair<String, String> overview){
            mTextViewTitle.setText(overview.first);
            mTextViewValue.setText(overview.second);
        }

    }

    protected class OverviewAdapter extends RecyclerView.Adapter<OverviewViewHolder>{

        List<Pair<String, String>> mOverview;
        @LayoutRes int mResId;

        public OverviewAdapter(List<Pair<String, String>> overview, @LayoutRes int resId){
            mOverview = overview;
            mResId = resId;
        }

        @Override
        public OverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(mResId, parent, false);
            return new OverviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OverviewViewHolder holder, int position) {
            holder.bindOverview(mOverview.get(position));
        }

        @Override
        public int getItemCount() {
            return mOverview.size();
        }
    }
}
