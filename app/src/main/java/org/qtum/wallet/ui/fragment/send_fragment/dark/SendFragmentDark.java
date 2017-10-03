package org.qtum.wallet.ui.fragment.send_fragment.dark;

import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.utils.FontManager;

/**
 * Created by kirillvolkov on 06.07.17.
 */

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

}
