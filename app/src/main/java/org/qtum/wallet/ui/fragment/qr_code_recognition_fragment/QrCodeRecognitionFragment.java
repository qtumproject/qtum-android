package org.qtum.wallet.ui.fragment.qr_code_recognition_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.google.zxing.Result;

import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeRecognitionFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mZXingScannerView;

    @BindView(org.qtum.wallet.R.id.camera_container)
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
        return inflater.inflate(org.qtum.wallet.R.layout.fragment_qrcode_camera, container, false);
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
        ((SendFragment) getTargetFragment()).qrCodeRecognitionToolBar();
        mZXingScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SendFragment) getTargetFragment()).sendToolBar();
        mZXingScannerView.stopCamera();
    }

    public void dismiss() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void handleResult(Result result) {
        String receiveAddr = "", tokenAddr = "", amount = "0.0";

        Pattern pattern = Pattern.compile("qtum:(.*?)\\?");
        Matcher matcher = pattern.matcher(result.getText());
        if (matcher.find()) {
            receiveAddr = matcher.group(1);
        } else {
            pattern = Pattern.compile("qtum:(.*?)$");
            matcher = pattern.matcher(result.getText());
            if (matcher.find()) {
                receiveAddr = matcher.group(1);
            }
        }

        pattern = Pattern.compile("tokenAddress=(.*?)$");
        matcher = pattern.matcher(result.getText());
        if (matcher.find())
            tokenAddr = matcher.group(1);

        pattern = Pattern.compile("amount=(.*?)&");
        matcher = pattern.matcher(result.getText());
        if (matcher.find())
            amount = matcher.group(1);

        if (!TextUtils.isEmpty(receiveAddr)) {
            try {
                Double.valueOf(amount);
            } catch(Exception e) {
                amount = "0.0";
            }
            ((SendFragment) getTargetFragment()).onResponse(receiveAddr, Double.valueOf(amount), tokenAddr);
        } else {
            ((SendFragment) getTargetFragment()).onResponseError();
        }
        dismiss();
    }
}