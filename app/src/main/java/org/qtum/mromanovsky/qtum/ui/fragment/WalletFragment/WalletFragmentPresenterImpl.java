package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.datastorage.TransactionQTUMList;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionFragment;

import java.util.List;

public class WalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements WalletFragmentPresenter {

//    Intent mIntent;
//    UpdateService mUpdateService;
//    WalletAppKit mWalletAppKit;

    WalletFragmentInteractorImpl mWalletFragmentInteractor;
    private WalletFragmentView mWalletFragmentView;

    public WalletFragmentPresenterImpl(WalletFragmentView walletFragmentView) {
        mWalletFragmentView = walletFragmentView;
        mWalletFragmentInteractor = new WalletFragmentInteractorImpl(getView().getContext());
    }

//    ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            Log.d(WalletFragment.TAG, "OnServiceConnected");
//            mUpdateService = ((UpdateService.UpdateBinder) iBinder).getService();
//            mUpdateService.registerListener(mUpdateData);
//            mWalletAppKit = mUpdateService.getWalletAppKit();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    };

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        ((MainActivity)getView().getFragmentActivity()).getBottomNavigationView().getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        loadAndUpdateData();
    }

    @Override
    public void onStart(Context context) {
        super.onStart(context);
//        mIntent = new Intent(context,UpdateService.class);
//        Log.d(WalletFragment.TAG, "OnStart");
//        context.bindService(mIntent,mServiceConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop(Context context) {
        super.onStop(context);
//        Log.d(WalletFragment.TAG, "OnStop");
//        context.unbindService(mServiceConnection);

    }


//    UpdateData mUpdateData = new UpdateData() {
//        @Override
//        public void updateDate() {
//            getView().updatePublicKey(mWalletAppKit.ic_wallet().currentReceiveAddress().toString());
//            getView().loadAndUpdateData(mWalletAppKit.ic_wallet().getTransaction().toFriendlyString());
//        }
//    };

    @Override
    public WalletFragmentView getView() {
        return mWalletFragmentView;
    }

    public WalletFragmentInteractorImpl getInteractor() {
        return mWalletFragmentInteractor;
    }

    @Override
    public void onClickReceive() {
        Fragment fragment = ReceiveFragment.newInstance();
        getView().openFragmentAndAddToBackStack(fragment);
    }

    @Override
    public void onClickQrCode() {
        SendBaseFragment sendBaseFragment = SendBaseFragment.newInstance(true);
        getView().openFragment(sendBaseFragment);
        ((MainActivity)getView().getFragmentActivity()).getBottomNavigationView().getMenu().getItem(3).setChecked(true);
//        QrCodeRecognitionDialogFragment qrCodeRecognitionDialogFragment = new QrCodeRecognitionDialogFragment();
//        qrCodeRecognitionDialogFragment.show(getView().getFragmentActivity().getFragmentManager(),"qr_code_recognition");
    }

    @Override
    public void onRefresh() {
        loadAndUpdateData();
    }

    @Override
    public void sharePubKey() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My QTUM address: " + getInteractor().getAddress());
        emailIntent.setType("text/plain");
        getView().getFragmentActivity().startActivity(emailIntent);
    }

    @Override
    public void openTransactionFragment(int position) {
        Fragment fragment = TransactionFragment.newInstance(position);
        getView().openFragmentAndAddToBackStack(fragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        updateData(TransactionQTUMList.getInstance().getTransactionQTUMList());
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        getView().updateRecyclerView(getInteractor().getTransactionList());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setAdapterNull();
    }

    private void loadAndUpdateData(){
        getView().startRefreshAnimation();
        getInteractor().getTransaction(new WalletFragmentInteractorImpl.GetDataCallBack() {
            @Override
            public void onSuccess(List<TransactionQTUM> transactionQTUMList) {
                TransactionQTUMList.getInstance().setTransactionQTUMList(transactionQTUMList);
                updateData(transactionQTUMList);
            }
        });
    }

    private void updateData(List<TransactionQTUM> transactionQTUMList){
        double balance = 0;
        for(TransactionQTUM transactionQTUM: transactionQTUMList){
            balance+=transactionQTUM.getAmount();
        }
        getView().updateData(balance);
        getView().updateRecyclerView(TransactionQTUMList.getInstance().getTransactionQTUMList());
        updatePubKey();
    }

    private void updatePubKey(){
        String pubKey = getInteractor().getAddress();
        getView().updatePubKey(pubKey);
    }
}
