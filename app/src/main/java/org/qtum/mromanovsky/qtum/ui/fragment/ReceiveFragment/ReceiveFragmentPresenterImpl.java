package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class ReceiveFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ReceiveFragmentPresenter {

    private ReceiveFragmentView mReceiveFragmentView;

    public ReceiveFragmentPresenterImpl(ReceiveFragmentView receiveFragmentView) {
        mReceiveFragmentView = receiveFragmentView;
    }

    @Override
    public ReceiveFragmentView getView() {
        return mReceiveFragmentView;
    }

    @Override
    public void generateQrCode() {
        try {
            getView().setQrCode(TextToImageEncode("romanovsky"));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        int QRcodeWidth = 500;
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
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

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
