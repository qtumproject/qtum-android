package org.qtum.wallet.ui.fragment.transaction_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import io.realm.Realm;

public class TransactionPresenterImpl extends BaseFragmentPresenterImpl implements TransactionPresenter {

    private TransactionView mTransactionView;
    private TransactionInteractor mTransactionInteractor;

    public TransactionPresenterImpl(TransactionView view, TransactionInteractor interactor) {
        mTransactionView = view;
        mTransactionInteractor = interactor;
    }

    @Override
    public TransactionView getView() {
        return mTransactionView;
    }

    private TransactionInteractor getInteractor() {
        return mTransactionInteractor;
    }

    @Override
    public void openTransactionView(final String txHash) {
        final String dateString;
        final History history = getInteractor().getHistory(txHash);
        if (history.getBlockTime() != null) {
            dateString = getInteractor().getFullDate(history.getBlockTime() * 1000L);
        } else {
            dateString = getInteractor().getUnconfirmedDate();
        }
        TransactionReceipt transactionReceipt = getInteractor().getHistoryReceipt(getView().getRealm(), history.getTxHash());
        getView().setUpTransactionData(history.getChangeInBalance(), history.getFee(), dateString,
                history.getBlockHeight() > 0, transactionReceipt != null && transactionReceipt.getLog() != null && !transactionReceipt.getLog().isEmpty());


    }
}
