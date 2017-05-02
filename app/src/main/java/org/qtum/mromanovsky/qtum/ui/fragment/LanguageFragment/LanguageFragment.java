package org.qtum.mromanovsky.qtum.ui.fragment.LanguageFragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageFragment extends BaseFragment implements LanguageFragmentView{

    LanguageFragmentPresenter mLanguageFragmentPresenter;
    private LanguageAdapter mLanguageAdapter;
    private List<String> mLanguagesList;

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
    public void setUpLanguagesList(List<String> languagesList) {
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
                        getPresenter().setCurrentLanguage(mLanguageAdapter.mLanguagesList.get(getAdapterPosition()));
                        mLanguageAdapter.notifyItemChanged(oldPosition);
                        mLanguageAdapter.notifyItemChanged(getAdapterPosition());
                    }
                }
            });
            ButterKnife.bind(this, itemView);
        }

        void bindLanguage(String language) {
            if (language.equals(getPresenter().getCurrentLanguage())) {
                mImageViewCheckIndicator.setVisibility(View.VISIBLE);
                mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey20));
            } else {
                mImageViewCheckIndicator.setVisibility(View.GONE);
                mLinearLayoutAddress.setBackgroundColor(Color.WHITE);
            }
            mTextViewLanguage.setText(language);
        }
    }

    public class LanguageAdapter extends RecyclerView.Adapter<LanguageHolder> {

        private List<String> mLanguagesList;
        private String mLanguage;


        LanguageAdapter(List<String> languagesList) {
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
        for(String lang : mLanguagesList){
            if(lang.equals(currentLanguage)){
                return pos;
            }
            pos++;
        }
        return pos;
    }
}
