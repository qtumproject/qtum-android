package org.qtum.mromanovsky.qtum.ui.fragment.SendFragment;


public interface SendFragmentPresenter {
    void onClickQrCode();
    void send(String[] sendInfo);
}
