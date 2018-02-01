package org.qtum.wallet.ui.base.base_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.fragment.processing_dialog.ProcessingDialogFragment;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.utils.FontButton;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements BaseFragmentView {

    protected abstract void createPresenter();

    protected abstract BaseFragmentPresenter getPresenter();

    protected abstract int getLayout();

    public static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private Unbinder mUnbinder;

    AlertDialog mAlertDialog;
    ProcessingDialogFragment mProcessingDialog;

    @Nullable
    @BindView(org.qtum.wallet.R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    public void setProgressDialog() {
        hideKeyBoard();
        mProcessingDialog = Factory.getProcessingDialog(getContext());
        mProcessingDialog.show(getFragmentManager(), mProcessingDialog.getClass().getCanonicalName());
    }

    @Override
    public void dismissProgressDialog() {
        if (mProcessingDialog != null) {
            mProcessingDialog.dismiss();
        }
    }

    public void onUserResume() {

    }

    protected void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public enum PopUpType {
        error, confirm
    }

    @Override
    public void setAlertDialog(String title, String buttonText, PopUpType type) {
        setAlertDialog(title, "", buttonText, type);
    }

    @Override
    public void setAlertDialog(@StringRes int titleResId, String message, @StringRes int buttonTextResId, PopUpType type) {
        setAlertDialog(getString(titleResId), message, getString(buttonTextResId), type);
    }

    @Override
    public void setAlertDialog(@StringRes int titleResId, @StringRes int buttonTextResId, PopUpType type) {
        setAlertDialog(getString(titleResId), "", getString(buttonTextResId), type);
    }

    @Override
    public void setAlertDialog(@StringRes int titleResId, String buttonText, PopUpType type) {
        setAlertDialog(getString(titleResId), "", buttonText, type);
    }

    @Override
    public void setAlertDialog(String title, String message, String buttonText, PopUpType popUpType) {
        setAlertDialog(title, message, buttonText, popUpType, null);
    }

    @Override
    public void setAlertDialog(@StringRes int titleResId, String message, String buttonText, PopUpType type) {
        setAlertDialog(getString(titleResId), message, buttonText, type, null);
    }

    @Override
    public void setAlertDialog(@StringRes int titleResId, @StringRes int messageResId, String buttonText, PopUpType type) {
        setAlertDialog(getString(titleResId), getString(messageResId), buttonText, type, null);
    }

    @Override
    public void setAlertDialog(@StringRes int titleResId, @StringRes int messageResId, @StringRes int buttonTextResId, PopUpType type) {
        setAlertDialog(getString(titleResId), getString(messageResId), getString(buttonTextResId), type, null);
    }

    @Override
    public void setAlertDialog(String title, String message, String buttonText, PopUpType type, final AlertDialogCallBack callBack) {
        try {
            dismissProgressDialog();
            dismissAlertDialog();
            View view = LayoutInflater.from(getContext()).inflate(ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK) ? org.qtum.wallet.R.layout.dialog_popup_fragment : org.qtum.wallet.R.layout.dialog_popup_fragment_light, null);

            FontTextView tvTitle = ((FontTextView) view.findViewById(org.qtum.wallet.R.id.tv_pop_up_title));
            tvTitle.setText(title);
            FontTextView tvMessage = ((FontTextView) view.findViewById(org.qtum.wallet.R.id.tv_pop_up_message));
            tvMessage.setText(message);
            FontButton popUpButton = ((FontButton) view.findViewById(org.qtum.wallet.R.id.bt_pop_up));
            view.findViewById(org.qtum.wallet.R.id.bt_pop_up2).setVisibility(View.GONE);

            ImageView icon = ((ImageView) view.findViewById(org.qtum.wallet.R.id.iv_icon));
            popUpButton.setText(buttonText);

            popUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlertDialog.cancel();
                    if (callBack != null) {
                        callBack.onButtonClick();
                    }
                }
            });

            if (ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK)) {
                switch (type.name()) {
                    case "error":
                        icon.setImageResource(org.qtum.wallet.R.drawable.ic_error);
                        view.findViewById(org.qtum.wallet.R.id.red_line).setVisibility(View.VISIBLE);
                        break;
                    case "confirm":
                        icon.setImageResource(org.qtum.wallet.R.drawable.ic_confirm);
                        view.findViewById(org.qtum.wallet.R.id.red_line).setVisibility(View.GONE);
                        break;
                }
            } else {
                switch (type.name()) {
                    case "error":
                        tvTitle.setTextColor(ContextCompat.getColor(getContext(), org.qtum.wallet.R.color.title_red_color));
                        icon.setImageResource(org.qtum.wallet.R.drawable.ic_popup_error_light);
                        break;
                    case "confirm":
                        icon.setImageResource(org.qtum.wallet.R.drawable.ic_confirmed_light);
                        break;
                }
            }

            mAlertDialog = new AlertDialog
                    .Builder(getContext())
                    .setView(view)
                    .create();

            if (mAlertDialog.getWindow() != null) {
                mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setAlertDialog(@StringRes int titleResId, String message, @StringRes int buttonTextResId, PopUpType type, final AlertDialogCallBack callBack) {
        setAlertDialog(getString(titleResId), message, getString(buttonTextResId), type, callBack);
    }

    @Override
    public void setAlertDialog(@StringRes int titleResId, String message, String buttonText, PopUpType type, final AlertDialogCallBack callBack) {
        setAlertDialog(getString(titleResId), message, buttonText, type, callBack);
    }

    @Override
    public void setAlertDialog(String title, String message, String buttonText, String buttonText2, PopUpType type, final AlertDialogCallBack callBack) {
        try {
            dismissProgressDialog();
            dismissAlertDialog();
            View view = LayoutInflater.from(getContext()).inflate(ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK) ? org.qtum.wallet.R.layout.dialog_popup_fragment : org.qtum.wallet.R.layout.dialog_popup_fragment_light, null);

            FontTextView tvTitle = ((FontTextView) view.findViewById(org.qtum.wallet.R.id.tv_pop_up_title));
            tvTitle.setText(title);
            FontTextView tvMessage = ((FontTextView) view.findViewById(org.qtum.wallet.R.id.tv_pop_up_message));
            tvMessage.setText(message);
            FontButton popUpButton = ((FontButton) view.findViewById(org.qtum.wallet.R.id.bt_pop_up));
            FontButton popUpButton2 = ((FontButton) view.findViewById(org.qtum.wallet.R.id.bt_pop_up2));
            view.findViewById(org.qtum.wallet.R.id.bt_pop_up2).setVisibility(View.VISIBLE);

            ImageView icon = ((ImageView) view.findViewById(org.qtum.wallet.R.id.iv_icon));
            popUpButton.setText(buttonText);
            popUpButton2.setText(buttonText2);

            popUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlertDialog.cancel();
                    if (callBack != null) {
                        callBack.onButtonClick();
                    }
                }
            });

            popUpButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlertDialog.cancel();
                    if (callBack != null) {
                        callBack.onButton2Click();
                    }
                }
            });

            if (ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK)) {
                switch (type.name()) {
                    case "error":
                        icon.setImageResource(org.qtum.wallet.R.drawable.ic_error);
                        view.findViewById(org.qtum.wallet.R.id.red_line).setVisibility(View.VISIBLE);
                        break;
                    case "confirm":
                        icon.setImageResource(org.qtum.wallet.R.drawable.ic_confirm);
                        view.findViewById(org.qtum.wallet.R.id.red_line).setVisibility(View.GONE);
                        break;
                }
            } else {
                switch (type.name()) {
                    case "error":
                        tvTitle.setTextColor(ContextCompat.getColor(getContext(), org.qtum.wallet.R.color.title_red_color));
                        icon.setImageResource(org.qtum.wallet.R.drawable.ic_popup_error_light);
                        break;
                    case "confirm":
                        icon.setImageResource(org.qtum.wallet.R.drawable.ic_confirmed_light);
                        break;
                }
            }

            mAlertDialog = new AlertDialog
                    .Builder(getContext())
                    .setView(view)
                    .create();

            if (mAlertDialog.getWindow() != null) {
                mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void hideBottomNavView(boolean recolorStatusBar) {
        ((MainActivity) getActivity()).hideBottomNavigationView(recolorStatusBar);
    }

    public void showBottomNavView(boolean recolorStatusBar) {
        ((MainActivity) getActivity()).showBottomNavigationView(recolorStatusBar);
    }

    public void hideBottomNavView(int colorRes) {
        ((MainActivity) getActivity()).hideBottomNavigationView(colorRes);
    }

    public void showBottomNavView(int colorRes) {
        ((MainActivity) getActivity()).showBottomNavigationView(colorRes);
    }

    @Override
    public void dismissAlertDialog() {
        if (mAlertDialog != null) {
            mAlertDialog.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyBoard();
        getPresenter().onPause();

        dismissProgressDialog();
        dismissAlertDialog();
    }

    @Override
    public void hideKeyBoard(View v) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPresenter();
        getPresenter().onCreate();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().initializeViews();
        getPresenter().getView().setSoftMode();
        getPresenter().onViewCreated();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        bindView(view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        getPresenter().onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().onDestroyView();
        unBindView();
    }

    @Override
    public void showSoftInput() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void setFocusTextInput(View textInputEditText, View textInputLayout) {
        textInputEditText.setFocusableInTouchMode(true);
        textInputEditText.requestFocus();
        textInputLayout.setFocusableInTouchMode(true);
        textInputLayout.requestFocus();
    }

    @Override
    public void finish() {
        ActivityCompat.finishAffinity(getActivity());
    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getActivity().startActivityForResult(intent, requestCode);
    }

    @Override
    public void hideKeyBoard() {
        Activity activity = getActivity();
        View view = activity.getCurrentFocus();
        if (view != null) {
            hideKeyBoard(activity, view);
        }
    }

    public void hideKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    @Override
    public void dismiss() {
        if (getMainActivity() != null && !getMainActivity().isFinishing()) {
            try {
                if(getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().remove(this).commit();
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void openRootFragment(Fragment fragment) {
        getFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        hideKeyBoard();
        getFragmentManager()
                .beginTransaction()
                .replace(org.qtum.wallet.R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(BaseFragment.BACK_STACK_ROOT_TAG)
                .commit();
    }


    @Override
    public void openFragment(Fragment fragment) {
        if(getFragmentManager().findFragmentByTag(fragment.getClass().getCanonicalName())==null) {
            hideKeyBoard();
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(org.qtum.wallet.R.anim.enter_from_right, org.qtum.wallet.R.anim.exit_to_left, org.qtum.wallet.R.anim.enter_from_left, org.qtum.wallet.R.anim.exit_to_right)
                    .add(org.qtum.wallet.R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void openFragmentForResult(Fragment fragment) {
        if(getFragmentManager().findFragmentByTag(fragment.getClass().getCanonicalName())==null) {
            hideKeyBoard();
            int code_response = 200;
            fragment.setTargetFragment(this, code_response);
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(org.qtum.wallet.R.anim.enter_from_right, org.qtum.wallet.R.anim.exit_to_left, org.qtum.wallet.R.anim.enter_from_left, org.qtum.wallet.R.anim.exit_to_right)
                    .add(org.qtum.wallet.R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void openFragmentWithBackStack(Fragment fragment, String tag) {
        hideKeyBoard();
        getFragmentManager()
                .beginTransaction()
                .replace(org.qtum.wallet.R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void initializeViews() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    protected void bindView(View view) {
        mUnbinder = ButterKnife.bind(this, view);
    }

    protected void unBindView() {
        mUnbinder.unbind();
    }

    @Override
    public void setSoftMode() {

    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    public interface AlertDialogCallBack {
        void onButtonClick();

        void onButton2Click();
    }
}
