package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.model.TransactionQTUM;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TransactionFragmentPresenterImpl extends BaseFragmentPresenterImpl implements TransactionFragmentPresenter {

    private TransactionFragmentView mTransactionFragmentView;
    private TransactionFragmentInteractorImpl mTransactionFragmentInteractor;

    public TransactionFragmentPresenterImpl(TransactionFragmentView transactionFragmentView) {
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
        //// TODO: stub
        TransactionQTUM transactionQTUM = getInteractor().getTransaction(position);

        Date date = new Date(transactionQTUM.getTime()*1000L);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String time = new SimpleDateFormat("HH:mm:ss").format(date);
        String dateString = calendar.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.US) + ", " + calendar.get(Calendar.DATE) + " " + time;

        getView().setUpTransactionData(transactionQTUM.getAmount(), dateString,
                "stub", transactionQTUM.getAddress());
    }
}
