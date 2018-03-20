package org.qtum.wallet.ui.fragment.transaction_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.math.BigDecimal;

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
    public void openTransactionView(final String txHash, HistoryType historyType) {
        String dateString;
        Long dateLong = 0L;
        String fee = "";
        String changeInBalance = "";
        String symbol = "";
        switch (historyType){
            case History:
                final History history = getInteractor().getHistory(txHash);
                dateLong = history.getBlockTime();
                fee = history.getFee();
                changeInBalance = history.getChangeInBalance();
                symbol = "QTUM";
                break;
            case Token_History:
                final TokenHistory tokenHistory = getInteractor().getTokenHistory(txHash);
                dateLong = tokenHistory.getTxTime();
                changeInBalance = new BigDecimal(tokenHistory.getAmount()).divide(new BigDecimal("10").pow(getView().getDecimalUnits())).toString();
                symbol = getView().getSymbol();
                break;
        }

        if (dateLong != null) {
            dateString = getInteractor().getFullDate(dateLong * 1000L);
        } else {
            dateString = getInteractor().getUnconfirmedDate();
        }
        TransactionReceipt transactionReceipt = getInteractor().getHistoryReceipt(getView().getRealm(), txHash);
        getView().setUpTransactionData(changeInBalance, symbol,fee, dateString,
                dateLong!=null && dateLong > 0, transactionReceipt != null && transactionReceipt.getLog() != null && !transactionReceipt.getLog().isEmpty());


    }
}
