package com.pixelplex.qtum.ui.FragmentFactory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.ProcessingDialog.Dark.ProcessingDialogFragmentDark;
import com.pixelplex.qtum.ui.fragment.ProcessingDialog.Light.ProcessingDialogFragmentLight;
import com.pixelplex.qtum.ui.fragment.ProcessingDialog.ProcessingDialogFragment;
import com.pixelplex.qtum.utils.ThemeUtils;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public class Factory {

    public static final String DARK_POSTFIX = "Dark";
    public static final String LIGHT_POSTFIX = "Light";

    public static BaseFragment instantiateFragment(Context context, Class fragment){
        return (BaseFragment) Fragment.instantiate(context,getThemedFargment(context, fragment));
    }

    public static BaseFragment instantiateFragment(Context context, Class fragment, Bundle bundle){
        return (BaseFragment) Fragment.instantiate(context,getThemedFargment(context, fragment), bundle);
    }

    private static String getThemedFargment(Context context, Class fragment){
        String postfix = (ThemeUtils.getCurrentTheme(context).equals(ThemeUtils.THEME_DARK)? DARK_POSTFIX : LIGHT_POSTFIX);
        String fullname = String.format("%s.%s.%s%s",fragment.getPackage().getName(),postfix ,fragment.getSimpleName(),postfix);
        return fullname;
    }

    public static ProcessingDialogFragment getProcessingDialog(Context context){
        return ThemeUtils.getCurrentTheme(context).equals(ThemeUtils.THEME_DARK)?
                new ProcessingDialogFragmentDark() :
                new ProcessingDialogFragmentLight();
    }

}
