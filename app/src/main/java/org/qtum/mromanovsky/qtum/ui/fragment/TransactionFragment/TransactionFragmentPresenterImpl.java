package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.utils.Transaction;

public class TransactionFragmentPresenterImpl extends BaseFragmentPresenterImpl implements TransactionFragmentPresenter{

    private TransactionFragmentView mTransactionFragmentView;
    private TransactionFragmentInteractorImpl mTransactionFragmentInteractor;

    public TransactionFragmentPresenterImpl(TransactionFragmentView transactionFragmentView){
        mTransactionFragmentView = transactionFragmentView;
        mTransactionFragmentInteractor = new TransactionFragmentInteractorImpl();
    }

    @Override
    public TransactionFragmentView getView() {
        return mTransactionFragmentView;
    }

    public TransactionFragmentInteractorImpl getInteractor() {
        return mTransactionFragmentInteractor;
    }

    @Override
    public void openTransactionView(int position) {
        Transaction transaction = getInteractor().getTransaction(position);
        getView().setUpTransactionData(transaction.getValue(),transaction.getDate(),
                transaction.getFrom(),transaction.getTo());
    }
}
