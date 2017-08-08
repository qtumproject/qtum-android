package com.pixelplex.qtum.ui.fragment.SendFragment.Dark;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.SendFragment.SendFragment;
import com.pixelplex.qtum.utils.FontManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class SendFragmentDark extends SendFragment {

    @OnClick(R.id.bt_send)
    public void onSendClick(){
        String[] sendInfo = new String[3];
        sendInfo[0] = mTextInputEditTextAddress.getText().toString();
        sendInfo[1] = mTextInputEditTextAmount.getText().toString();
        if(mLinearLayoutCurrency.getVisibility()==View.VISIBLE){
            sendInfo[2] = mTextViewCurrency.getText().toString();
        } else {
            sendInfo[2] = "Qtum "+getString(R.string.default_currency);
        }
        getPresenter().send(sendInfo);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_send;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        mTextInputEditTextAddress.setTypeface(FontManager.getInstance().getFont(getString(R.string.simplonMonoRegular)));
        mTextInputEditTextAmount.setTypeface(FontManager.getInstance().getFont(getString(R.string.simplonMonoRegular)));
        tilAdress.setTypeface(FontManager.getInstance().getFont(getString(R.string.simplonMonoRegular)));
        tilAmount.setTypeface(FontManager.getInstance().getFont(getString(R.string.simplonMonoRegular)));
    }

}
