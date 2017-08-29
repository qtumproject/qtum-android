package com.pixelplex.qtum.ui.fragment.currency_fragment.dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.Currency;
import com.pixelplex.qtum.ui.fragment.currency_fragment.CurrencyFragment;
import com.pixelplex.qtum.utils.SearchBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class CurrencyFragmentDark extends CurrencyFragment {

    @BindView(R.id.search_bar)
    SearchBar searchBar;

    @Override
    protected int getLayout() {
        return R.layout.fragment_currency;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);
    }

    @Override
    public void setCurrencyList(List<Currency> currencyList) {
        mCurrencyAdapter = new CurrencyAdapter(currencyList, R.layout.lyt_token_list_item);
        mCurrentList = currencyList;
        mRecyclerView.setAdapter(mCurrencyAdapter);
    }
}
