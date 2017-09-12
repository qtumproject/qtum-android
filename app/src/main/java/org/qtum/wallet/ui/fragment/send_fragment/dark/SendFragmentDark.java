package org.qtum.wallet.ui.fragment.send_fragment.dark;

import android.view.View;

import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.utils.FontManager;

import butterknife.OnClick;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class SendFragmentDark extends SendFragment {

    @OnClick(org.qtum.wallet.R.id.bt_send)
    public void onSendClick(){
        String[] sendInfo = new String[3];
        sendInfo[0] = mTextInputEditTextAddress.getText().toString();
        sendInfo[1] = mTextInputEditTextAmount.getText().toString();
        if(mLinearLayoutCurrency.getVisibility()==View.VISIBLE){
            sendInfo[2] = mTextViewCurrency.getText().toString();
        } else {
            sendInfo[2] = "Qtum "+getString(org.qtum.wallet.R.string.default_currency);
        }
        getPresenter().send(sendInfo);
    }

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_send;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        mTextInputEditTextAddress.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        mTextInputEditTextAmount.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        tilAdress.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        tilAmount.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
    }

}
