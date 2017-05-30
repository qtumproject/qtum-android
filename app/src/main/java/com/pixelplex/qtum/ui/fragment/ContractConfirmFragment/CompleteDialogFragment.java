package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.pixelplex.qtum.R;

/**
 * Created by kirillvolkov on 29.05.17.
 */

public class CompleteDialogFragment extends DialogFragment {

    public CompleteDialogFragment(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog
                .Builder(getActivity())
                .setTitle(R.string.complete)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OnDialogClick listener = (OnDialogClick)getTargetFragment();
                        listener.onDialogClick();
                    }
                })
                .create();
    }

    public interface OnDialogClick {
        void onDialogClick();
    }
}
