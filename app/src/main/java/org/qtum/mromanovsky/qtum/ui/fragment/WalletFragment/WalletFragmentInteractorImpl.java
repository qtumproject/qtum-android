package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.datastorage.Transactions;
import org.qtum.mromanovsky.qtum.model.Transaction;

import java.util.List;

public class WalletFragmentInteractorImpl implements WalletFragmentInteractor {

    @Override
    public List<Transaction> getTransactionList() {
        List<Transaction> transactionList = Transactions.getInstance().getTransactionList();
        transactionList.add(new Transaction("5odqs25oxasmewqo4129d", "Sep 23", 7.450, "231dcasdawe123123lcasd", "231dcasdawe123123lcasd"));
        transactionList.add(new Transaction("4129d5odqs25oxasmewqo", "Nov 03", -7.450, "231dcasdawe123123lcasd", "231dcasdawe123123lcasd"));
        transactionList.add(new Transaction("oxasmew5odqs25qo4129d", "Dec 16", 0.930, "231dcasdawe123123lcasd", "231dcasdawe123123lcasd"));
        return transactionList;
    }
}
