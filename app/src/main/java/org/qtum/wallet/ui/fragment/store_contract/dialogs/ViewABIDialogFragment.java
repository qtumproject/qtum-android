package org.qtum.wallet.ui.fragment.store_contract.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.R;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewABIDialogFragment extends AppCompatDialogFragment {

    public static final String ABI = "CONTRACT_ABI";

    @OnClick(R.id.btn_cancel)
    public void onCancelClick(){
        dismiss();
    }

    @OnClick(R.id.btn_copy)
    public void onConfirmClick(){
        dismiss();
    }

    @BindView(R.id.source_code_tv)
    FontTextView tvSourceCode;

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
        View view = inflater.inflate(ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK)? R.layout.lyt_view_source_code_popup : R.layout.lyt_view_source_code_popup_light, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSourceCode.setText(getArguments().getString(ABI));
    }
}
