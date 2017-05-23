package com.pixelplex.qtum.ui.fragment.TransactionFragment;


import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.pixelplex.qtum.R;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.Vin;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.Vout;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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

        String dateString;
        History history = getInteractor().getHistory(position);
        if(history.getBlockTime()!=null) {
            Date date = new Date(history.getBlockTime() * 1000L);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            String time = new SimpleDateFormat("HH:mm:ss").format(date);
            dateString = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + ", " + calendar.get(Calendar.DATE) + " " + time;
        } else {
            dateString = "Not confirmed";
        }
        List<String> listTo = new ArrayList<>();
        List<String> listFrom = new ArrayList<>();

        for(Vout vout : history.getVout()){
            listTo.add(vout.getAddress());
        }

        for(Vin vin : history.getVin()){
            listFrom.add(vin.getAddress());
        }

        getView().setUpTransactionData(history.getChangeInBalance().toString(), dateString,
                listFrom, listTo, history.getBlockHeight()>0);
    }
}
