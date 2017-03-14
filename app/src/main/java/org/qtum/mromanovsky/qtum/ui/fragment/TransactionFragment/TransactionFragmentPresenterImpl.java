package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import android.os.Build;
import android.support.v4.content.ContextCompat;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

class TransactionFragmentPresenterImpl extends BaseFragmentPresenterImpl implements TransactionFragmentPresenter {

    private TransactionFragmentView mTransactionFragmentView;
    private TransactionFragmentInteractorImpl mTransactionFragmentInteractor;

    TransactionFragmentPresenterImpl(TransactionFragmentView transactionFragmentView) {
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
    public void onDestroyView() {
        super.onDestroyView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getView().getFragmentActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getView().getContext(), R.color.colorPrimaryDark));
        }
    }

    @Override
    public void openTransactionView(int position) {

        History history = getInteractor().getHistory(position);
        Date date = new Date(history.getBlockTime()*1000L);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String time = new SimpleDateFormat("HH:mm:ss").format(date);
        String dateString = calendar.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.US) + ", " + calendar.get(Calendar.DATE) + " " + time;
        getView().setUpTransactionData(history.getAmount()*(QtumSharedPreference.getInstance().getExchangeRates(getView().getContext())), dateString,
                history.getFromAddress(), history.getToAddress());
    }
}
