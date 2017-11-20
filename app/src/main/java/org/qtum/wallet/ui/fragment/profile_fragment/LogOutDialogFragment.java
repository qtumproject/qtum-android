package org.qtum.wallet.ui.fragment.profile_fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class LogOutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog
                .Builder(getActivity())
                .setTitle(org.qtum.wallet.R.string.are_you_sure)
                .setPositiveButton(org.qtum.wallet.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OnYesClickListener onYesClickListener = (OnYesClickListener) getTargetFragment();
                        onYesClickListener.onClick();
                    }
                })
                .setNegativeButton(org.qtum.wallet.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
    }

    public interface OnYesClickListener {
        void onClick();
    }
}
