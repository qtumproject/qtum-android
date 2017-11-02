package org.qtum.wallet.ui.activity.main_activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.my_contracts_fragment.MyContractsFragment;
import org.qtum.wallet.utils.ThemeUtils;

import butterknife.ButterKnife;


public class WizardDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK) ? R.layout.dialog_fragment_wizard : R.layout.dialog_fragment_wizard_light,null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                final MyContractsFragment fragment = (MyContractsFragment) getTargetFragment();
                fragment.onWizardCanceled();
            }
        });
        ButterKnife.bind(this,view);
        Dialog dialog = new Dialog(getContext());
        if(dialog.getWindow()!=null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        return dialog;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getDialog()!=null)
        getDialog().dismiss();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}