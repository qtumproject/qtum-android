package com.pixelplex.qtum.ui.fragment.LanguageFragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageFragment extends BaseFragment implements LanguageFragmentView{

    LanguageFragmentPresenter mLanguageFragmentPresenter;
    private LanguageAdapter mLanguageAdapter;
    private List<Pair<String,String>> mLanguagesList;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_toolbar_language)
    TextView mTextViewToolBarLanguage;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static LanguageFragment newInstance() {

        Bundle args = new Bundle();

        LanguageFragment fragment = new LanguageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mLanguageFragmentPresenter = new LanguageFragmentPresenter(this);
    }

    @Override
    protected LanguageFragmentPresenter getPresenter() {
        return mLanguageFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_language;
    }

    @Override
    public void initializeViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setUpLanguagesList(List<Pair<String,String>> languagesList) {
        mLanguagesList = languagesList;
        mLanguageAdapter = new LanguageAdapter(languagesList);
        mRecyclerView.setAdapter(mLanguageAdapter);
    }

    @Override
    public void resetText() {
        mTextViewToolBarLanguage.setText(R.string.language);
    }

    public class LanguageHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_single_string)
        TextView mTextViewLanguage;
        @BindView(R.id.iv_check_indicator)
        ImageView mImageViewCheckIndicator;
        @BindView(R.id.ll_single_item)
        LinearLayout mLinearLayoutAddress;

        LanguageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int oldPosition = findLanguagePosition(getPresenter().getCurrentLanguage());
                    if(oldPosition != getAdapterPosition()){
                        getPresenter().setCurrentLanguage(mLanguageAdapter.mLanguagesList.get(getAdapterPosition()).first);
                        mLanguageAdapter.notifyItemChanged(oldPosition);
                        mLanguageAdapter.notifyItemChanged(getAdapterPosition());
                    }
                }
            });
            ButterKnife.bind(this, itemView);
        }

        void bindLanguage(Pair<String,String> language) {
            if (language.first.equals(getPresenter().getCurrentLanguage())) {
                mImageViewCheckIndicator.setVisibility(View.VISIBLE);
                mTextViewLanguage.setTextColor(ContextCompat.getColor(getContext(), R.color.background));
                mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.accent_red_color));
            } else {
                mTextViewLanguage.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                mImageViewCheckIndicator.setVisibility(View.GONE);
                mLinearLayoutAddress.setBackgroundColor(Color.TRANSPARENT);
            }
            mTextViewLanguage.setText(language.second);
        }
    }

    public class LanguageAdapter extends RecyclerView.Adapter<LanguageHolder> {

        private List<Pair<String,String>> mLanguagesList;
        private Pair<String,String> mLanguage;


        LanguageAdapter(List<Pair<String,String>> languagesList) {
            mLanguagesList = languagesList;
        }

        @Override
        public LanguageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_single_checkable, parent, false);
            return new LanguageHolder(view);
        }

        @Override
        public void onBindViewHolder(LanguageHolder holder, int position) {
            mLanguage = mLanguagesList.get(position);
            holder.bindLanguage(mLanguage);
        }

        @Override
        public int getItemCount() {
            return mLanguagesList.size();
        }
    }

    private int findLanguagePosition(String currentLanguage){
        int pos = 0;
        for(Pair<String,String> lang : mLanguagesList){
            if(lang.first.equals(currentLanguage)){
                return pos;
            }
            pos++;
        }
        return pos;
    }
}
