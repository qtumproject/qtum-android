package com.pixelplex.qtum.ui.fragment.CurrencyFragment.Light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.Currency;
import com.pixelplex.qtum.ui.fragment.CurrencyFragment.CurrencyFragment;
import com.pixelplex.qtum.utils.SearchBarLight;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class CurrencyFragmentLight extends CurrencyFragment {

    @BindView(R.id.search_bar)
    SearchBarLight searchBar;

    @Override
    protected int getLayout() {
        return R.layout.fragment_currency_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);
    }

    @Override
    public void setCurrencyList(List<Currency> currencyList) {
        mCurrencyAdapter = new CurrencyAdapter(currencyList, R.layout.lyt_token_list_item_light);
        mCurrentList = currencyList;
        mRecyclerView.setAdapter(mCurrencyAdapter);
    }
}
