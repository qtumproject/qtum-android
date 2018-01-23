package org.qtum.wallet.ui.fragment.receive_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.math.BigDecimal;

import rx.Subscription;

public class ReceivePresenterImpl extends BaseFragmentPresenterImpl implements ReceivePresenter {

    private ReceiveView mReceiveView;
    private ReceiveInteractor mReceiveInteractor;
    private String mAmount = "0.0";
    private String mTokenAddress;
    private Subscription subscription;

    public ReceivePresenterImpl(ReceiveView view, ReceiveInteractor interactor) {
        mReceiveView = view;
        mReceiveInteractor = interactor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().setUpAddress(getInteractor().getCurrentReceiveAddress());
        changeAmount("");
    }

    @Override
    public void onBalanceChanged(BigDecimal unconfirmedBalance, BigDecimal balance) {
        String balanceString = balance.toString();
        if (balanceString != null) {
            String unconfirmedBalanceString = unconfirmedBalance.toString();
            if (getView().isUnconfirmedBalanceValid(unconfirmedBalanceString)) {
                getView().updateBalance(balanceString, unconfirmedBalanceString, "QTUM");
            } else {
                getView().updateBalance(balanceString, null, "QTUM");
            }
        }
    }

    @Override
    public ReceiveView getView() {
        return mReceiveView;
    }

    private ReceiveInteractor getInteractor() {
        return mReceiveInteractor;
    }

    @Override
    public void changeAmount(String s) {
        mAmount = s;
        String result = buildFullQrCodeData(getInteractor().getCurrentReceiveAddress(), mAmount, mTokenAddress);
        getView().showSpinner();
        subscription = getView().imageEncodeObservable(result);
    }

    @Override
    public void setTokenAddress(String address) {
        mTokenAddress = address;
        String result = buildFullQrCodeData(getInteractor().getCurrentReceiveAddress(), mAmount, mTokenAddress);
        getView().showSpinner();
        subscription = getView().imageEncodeObservable(result);
    }

    private String getFormattedReceiveAddr(String addr) {
        return getInteractor().formatReceiveAddress(addr);
    }

    private String getFormattedAmount(String amount) {
        if (getView().isAmountValid(amount)) {
            return getInteractor().formatAmount(amount);
        } else {
            return "";
        }
    }

    private String getadditionalInfo() {
        return "label=QTUM Mobile Wallet&message=Payment Request";
    }

    private String getFormattedTokenAddr(String addr) {
        if (getView().isTokenAddressValid(addr)) {
            return getInteractor().formatTokenAddress(addr);
        } else {
            return "";
        }
    }

    private String buildFullQrCodeData(String receiveAddr, String amount, String mTokenAddress) {
        return getFormattedReceiveAddr(receiveAddr) + getFormattedAmount(amount) + getadditionalInfo() + getFormattedTokenAddr(mTokenAddress);
    }

    @Override
    public void changeAddress() {
        String result = buildFullQrCodeData(getInteractor().getCurrentReceiveAddress(), mAmount, mTokenAddress);
        getView().showSpinner();
        subscription = getView().imageEncodeObservable(result);
        getView().setUpAddress(getInteractor().getCurrentReceiveAddress());
    }

    private int moduleWidth = 0;
    private boolean withCrossQR = false;
    private int patternWidth = 0;
    private int topOffsetHeight = 0;
    private int leftOffcetWidth = 0;
    private boolean firstInputY, firstInputX = false;

    @Override
    public void calcModuleWidth(boolean isDataPixel, int x, int y) {
        if (isDataPixel) {
            if (!firstInputY) {
                topOffsetHeight = y;
                firstInputY = true;
            }
            if (!firstInputX) {
                leftOffcetWidth = x;
                firstInputX = true;
            }
            patternWidth++;
        } else {
            if (patternWidth > 0) {
                moduleWidth = (moduleWidth > patternWidth) ? patternWidth : moduleWidth;
                patternWidth = 0;
            }
        }
    }

    @Override
    public void setModuleWidth(int moduleWidth) {
        this.moduleWidth = moduleWidth;
    }

    @Override
    public boolean getWithCrossQr() {
        return withCrossQR;
    }

    @Override
    public int getTopOffsetHeight() {
        return topOffsetHeight;
    }

    @Override
    public int getModuleWidth() {
        return moduleWidth;
    }

    @Override
    public CharSequence getCurrentReceiveAddress() {
        return getInteractor().getCurrentReceiveAddress();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}