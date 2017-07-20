package com.pixelplex.qtum.ui.fragment.SendBaseFragment.QrCodeRecognitionFragment;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QrCodeRecognitionFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mZXingScannerView;

    @BindView(R.id.camera_container)
    FrameLayout mLinearLayout;

    public static QrCodeRecognitionFragment newInstance() {

        Bundle args = new Bundle();

        QrCodeRecognitionFragment fragment = new QrCodeRecognitionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qrcode_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mZXingScannerView = new ZXingScannerView(getContext());
        mZXingScannerView.setResultHandler(this);
        mLinearLayout.addView(mZXingScannerView);

        mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = mZXingScannerView.getLayoutParams();
                layoutParams.width = mLinearLayout.getWidth();
                layoutParams.height = mLinearLayout.getHeight();
                mZXingScannerView.setLayoutParams(layoutParams);
            }
        });
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

    public void dismiss(){
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    //qtum:QYxULw7ppJex4uhHoDQbW4jRjYP1vS2CEc?label=1234&message=mne&tokenAddress=1e6abee8af69f098aa354802164c79333623b252

    @Override
    public void handleResult(Result result) {

        String receiveAddr = "", tokenAddr = "", amount = "0.0";

        Pattern pattern = Pattern.compile("qtum:(.*?)\\?");
        Matcher matcher = pattern.matcher(result.getText());
        if (matcher.find())
           receiveAddr = matcher.group(1);

        pattern = Pattern.compile("tokenAddress=(.*?)$");
        matcher = pattern.matcher(result.getText());
        if (matcher.find())
            tokenAddr = matcher.group(1);

        pattern = Pattern.compile("amount=(.*?)&");
        matcher = pattern.matcher(result.getText());
        if (matcher.find())
            amount = matcher.group(1);

        if(!TextUtils.isEmpty(receiveAddr)) {
            ((SendBaseFragment) getTargetFragment()).onResponse(receiveAddr, Double.valueOf(amount), tokenAddr);
        } else {
            ((SendBaseFragment) getTargetFragment()).onResponseError();
        }
        dismiss();
    }

}