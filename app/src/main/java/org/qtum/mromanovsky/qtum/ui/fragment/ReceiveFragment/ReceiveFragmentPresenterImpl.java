package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;
import org.qtum.mromanovsky.qtum.ui.fragment.AddressesFragment.AddressesFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import static android.content.Context.CLIPBOARD_SERVICE;


class ReceiveFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ReceiveFragmentPresenter {

    private ReceiveFragmentView mReceiveFragmentView;
    private ReceiveFragmentInteractorImpl mReceiveFragmentInteractor;

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
        try {
            json.put("amount", s);
            json.put("publicAddress", getInteractor().getCurrentReceiveAddress());
            getView().setQrCode(TextToImageEncode(json.toString()));
        } catch (JSONException | WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCopyWalletAddressClick() {
        ClipboardManager clipboard = (ClipboardManager) getView().getFragmentActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", getInteractor().getCurrentReceiveAddress());
        clipboard.setPrimaryClip(clip);
        //TODO : change notification type
        Toast.makeText(getView().getContext(), "Coped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChooseAnotherAddressClick() {
        AddressesFragment addressesFragment = AddressesFragment.newInstance();
        getView().openFragment(addressesFragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().setAddressInTV(getInteractor().getCurrentReceiveAddress());
        getView().setBalance(getInteractor().getBalance());
        changeAmount(null);
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {

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