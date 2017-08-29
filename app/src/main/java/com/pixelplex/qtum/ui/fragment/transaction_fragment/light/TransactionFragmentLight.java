package com.pixelplex.qtum.ui.fragment.transaction_fragment.light;

import android.widget.ImageView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.transaction_fragment.TransactionFragment;
import java.util.List;
import butterknife.BindView;

/**
 * Created by kirillvolkov on 11.07.17.
 */

public class TransactionFragmentLight extends TransactionFragment {

    @BindView(R.id.ic_confirm)
    ImageView confirmIcon;

    private boolean confirmed;

    @Override
    protected int getLayout() {
        return R.layout.fragment_transaction_light;
    }

    @Override
    public void setUpTransactionData(String value, String receivedTime, List<String> from, List<String> to, boolean confirmed) {
        setTransactionData(value, receivedTime);
        this.confirmed = confirmed;
        checkConfirmation(this.confirmed);
    }

    @Override
    public void onPause() {
        super.onPause();
        getMainActivity().recolorStatusBar(R.color.title_color_light);
    }

    @Override
    public void onUserResume() {
        super.onUserResume();
        checkConfirmation(this.confirmed);
    }

    void checkConfirmation(boolean confirmed){
        if(confirmed){
            getMainActivity().recolorStatusBar(R.color.title_lt_green_color);
            toolbarLayout.setBackgroundResource(R.drawable.transaction_back_confirmed);
            confirmIcon.setImageResource(R.drawable.ic_confirmed_light);
            notConfFlag.setText(R.string.confirmed);
        } else {
            getMainActivity().recolorStatusBar(R.color.title_red_color);
            toolbarLayout.setBackgroundResource(R.drawable.transaction_back_not_confirmed);
            confirmIcon.setImageResource(R.drawable.ic_confirmation_loader);
            notConfFlag.setText(R.string.not_confirmed_yet);
        }
    }
}
