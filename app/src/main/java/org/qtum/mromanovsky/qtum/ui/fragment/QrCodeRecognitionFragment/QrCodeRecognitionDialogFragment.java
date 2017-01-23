package org.qtum.mromanovsky.qtum.ui.fragment.QrCodeRecognitionFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by max-v on 1/23/2017.
 */

public class QrCodeRecognitionDialogFragment extends DialogFragment implements ZXingScannerView.ResultHandler{

    ZXingScannerView mZXingScannerView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mZXingScannerView = new ZXingScannerView(getActivity().getBaseContext());
        mZXingScannerView.setResultHandler(this);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(mZXingScannerView)
                .create();
        return alertDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        mZXingScannerView.startCamera();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mZXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

    }
}
