package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.QrCodeRecognitionFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QrCodeRecognitionFragment extends Fragment implements ZXingScannerView.ResultHandler{

    public static QrCodeRecognitionFragment newInstance(){
        QrCodeRecognitionFragment qrCodeRecognitionFragment = new QrCodeRecognitionFragment();
        return qrCodeRecognitionFragment;
    }

    ZXingScannerView mZXingScannerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mZXingScannerView = new ZXingScannerView(getContext());
        mZXingScannerView.setResultHandler(this);
        return mZXingScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SendBaseFragment) getTargetFragment()).qrCodeRecognitionToolBar();
        mZXingScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SendBaseFragment) getTargetFragment()).sendToolBar();
        mZXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.getText());
            ((SendBaseFragment) getTargetFragment()).onResponse(jsonObject.getString("publicAddress"),
                    jsonObject.getDouble("amount"));
            getFragmentManager().beginTransaction().remove(this).commit();
            getFragmentManager().popBackStack();
        } catch (JSONException e) {
            try {
                ((SendBaseFragment) getTargetFragment()).onResponse(jsonObject.getString("publicAddress"),
                        0.0);
                getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().popBackStack();
            } catch (JSONException e1) {
                ((SendBaseFragment) getTargetFragment()).onResponseError();
                getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().popBackStack();
            }
        }
    }
}