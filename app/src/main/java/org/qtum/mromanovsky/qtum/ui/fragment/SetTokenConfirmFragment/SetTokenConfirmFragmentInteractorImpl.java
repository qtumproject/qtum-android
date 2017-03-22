package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.ByteCode;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.GenerateTokenBytecodeRequest;
import org.qtum.mromanovsky.qtum.datastorage.QtumToken;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class SetTokenConfirmFragmentInteractorImpl implements SetTokenConfirmFragmentInteractor{

    SetTokenConfirmFragmentInteractorImpl(){ }

    @Override
    public void generateTokenBytecode(final GenerateTokenBytecodeCallBack callBack) {
        QtumToken qtumToken = QtumToken.getInstance();
        GenerateTokenBytecodeRequest generateTokenBytecodeRequest = new GenerateTokenBytecodeRequest(qtumToken.getInitialSupply(),qtumToken.getTokenName(),
                qtumToken.getDecimalUnits(),qtumToken.getTokenSymbol());
        QtumService.newInstance().generateTokenBytecode(generateTokenBytecodeRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ByteCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ByteCode byteCode) {
                        callBack.onSuccess(byteCode.getBytecode());
                    }
                });
    }

    public interface GenerateTokenBytecodeCallBack{
        void onSuccess(String byteCode);
    }
}
