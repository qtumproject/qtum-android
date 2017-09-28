package org.qtum.wallet.ui.fragment.currency_fragment.light;

import org.qtum.wallet.model.Currency;
import org.qtum.wallet.ui.fragment.currency_fragment.CurrencyFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class CurrencyFragmentLight extends CurrencyFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_currency_light;
    }

    @Override
    public void setCurrencyList(List<Currency> currencyList) {
        mCurrencyAdapter = new CurrencyAdapter(currencyList, org.qtum.wallet.R.layout.lyt_token_list_item_light);
        mCurrentList = currencyList;
        mRecyclerView.setAdapter(mCurrencyAdapter);
    }
}
