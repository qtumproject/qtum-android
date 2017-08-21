package com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment.Dark;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.history.TransactionInfo;
import com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment.TransactionDetailAdapter;
import com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment.TransactionDetailFragment;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 11.07.17.
 */

public class TransactionDetailFragmentDark extends TransactionDetailFragment {

    protected TransactionDetailAdapterDark mTransactionDetailAdapter;

    @Override
    public void setUpRecyclerView(List<TransactionInfo> transactionInfoList) {
        mTransactionDetailAdapter = new TransactionDetailAdapterDark(transactionInfoList);
        mRecyclerView.setAdapter(mTransactionDetailAdapter);
    }
}
