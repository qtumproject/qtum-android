package com.pixelplex.qtum.ui.fragment.ProcessingDialog.Light;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.ProcessingDialog.ProcessingDialogFragment;

import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class ProcessingDialogFragmentLight extends ProcessingDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.lyt_processing_dialog_light,null);
        ButterKnife.bind(this,view);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return dialog;
    }
}
