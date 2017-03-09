package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;

import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SendBaseFragmentView extends BaseFragmentView{
    void openFragmentForResult(Fragment fragment);
    void qrCodeRecognitionToolBar();
    void sendToolBar();
    void updateData(String publicAddress, double amount);
    void errorRecognition();
    void confirmError(String errorText);
    void clearError();
}
