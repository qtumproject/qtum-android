package org.qtum.wallet.ui.fragment.send_fragment.dark;

import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.fragment.send_fragment.AddressWithTokenBalanceSpinnerAdapter;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.utils.FontManager;

public class SendFragmentDark extends SendFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_send;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTextInputEditTextAddress.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        mTextInputEditTextAmount.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        mTextInputEditTextFee.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        tilAdress.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        tilAmount.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        tilFee.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
    }

    @Override
    public void setUpSpinner(TokenBalance tokenBalance, Integer decimalUnits) {
        if(mSpinner.getAdapter()!=null &&
                ((AddressWithTokenBalanceSpinnerAdapter)mSpinner.getAdapter()).getTokenBalance().getContractAddress().equals(tokenBalance.getContractAddress())) {
            adapter.setTokenBalance(tokenBalance);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new AddressWithTokenBalanceSpinnerAdapterDark(getContext(), tokenBalance, "test", decimalUnits);
            mSpinner.setAdapter(adapter);
        }
    }
}
