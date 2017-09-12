package org.qtum.wallet.ui.fragment.currency_fragment.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.ui.fragment.currency_fragment.CurrencyFragment;
import org.qtum.wallet.utils.SearchBar;

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
