package org.qtum.wallet.ui.fragment.other_tokens;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OtherTokensPresenterImpl extends BaseFragmentPresenterImpl implements OtherTokensPresenter {
    private OtherTokensView view;
    private OtherTokensInteractor interactor;

    public OtherTokensPresenterImpl(OtherTokensView view, OtherTokensInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public OtherTokensView getView() {
        return view;
    }

    @Override
    public void notifyNewToken() {
        getInteractor().getTokenObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Token>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Token> tokens) {
                        if (tokens != null && tokens.size() > 0) {
                            getView().setTokensData(tokens);
                        }
                    }
                });
    }

    public OtherTokensInteractor getInteractor() {
        return interactor;
    }
}
