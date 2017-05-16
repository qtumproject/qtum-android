package com.pixelplex.qtum.ui.fragment.ReceiveFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;
import com.pixelplex.qtum.ui.fragment.AddressesFragment.AddressesFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import static android.content.Context.CLIPBOARD_SERVICE;


class ReceiveFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ReceiveFragmentPresenter {

    private ReceiveFragmentView mReceiveFragmentView;
    private ReceiveFragmentInteractorImpl mReceiveFragmentInteractor;
    private String mAmount;

    ReceiveFragmentPresenterImpl(ReceiveFragmentView receiveFragmentView) {
        mReceiveFragmentView = receiveFragmentView;
        mReceiveFragmentInteractor = new ReceiveFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public ReceiveFragmentView getView() {
        return mReceiveFragmentView;
    }

    public ReceiveFragmentInteractorImpl getInteractor() {
        return mReceiveFragmentInteractor;
    }

    @Override
    public void changeAmount(String s) {
        JSONObject json = new JSONObject();
        mAmount = s;
        try {
            json.put("amount", s);
            json.put("publicAddress", getInteractor().getCurrentReceiveAddress());
            getView().setQrCode(textToImageEncode(json.toString()));
        } catch (JSONException | WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().hideKeyBoard();
    }

    @Override
    public void onCopyWalletAddressClick() {
        ClipboardManager clipboard = (ClipboardManager) getView().getFragmentActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", getInteractor().getCurrentReceiveAddress());
        clipboard.setPrimaryClip(clip);
        getView().showToast();
    }

    @Override
    public void onChooseAnotherAddressClick() {
        AddressesFragment addressesFragment = AddressesFragment.newInstance();
        getView().openFragmentForResult(addressesFragment);
    }



    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().setUpAddress(getInteractor().getCurrentReceiveAddress());
        getView().setBalance(getInteractor().getBalance());
        changeAmount("");
    }

    @Override
    public void changeAddress(){
        JSONObject json = new JSONObject();
        try {
            json.put("amount", mAmount);
            json.put("publicAddress", getInteractor().getCurrentReceiveAddress());
            getView().setQrCode(textToImageEncode(json.toString()));
        } catch (JSONException | WriterException e) {
            e.printStackTrace();
        }
        getView().setUpAddress(getInteractor().getCurrentReceiveAddress());
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
    }

    private Bitmap textToImageEncode(String Value) throws WriterException {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = getView().getFragmentActivity().getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        int QRCodeWidth = displayMetrics.heightPixels / 3;
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRCodeWidth, QRCodeWidth, null
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

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, QRCodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}