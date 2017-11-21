package org.qtum.wallet.ui.fragment_factory;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.processing_dialog.dark.ProcessingDialogFragmentDark;
import org.qtum.wallet.ui.fragment.processing_dialog.light.ProcessingDialogFragmentLight;
import org.qtum.wallet.ui.fragment.processing_dialog.ProcessingDialogFragment;
import org.qtum.wallet.utils.ThemeUtils;

public class Factory {

    public static final String DARK_POSTFIX = "Dark";
    public static final String LIGHT_POSTFIX = "Light";

    public static BaseFragment instantiateFragment(Context context, Class fragment) {
        return (BaseFragment) Fragment.instantiate(context, getThemedFargment(context, fragment));
    }

    public static Fragment instantiateDefaultFragment(Context context, Class fragment) {
        return Fragment.instantiate(context, getThemedFargment(context, fragment));
    }

    private static String getThemedFargment(Context context, Class fragment) {
        String postfix = (ThemeUtils.getCurrentTheme(context).equals(ThemeUtils.THEME_DARK) ? DARK_POSTFIX : LIGHT_POSTFIX);
        String fullname = String.format("%s.%s.%s%s", fragment.getPackage().getName(), postfix.toLowerCase(), fragment.getSimpleName(), postfix);
        return fullname;
    }

    public static ProcessingDialogFragment getProcessingDialog(Context context) {
        return ThemeUtils.getCurrentTheme(context).equals(ThemeUtils.THEME_DARK) ?
                new ProcessingDialogFragmentDark() :
                new ProcessingDialogFragmentLight();
    }
}
