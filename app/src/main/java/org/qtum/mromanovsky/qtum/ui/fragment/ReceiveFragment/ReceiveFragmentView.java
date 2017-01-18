package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;

import android.graphics.Bitmap;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;


public interface ReceiveFragmentView extends BaseFragmentView {
    void setQrCode(Bitmap bitmap);
    void setAddressInTV(String s);
}
