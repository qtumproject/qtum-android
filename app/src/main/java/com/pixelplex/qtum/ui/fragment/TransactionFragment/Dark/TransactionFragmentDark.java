package com.pixelplex.qtum.ui.fragment.TransactionFragment.Dark;

import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionFragment;

import java.util.List;

/**
 * Created by kirillvolkov on 11.07.17.
 */

public class TransactionFragmentDark extends TransactionFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_transaction;
    }

    @Override
    public void setUpTransactionData(String value, String receivedTime, List<String> from, List<String> to, boolean confirmed) {
        setTransactionData(value, receivedTime);
        notConfFlag.setVisibility((!confirmed)? View.VISIBLE : View.INVISIBLE);
    }
}
