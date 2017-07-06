package com.pixelplex.qtum.ui.fragment.ReceiveFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.Display;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import com.pixelplex.qtum.ui.fragment.AddressesFragment.AddressesFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.EnumMap;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;


public class ReceiveFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ReceiveFragmentPresenter {

    private ReceiveFragmentView mReceiveFragmentView;
    private ReceiveFragmentInteractorImpl mReceiveFragmentInteractor;
    private String mAmount;

    private DrawQRTask drawQRTask;

    private int qrCodeColor = Color.BLACK;
    private int qrBackColor = Color.WHITE;

    public void setQrColors(int crossColor, int qrColor){
        this.qrCodeColor = qrColor;
        this.qrBackColor = crossColor;
    }

    ReceiveFragmentPresenterImpl(ReceiveFragmentView receiveFragmentView) {
        mReceiveFragmentView = receiveFragmentView;
        mReceiveFragmentInteractor = new ReceiveFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public ReceiveFragmentView getView() {
        return mReceiveFragmentView;
    }

    private ReceiveFragmentInteractorImpl getInteractor() {
        return mReceiveFragmentInteractor;
    }

    @Override
    public void changeAmount(String s) {
        JSONObject json = new JSONObject();
        mAmount = s;
        try {
            json.put("amount", s);
            json.put("publicAddress", getInteractor().getCurrentReceiveAddress());
            getView().showSpinner();
            drawQRTask = new DrawQRTask();
            drawQRTask.execute(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().hideKeyBoard();
        drawQRTask.cancel(false);
    }

    @Override
    public void onCopyWalletAddressClick() {
        ClipboardManager clipboard = (ClipboardManager) getView().getMainActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", getInteractor().getCurrentReceiveAddress());
        clipboard.setPrimaryClip(clip);
        getView().showToast();
    }

    @Override
    public void onChooseAnotherAddressClick() {
        BaseFragment addressesFragment = AddressesFragment.newInstance(getView().getContext());
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
            getView().showSpinner();
            drawQRTask = new DrawQRTask();
            drawQRTask.execute(json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getView().setUpAddress(getInteractor().getCurrentReceiveAddress());
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
    }

    private int moduleWidth = 0;
    private boolean withCrossQR = true;

    public void setQRCrossing(boolean crossing) {
        withCrossQR = crossing;
    }

    class DrawQRTask extends AsyncTask<String, Bitmap, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            for (String param : params) {
                try {
                    return textToImageEncode(param);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                getView().setQrCode(bitmap);
            }
        }
    }

    private Bitmap textToImageEncode(String Value) throws WriterException {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = getView().getMainActivity().getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        int QRCodeWidth = displayMetrics.heightPixels / 3;
        moduleWidth = QRCodeWidth;
        BitMatrix bitMatrix;
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN,0);

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

                calcModuleWidth(isDataPixel, x, y);

                pixels[offset + x] = isDataPixel ?
                        qrCodeColor : qrBackColor;
            }
        }

        if(withCrossQR) {
            for (int i = topOffsetHeight; i < bitMatrixHeight; i += moduleWidth) {

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

    private int patternWidth = 0;
    private int topOffsetHeight = 0;
    private int leftOffcetWidth = 0;
    private boolean firstInputY, firstInputX = false;

    private void calcModuleWidth(boolean isDataPixel, int x, int y){
        if(isDataPixel) {

            if(!firstInputY) {
                topOffsetHeight = y;
                firstInputY = true;
            }

            if(!firstInputX) {
                leftOffcetWidth = x;
                firstInputX = true;
            }

            patternWidth ++;
        } else {
            if(patternWidth > 0) {
                moduleWidth = (moduleWidth > patternWidth)? patternWidth : moduleWidth;
                patternWidth = 0;
            }
        }
    }
}