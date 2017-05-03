package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface ReceiveFragmentView extends BaseFragmentView {
    void setQrCode(Bitmap bitmap);
    void setUpAddress(String s);
    void setBalance(String balance);
    void showToast();
    void openFragmentForResult(Fragment fragment);
}
