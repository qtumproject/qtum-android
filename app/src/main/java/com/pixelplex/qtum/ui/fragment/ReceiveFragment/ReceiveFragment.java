package com.pixelplex.qtum.ui.fragment.ReceiveFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.WalletFragment.WalletFragment;
import com.pixelplex.qtum.utils.FontManager;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public abstract class ReceiveFragment extends BaseFragment implements ReceiveFragmentView {

    private ReceiveFragmentPresenterImpl mReceiveFragmentPresenter;

    private final int WRITE_EXTERNAL_STORAGE_CODE = 5;

    public static final String TOKEN_ADDRESS = "TOKEN_ADDRESS";

    @BindView(R.id.iv_qr_code)
    ImageView mImageViewQrCode;
    @BindView(R.id.et_amount)
    TextInputEditText mTextInputEditTextAmount;
    @BindView(R.id.til_amount)
    TextInputLayout mTextInputLayoutAmount;
    @BindView(R.id.tv_single_string)
    TextView mTextViewAddress;
    @BindView(R.id.bt_copy_wallet_address)
    Button mButtonCopyWalletAddress;
    @BindView(R.id.bt_choose_another_address)
    Button mButtonChooseAnotherAddress;
    @BindView(R.id.ibt_back)
    ImageButton mImageButtonBack;
    @BindView(R.id.rl_receive)
    RelativeLayout mRelativeLayoutBase;
    @BindView(R.id.cl_receive)
    protected
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.qr_progress_bar)
    ProgressBar qrProgressBar;

    @BindView(R.id.not_confirmed_balance_view) View notConfirmedBalancePlaceholder;
    @BindView(R.id.tv_placeholder_balance_value) TextView placeHolderBalance;
    @BindView(R.id.tv_placeholder_not_confirmed_balance_value) TextView placeHolderBalanceNotConfirmed;

    QrCodePreview zoomDialog;

    @OnClick(R.id.iv_qr_code)
    public void onQrCodeClick() {
        mImageViewQrCode.setDrawingCacheEnabled(false);
        mImageViewQrCode.setDrawingCacheEnabled(true);
        mImageViewQrCode.buildDrawingCache();
        if (mImageViewQrCode.getDrawingCache() != null) {
            zoomDialog = QrCodePreview.newInstance(mImageViewQrCode.getDrawingCache());
            zoomDialog.show(getFragmentManager(), zoomDialog.getClass().getCanonicalName());
        }
    }

    @OnClick(R.id.bt_share)
    public void onShareClick() {
        if (checkPermissionWriteExternalStorage()) {
            getPresenter().chooseShareMethod();
        }
    }

    @Override
    public void onPause() {
        if (zoomDialog != null) {
            zoomDialog.dismiss();
        }
        super.onPause();

    }

    private boolean checkPermissionWriteExternalStorage() {
        if (getMainActivity().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return true;
        } else {
            getMainActivity().loadPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
            return false;
        }
    }

    @OnClick({R.id.bt_copy_wallet_address, R.id.bt_choose_another_address, R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_copy_wallet_address:
                getPresenter().onCopyWalletAddressClick();
                break;
            case R.id.bt_choose_another_address:
                getPresenter().onChooseAnotherAddressClick();
                break;
            case R.id.ibt_back:
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context, String tokenAddress) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, ReceiveFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mReceiveFragmentPresenter = new ReceiveFragmentPresenterImpl(this);
    }

    @Override
    protected ReceiveFragmentPresenterImpl getPresenter() {
        return mReceiveFragmentPresenter;
    }

    @Override
    public void initializeViews() {
        mImageViewQrCode.setDrawingCacheEnabled(true);
        getPresenter().setQrColors(ContextCompat.getColor(getContext(), R.color.colorPrimary), mCoordinatorLayout.getDrawingCacheBackgroundColor());
        mTextInputEditTextAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    getPresenter().changeAmount(mTextInputEditTextAmount.getText().toString());
                    mRelativeLayoutBase.requestFocus();
                    return false;
                }
                return false;
            }
        });

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                mTextInputEditTextAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                    }

                    @Override
                    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                        subscriber.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(final Editable s) {
                    }
                });
            }
        })
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String s) {
                        getPresenter().changeAmount(s);
                    }
                });

        mRelativeLayoutBase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    getPresenter().changeAmount(mTextInputEditTextAmount.getText().toString());
                    hideKeyBoard();
                }
            }
        });

        mTextInputEditTextAmount.setTypeface(FontManager.getInstance().getFont(getString(R.string.simplonMonoRegular)));
        mTextInputLayoutAmount.setTypeface(FontManager.getInstance().getFont(getString(R.string.simplonMonoRegular)));

        if (getArguments() != null) {
            String tokenAddr = getArguments().getString(TOKEN_ADDRESS, null);
            if (tokenAddr != null) {
                getPresenter().setTokenAddress(tokenAddr);
            }
        }

        getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        getPresenter().chooseShareMethod();
                    }
                }
            }
        });
    }

    @Override
    public void openFragmentForResult(Fragment fragment) {
        int code_response = 201;
        fragment.setTargetFragment(this, code_response);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public Bitmap getQrCode() {
        return mImageViewQrCode.getDrawingCache();
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void setQrCode(Bitmap bitmap) {
        if (bitmap != null && mImageViewQrCode != null) {
            hideSpinner();
            mImageViewQrCode.setImageBitmap(bitmap);
        }
    }

    @Override
    public void showSpinner() {
        qrProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSpinner() {
        qrProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUpAddress(String s) {
        mTextViewAddress.setText(s);
        ((WalletFragment) getTargetFragment()).updatePubKey(s);
    }

    @Override
    public void showToast() {
        Toast.makeText(getContext(), R.string.coped, Toast.LENGTH_SHORT).show();
    }

    public void onChangeAddress() {
        getPresenter().changeAddress();
    }

    @Override
    public void updateBalance(String balance, String unconfirmedBalance) {
        placeHolderBalance.setText(balance);
        if(!TextUtils.isEmpty(unconfirmedBalance)) {
            notConfirmedBalancePlaceholder.setVisibility(View.VISIBLE);
            placeHolderBalanceNotConfirmed.setText(unconfirmedBalance);
        } else {
            notConfirmedBalancePlaceholder.setVisibility(View.GONE);
        }
    }
}