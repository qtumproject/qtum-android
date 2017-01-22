package org.qtum.mromanovsky.qtum.ui.fragment.QrCodeRecognitionFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;
import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.SendFragment.SendFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QrCodeRecognitionFragment extends Fragment implements ZXingScannerView.ResultHandler{

    public static final String BACK_STACK_ROOT_TAG = "root_fragment";
    public static final String IS_PARENT_SEND_FRAGMENT = "is_parent_send_fragment";
    public boolean mIsParentSendFragment;

    public static QrCodeRecognitionFragment newInstance(boolean isParentSendFragment){
        QrCodeRecognitionFragment qrCodeRecognitionFragment = new QrCodeRecognitionFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_PARENT_SEND_FRAGMENT, isParentSendFragment);
        qrCodeRecognitionFragment.setArguments(args);
        return qrCodeRecognitionFragment;
    }

    ZXingScannerView mZXingScannerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mZXingScannerView = new ZXingScannerView(getContext());
        mZXingScannerView.setResultHandler(this);
        mIsParentSendFragment = getArguments().getBoolean(IS_PARENT_SEND_FRAGMENT);
        return mZXingScannerView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mZXingScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mZXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getText());
            SendFragment fragment = SendFragment.newInstance(jsonObject.getString("publicAddress"),jsonObject.getDouble("amount"));
            FragmentManager fm = getFragmentManager();
            Fragment fragmentParent;
            if(mIsParentSendFragment){
                fragmentParent = fm.findFragmentByTag(SendFragment.class.getCanonicalName());
            }else {
                fragmentParent = fm.findFragmentByTag(WalletFragment.class.getCanonicalName());
            }
            fm.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm
                    .beginTransaction()
                    .remove(fragmentParent)
                    .replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                    .commit();
            ((MainActivity)getActivity()).getBottomNavigationView().getMenu().getItem(3).setChecked(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
