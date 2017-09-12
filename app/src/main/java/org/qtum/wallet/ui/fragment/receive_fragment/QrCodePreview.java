package org.qtum.wallet.ui.fragment.receive_fragment;

/**
 * Created by kirillvolkov on 20.07.17.
 */

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by kirillvolkov on 20.07.17.
 */

public class QrCodePreview extends AppCompatDialogFragment {

    public static String QR_CODE_DATA = "QR_CODE_DATA";

    public static QrCodePreview newInstance(Bitmap bm) {
        Bundle args = new Bundle();
        QrCodePreview fragment = new QrCodePreview();
        args.putParcelable(QR_CODE_DATA,bm);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView view = new ImageView(getContext());
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(layoutParams == null){
            layoutParams = new ViewGroup.LayoutParams(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        }else {
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = getResources().getDisplayMetrics().heightPixels;
        }
        view.setLayoutParams(layoutParams);
        view.setImageBitmap((Bitmap)getArguments().getParcelable(QR_CODE_DATA));
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}