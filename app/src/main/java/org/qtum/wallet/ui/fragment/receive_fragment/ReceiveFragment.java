package org.qtum.wallet.ui.fragment.receive_fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.ui.fragment.addresses_fragment.AddressesFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletFragment;
import org.qtum.wallet.utils.FontManager;

import java.io.File;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import static android.content.Context.CLIPBOARD_SERVICE;

public abstract class ReceiveFragment extends BaseFragment implements ReceiveView {

    private ReceivePresenter mReceivePresenter;

    private final int WRITE_EXTERNAL_STORAGE_CODE = 5;

    public static final String TOKEN_ADDRESS = "TOKEN_ADDRESS";
    public static final String TOKEN_BALANCE = "TOKEN_BALANCE";

    @BindView(org.qtum.wallet.R.id.iv_qr_code)
    ImageView mImageViewQrCode;
    @BindView(org.qtum.wallet.R.id.et_amount)
    TextInputEditText mTextInputEditTextAmount;
    @BindView(org.qtum.wallet.R.id.til_amount)
    TextInputLayout mTextInputLayoutAmount;
    @BindView(org.qtum.wallet.R.id.tv_single_string)
    TextView mTextViewAddress;
    @BindView(org.qtum.wallet.R.id.bt_copy_wallet_address)
    Button mButtonCopyWalletAddress;
    @BindView(org.qtum.wallet.R.id.bt_choose_another_address)
    Button mButtonChooseAnotherAddress;
    @BindView(org.qtum.wallet.R.id.ibt_back)
    ImageButton mImageButtonBack;
    @BindView(org.qtum.wallet.R.id.rl_receive)
    RelativeLayout mRelativeLayoutBase;
    @BindView(org.qtum.wallet.R.id.cl_receive)
    protected
    CoordinatorLayout mCoordinatorLayout;

    @BindView(org.qtum.wallet.R.id.qr_progress_bar)
    ProgressBar qrProgressBar;

    @BindView(org.qtum.wallet.R.id.not_confirmed_balance_view)
    View notConfirmedBalancePlaceholder;
    @BindView(org.qtum.wallet.R.id.tv_placeholder_balance_value)
    TextView placeHolderBalance;
    @BindView(org.qtum.wallet.R.id.tv_placeholder_not_confirmed_balance_value)
    TextView placeHolderBalanceNotConfirmed;

    QrCodePreview zoomDialog;
    private UpdateService mUpdateService;
    private int qrCodeColor = Color.BLACK;
    private int qrBackColor = Color.WHITE;

    @OnClick(org.qtum.wallet.R.id.iv_qr_code)
    public void onQrCodeClick() {
        rebuildDrawingCash();
        if (mImageViewQrCode.getDrawingCache() != null) {
            zoomDialog = QrCodePreview.newInstance(mImageViewQrCode.getDrawingCache());
            zoomDialog.show(getFragmentManager(), zoomDialog.getClass().getCanonicalName());
        }
    }

    @OnClick(org.qtum.wallet.R.id.bt_share)
    public void onShareClick() {
        if (checkPermissionWriteExternalStorage()) {
            chooseShareMethod();
        }
    }

    private void chooseShareMethod() {
        if (getQrCode() != null) {
            String pathofBmp = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), getQrCode(), "QTUM QRCode", "Share");
            if (TextUtils.isEmpty(pathofBmp)) {
                fixMediaDir();
                return;
            }
            Uri bmpUri = Uri.parse(pathofBmp);
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("image/png");
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Qtum QR-Code");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, bmpUri);
            getMainActivity().startActivity(Intent.createChooser(intentShareFile, "Qtum QR-Code"));
        }
    }

    void fixMediaDir() {
        File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard != null) {
            File mediaDir = new File(sdcard, "DCIM/Camera");
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getTokenBalance() == null) {
        getMainActivity().subscribeServiceConnectionChangeEvent(new MainActivity.OnServiceConnectionChangeListener() {
            @Override
            public void onServiceConnectionChange(boolean isConnecting) {
                if(isConnecting) {
                    mUpdateService = getMainActivity().getUpdateService();
                    mUpdateService.addBalanceChangeListener(mBalanceChangeListener);
                }
            }
        });
        } else {
            updateBalance(getTokenBalance(), null);
        }
    }

    BalanceChangeListener mBalanceChangeListener = new BalanceChangeListener() {
        @Override
        public void onChangeBalance(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
            getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getPresenter().onBalanceChanged(unconfirmedBalance, balance);
                }
            });
        }
    };

    @Override
    public void onPause() {
        if (zoomDialog != null) {
            zoomDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideKeyBoard();
        if (mUpdateService != null) {
            mUpdateService.removeBalanceChangeListener(mBalanceChangeListener);
        }
    }

    private boolean checkPermissionWriteExternalStorage() {
        if (getMainActivity().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return true;
        } else {
            getMainActivity().loadPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
            return false;
        }
    }

    @OnClick({org.qtum.wallet.R.id.bt_copy_wallet_address, org.qtum.wallet.R.id.bt_choose_another_address, org.qtum.wallet.R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.bt_copy_wallet_address:
                onCopyWalletAddressClick();
                break;
            case org.qtum.wallet.R.id.bt_choose_another_address:
                onChooseAnotherAddressClick();
                break;
            case org.qtum.wallet.R.id.ibt_back:
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                getActivity().onBackPressed();
                break;
        }
    }

    private void onChooseAnotherAddressClick() {
        BaseFragment addressesFragment = AddressesFragment.newInstance(getContext());
        openFragmentForResult(addressesFragment);
    }

    private void onCopyWalletAddressClick() {
        ClipboardManager clipboard = (ClipboardManager) getMainActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", getPresenter().getCurrentReceiveAddress());
        clipboard.setPrimaryClip(clip);
        showToast();
    }

    public static BaseFragment newInstance(Context context, String tokenAddress, String balance) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, ReceiveFragment.class);
        args.putString(TOKEN_BALANCE, balance);
        args.putString(TOKEN_ADDRESS, tokenAddress);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mReceivePresenter = new ReceivePresenterImpl(this, new ReceiveInteractorImpl(getContext()));
    }

    @Override
    protected ReceivePresenter getPresenter() {
        return mReceivePresenter;
    }

    @Override
    public void initializeViews() {
        mImageViewQrCode.setDrawingCacheEnabled(true);

        setQrColors(ContextCompat.getColor(getContext(), org.qtum.wallet.R.color.colorPrimary), mCoordinatorLayout.getDrawingCacheBackgroundColor());

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
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        setAlertDialog(R.string.error, throwable.getMessage(), R.string.cancel, PopUpType.error);
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

        mTextInputEditTextAmount.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        mTextInputLayoutAmount.setTypeface(FontManager.getInstance().getFont(getString(org.qtum.wallet.R.string.simplonMonoRegular)));

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
                        chooseShareMethod();
                    }
                }
            }
        });
    }

    protected void setQrColors(int crossColor, int qrColor) {
        this.qrCodeColor = qrColor;
        this.qrBackColor = crossColor;
    }


    @Override
    public void openFragmentForResult(Fragment fragment) {
        int code_response = 201;
        fragment.setTargetFragment(this, code_response);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(org.qtum.wallet.R.anim.enter_from_right, org.qtum.wallet.R.anim.exit_to_left, org.qtum.wallet.R.anim.enter_from_left, org.qtum.wallet.R.anim.exit_to_right)
                .add(org.qtum.wallet.R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public Bitmap getQrCode() {
        rebuildDrawingCash();
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
        if (getTargetFragment() != null) {
            ((WalletFragment) getTargetFragment()).updatePubKey(s);
        }
    }

    @Override
    public void showToast() {
        Toast.makeText(getContext(), org.qtum.wallet.R.string.copied, Toast.LENGTH_SHORT).show();
    }

    public void onChangeAddress() {
        getPresenter().changeAddress();
    }

    @Override
    public String getTokenBalance() {
        return getArguments().getString(TOKEN_BALANCE);
    }

    @Override
    public void updateBalance(String balance, String unconfirmedBalance) {
        placeHolderBalance.setText(balance);
        if (!TextUtils.isEmpty(unconfirmedBalance)) {
            notConfirmedBalancePlaceholder.setVisibility(View.VISIBLE);
            placeHolderBalanceNotConfirmed.setText(unconfirmedBalance);
        } else {
            notConfirmedBalancePlaceholder.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateBalance(String balance) {
        updateBalance(balance, null);
    }

    private void rebuildDrawingCash() {
        mImageViewQrCode.setDrawingCacheEnabled(false);
        mImageViewQrCode.setDrawingCacheEnabled(true);
        mImageViewQrCode.buildDrawingCache();
    }

    @Override
    public Subscription imageEncodeObservable(final String param) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                try {
                    return Observable.just(textToImageEncode(param));
                } catch (WriterException e) {
                    e.printStackTrace();
                    return Observable.error(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        if (bitmap != null) {
                            setQrCode(bitmap);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private Bitmap textToImageEncode(String Value) throws WriterException {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = getMainActivity().getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        int QRCodeWidth = displayMetrics.heightPixels / 4;
        getPresenter().setModuleWidth(QRCodeWidth);
        BitMatrix bitMatrix;
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 0);
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRCodeWidth, QRCodeWidth, hints
            );
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                boolean isDataPixel = bitMatrix.get(x, y);
                getPresenter().calcModuleWidth(isDataPixel, x, y);
                pixels[offset + x] = isDataPixel ? qrCodeColor : qrBackColor;
            }
        }

        if (getPresenter().getWithCrossQr()) {
            for (int i = getPresenter().getTopOffsetHeight(); i < bitMatrixHeight; i += getPresenter().getModuleWidth()) {

                int offset = i * bitMatrixWidth;
                for (int j = 0; j < bitMatrixWidth; j++) {
                    pixels[offset + j] = qrBackColor;
                    pixels[j * bitMatrixHeight + i] = qrBackColor;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, QRCodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    @Override
    public boolean isAmountValid(String amount) {
        return !TextUtils.isEmpty(amount);
    }

    @Override
    public boolean isTokenAddressValid(String addr) {
        return !TextUtils.isEmpty(addr);
    }

    @Override
    public boolean isUnconfirmedBalanceValid(String unconfirmedBalance) {
        return !TextUtils.isEmpty(unconfirmedBalance) && !unconfirmedBalance.equals("0");
    }
}