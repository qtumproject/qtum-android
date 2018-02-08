package org.qtum.wallet.ui.fragment.profile_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.ui.base.base_nav_fragment.BaseNavFragment;
import org.qtum.wallet.ui.fragment.about_fragment.AboutFragment;
import org.qtum.wallet.ui.fragment.language_fragment.LanguageFragment;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;
import org.qtum.wallet.ui.fragment.smart_contracts_fragment.SmartContractsFragment;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;
import org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.SubscribeTokensFragment;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.ThemeUtils;

import butterknife.BindView;
import io.realm.Realm;

import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION_FOR_PASSPHRASE;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CHANGING;

public abstract class ProfileFragment extends BaseNavFragment implements ProfileView, OnSettingClickListener {

    protected PrefAdapter adapter;

    @BindView(org.qtum.wallet.R.id.pref_list)
    protected RecyclerView prefList;

    @BindView(R.id.tv_toolbar_profile)
    TextView mTextViewToolBar;

    protected ProfilePresenter mProfileFragmentPresenter;
    protected DividerItemDecoration dividerItemDecoration;
    private UpdateService mUpdateService;
    private LanguageChangeListener mLanguageChangeListener = new LanguageChangeListener() {
        @Override
        public void onLanguageChange() {
            resetText();
        }
    };

    public static ProfileFragment newInstance(Context context) {
        Bundle args = new Bundle();
        ProfileFragment fragment = (ProfileFragment) Factory.instantiateFragment(context, ProfileFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void activateTab() {
        getMainActivity().setIconChecked(1);
    }

    @Override
    public String getNavigationTag() {
        return ProfileFragment.class.getCanonicalName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().setIconChecked(1);
        getPresenter().setupLanguageChangeListener(mLanguageChangeListener);
    }

    @Override
    protected void createPresenter() {
        mProfileFragmentPresenter = new ProfilePresenterImpl(this, new ProfileInteractorImpl(getContext()));
    }

    @Override
    protected ProfilePresenter getPresenter() {
        return mProfileFragmentPresenter;
    }

    @Override
    public void onSettingClick(int key) {
        BaseFragment fragment = null;
        switch (key) {
            case org.qtum.wallet.R.string.language:
                fragment = LanguageFragment.newInstance(getContext());
                break;
            case org.qtum.wallet.R.string.change_pin:
                fragment = PinFragment.newInstance(CHANGING, getContext());
                break;
            case org.qtum.wallet.R.string.wallet_backup:
                fragment = PinFragment.newInstance(AUTHENTICATION_FOR_PASSPHRASE, getContext());
                break;
            case org.qtum.wallet.R.string.smart_contracts:
                fragment = SmartContractsFragment.newInstance(getContext());
                break;
            case org.qtum.wallet.R.string.subscribe_tokens:
                fragment = SubscribeTokensFragment.newInstance(getContext());
                break;
            case org.qtum.wallet.R.string.about:
                fragment = AboutFragment.newInstance(getContext());
                break;
            case org.qtum.wallet.R.string.log_out:
                setAlertDialog(getString(R.string.warning), getString(R.string.you_are_about_to_exit_your_account_all_account_data_will_be_erased_from_the_device_please_make_sure_you_have_saved_backups_of_your_passphrase_and_required_contracts), "Cancel", "Logout", PopUpType.error, new AlertDialogCallBack() {
                    @Override
                    public void onButtonClick() {
                    }

                    @Override
                    public void onButton2Click() {
                        onLogout();
                    }
                });
                break;
            case org.qtum.wallet.R.string.switch_theme:
                ThemeUtils.switchPreferencesTheme(getContext());
                getMainActivity().reloadActivity();
                break;
            default:
                break;
        }

        if (fragment != null) {
            addChild(fragment);
        }
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        prefList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().removeLanguageListener(mLanguageChangeListener);
    }

    @Override
    public void onSwitchChange(int key, boolean isChecked) {
        switch (key) {
            case org.qtum.wallet.R.string.touch_id:
                getPresenter().onTouchIdSwitched(isChecked);
                break;
            default:
                break;
        }
    }

    public void onLogout() {
        getMainActivity().onLogout();
        getPresenter().clearWallet();
        mUpdateService = getMainActivity().getUpdateService();
        mUpdateService.stopMonitoring();
        BaseFragment startPageFragment = StartPageFragment.newInstance(getContext());
        getMainActivity().openRootFragment(startPageFragment);
    }

    @Override
    public void startDialogFragmentForResult() {
        LogOutDialogFragment logOutDialogFragment = new LogOutDialogFragment();
        logOutDialogFragment.setTargetFragment(this, 200);
        logOutDialogFragment.show(getFragmentManager(), LogOutDialogFragment.class.getCanonicalName());
    }

    @Override
    public boolean checkAvailabilityTouchId() {
        return getMainActivity().checkAvailabilityTouchId();
    }

    @Override
    public void resetText() {
        adapter.notifyDataSetChanged();
        mTextViewToolBar.setText(R.string.profile);
    }

    @Override
    public int getRootView() {
        return R.id.fragment_container;
    }

    @Override
    public Realm getRealm() {
        return getMainActivity().getRealm();
    }
}
