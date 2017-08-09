package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import android.text.TextUtils;
import android.widget.Toast;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StoreContractPresenter extends BaseFragmentPresenterImpl {

    private StoreContractView view;
    private QstoreContract qstoreContract;
    private String abiString;

    public QstoreContract getContract(){
        return qstoreContract;
    }

    public StoreContractPresenter(StoreContractView view) {
        this.view = view;
    }

    @Override
    public StoreContractView getView() {
        return view;
    }

    public void getContractById(String id){
        getView().setProgressDialog();
        if(!TextUtils.isEmpty(id)) {
            QtumService
                    .newInstance()
                    .getContractById(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QstoreContract>() {
                        @Override
                        public void onCompleted() {
                            getView().dismissProgressDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().dismissProgressDialog();
                            Toast.makeText(getView().getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(QstoreContract qstoreContract) {
                            StoreContractPresenter.this.qstoreContract = qstoreContract;
                            getView().setContractData(qstoreContract);
                        }
                    });
        }
    }

    public void getContractAbiById(String id){

        if(!TextUtils.isEmpty(abiString)){
            getView().openAbiViewer(abiString);
            return;
        }

        getView().setProgressDialog();
        if(!TextUtils.isEmpty(id)) {
            QtumService
                    .newInstance()
                    .getAbiByContractId(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {
                            getView().dismissProgressDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().dismissProgressDialog();
                            Toast.makeText(getView().getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(Object abi) {
                            abiString = abi.toString();
                            getView().openAbiViewer(abiString);
                        }
                    });
        }
    }


}
