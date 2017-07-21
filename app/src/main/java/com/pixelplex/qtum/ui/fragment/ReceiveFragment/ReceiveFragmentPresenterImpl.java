package com.pixelplex.qtum.ui.fragment.ReceiveFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.pixelplex.qtum.ui.fragment.AddressesFragment.AddressesFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.EnumMap;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;


class ReceiveFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ReceiveFragmentPresenter {

    private ReceiveFragmentView mReceiveFragmentView;
    private ReceiveFragmentInteractorImpl mReceiveFragmentInteractor;
    private String mAmount = "0.0";
    private String mTokenAddress;

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

    //qtum:qQMKtb8fs82ZTQwB1PWB8LDffoTKrNkK4z?amount=1.00000000&label=1234&message=mne&tokenAddress=....
    //qtum:QYxULw7ppJex4uhHoDQbW4jRjYP1vS2CEc?amount=2&label=1234&message=mne&tokenAddress=1e6abee8af69f098aa354802164c79333623b252

    @Override
    public void changeAmount(String s) {
        mAmount = s;
        String result = buildFullQrCodeData(getInteractor().getCurrentReceiveAddress(), mAmount, mTokenAddress);
            getView().showSpinner();
            drawQRTask = new DrawQRTask();
            drawQRTask.execute(result);
    }

    @Override
    public void setTokenAddress(String address) {
        mTokenAddress = address;
        String result = buildFullQrCodeData(getInteractor().getCurrentReceiveAddress(), mAmount, mTokenAddress);
            getView().showSpinner();
            drawQRTask = new DrawQRTask();
            drawQRTask.execute(result);
    }

    String getFormattedReceiveAddr(String addr){
        return String.format("qtum:%s?",addr);
    }

    String getFormattedAmount(String amount){
        if(!TextUtils.isEmpty(amount)) {
            return String.format("amount=%s&", amount);
        }else {
            return "";
        }
    }

    String getadditionalInfo(){
        return "label=1234&message=mne";
    }

    String getFormattedTokenAddr(String addr){
        if(!TextUtils.isEmpty(addr)) {
            return String.format("&tokenAddress=%s", addr);
        } else {
            return "";
        }
    }

    String buildFullQrCodeData(String receiveAddr, String amount, String mTokenAddress){
        return getFormattedReceiveAddr(receiveAddr) + getFormattedAmount(amount) + getadditionalInfo() + getFormattedTokenAddr(mTokenAddress);
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
        String result = buildFullQrCodeData(getInteractor().getCurrentReceiveAddress(), mAmount, mTokenAddress);
            getView().showSpinner();
            drawQRTask = new DrawQRTask();
            drawQRTask.execute(result);
        getView().setUpAddress(getInteractor().getCurrentReceiveAddress());
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
    }

    private int moduleWidth = 0;
    private boolean withCrossQR = false;

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
        int QRCodeWidth = displayMetrics.heightPixels / 4;
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

    public void chooseShareMethod(){
        if(getView().getQrCode() != null) {
            String pathofBmp = MediaStore.Images.Media.insertImage(getView().getContext().getContentResolver(), getView().getQrCode(), "QTUM QRCode", null);
            Uri bmpUri = Uri.parse(pathofBmp);
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("image/png");
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Qtum QR-Code");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, bmpUri);
            getView().getMainActivity().startActivity(Intent.createChooser(intentShareFile, "Qtum QR-Code"));
        }
    }
}