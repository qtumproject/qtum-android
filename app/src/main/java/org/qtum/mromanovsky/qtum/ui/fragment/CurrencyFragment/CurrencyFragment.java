package org.qtum.mromanovsky.qtum.ui.fragment.CurrencyFragment;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.datastorage.Currency;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrencyFragment extends BaseFragment implements CurrencyFragmentView{

    private final int LAYOUT = R.layout.fragment_currency;
    private CurrencyFragmentPresenterImpl mCurrencyFragmentPresenter;
    private CurrencyAdapter mCurrencyAdapter;
    private String mSearchString;
    private boolean mIsCurrencyFragment;
    private static String IS_CURRENCY_FRAGMENT = "is_currency_fragment";

    //TODO: remove
    String currentCurrency = "one";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_search_currency)
    EditText mEditTextSearchCurrency;
    @BindView(R.id.tv_currency_title)
    TextView mTextViewCurrencyTitle;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static CurrencyFragment newInstance(boolean isCurrencyFragment) {

        Bundle args = new Bundle();
        args.putBoolean(IS_CURRENCY_FRAGMENT, isCurrencyFragment);
        CurrencyFragment fragment = new CurrencyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mCurrencyFragmentPresenter = new CurrencyFragmentPresenterImpl(this);
    }

    @Override
    protected CurrencyFragmentPresenterImpl getPresenter() {
        return mCurrencyFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mIsCurrencyFragment = getArguments().getBoolean(IS_CURRENCY_FRAGMENT);
        if(mIsCurrencyFragment){
            mTextViewCurrencyTitle.setText(R.string.currency);
        } else{
            mTextViewCurrencyTitle.setText(R.string.chose_to_subscribe);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final List<Currency> list = new ArrayList<>();
        list.add(new Currency("one"));
        list.add(new Currency("two"));
        list.add(new Currency("three"));

        mCurrencyAdapter = new CurrencyAdapter(list);
        mRecyclerView.setAdapter(mCurrencyAdapter);

        mEditTextSearchCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    mCurrencyAdapter.setFilter(list);
                } else {
                    mSearchString = editable.toString().toLowerCase();
                    List<Currency> newList = new ArrayList<Currency>();
                    for(Currency currency: list){
                        if(currency.getName().contains(mSearchString))
                            newList.add(currency);
                    }
                    mCurrencyAdapter.setFilter(newList);
                }
            }
        });
    }

    public class CurrencyHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_currency)
        TextView mTextViewCurrency;
        @BindView(R.id.iv_check_indicator)
        ImageView mImageViewCheckIndicator;
        @BindView(R.id.ll_currency)
        LinearLayout mLinearLayoutCurrency;

        CurrencyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            if(!mIsCurrencyFragment){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentCurrency = mCurrencyAdapter.getCurrencyList().get(getAdapterPosition()).getName();
                        mCurrencyAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        void bindCurrency(String currency){
            mTextViewCurrency.setText(currency);
            if(!mIsCurrencyFragment && currency.equals(currentCurrency)){
                mLinearLayoutCurrency.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.grey20));
                mImageViewCheckIndicator.setVisibility(View.VISIBLE);
            } else {
                mLinearLayoutCurrency.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.background_white_with_grey_pressed));
                mImageViewCheckIndicator.setVisibility(View.GONE);
            }
        }
    }

    public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyHolder>{

        List<Currency> mCurrencyList;

        CurrencyAdapter(List<Currency> currencyList){
            mCurrencyList = currencyList;
        }

        @Override
        public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_currency, parent, false);
            return new CurrencyHolder(view);
        }

        @Override
        public void onBindViewHolder(CurrencyHolder holder, int position) {
            holder.bindCurrency(mCurrencyList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mCurrencyList.size();
        }

        public void setFilter(List<Currency> newList){
            mCurrencyList = new ArrayList<>();
            mCurrencyList.addAll(newList);
            notifyDataSetChanged();
        }

        public List<Currency> getCurrencyList() {
            return mCurrencyList;
        }
    }
}
