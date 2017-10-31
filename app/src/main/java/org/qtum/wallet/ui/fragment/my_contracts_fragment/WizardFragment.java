package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.utils.ThemeUtils;

public class WizardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK)? org.qtum.wallet.R.layout.fragment_wizard : org.qtum.wallet.R.layout.fragment_wizard_light, null);
        return view;
    }
}
