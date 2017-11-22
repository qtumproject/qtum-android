package org.qtum.wallet.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.qtum.wallet.R;
import org.qtum.wallet.listener.QrCodeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.qtum.wallet.activity.MainActivity.ADDRESS;

/**
 * Created by kirillvolkov on 22.11.2017.
 */

public class QrCodeActivity extends WearableActivity implements QrCodeListener {

    @BindView(R.id.iv_qr_code)
    ImageView qrCode;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    BitmapTask task;

    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.bind(this);

        address = getIntent().getStringExtra(ADDRESS);

        task = new BitmapTask(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCode.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                qrCode.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                task.execute(address);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        task.cancel(false);
    }

    @Override
    public void onQrCodeReady(Bitmap bitmap) {
        progressBar.setVisibility(View.GONE);
        if (bitmap != null) {
            qrCode.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "Qr-Code build failed", Toast.LENGTH_SHORT).show();
        }
    }

    private class BitmapTask extends AsyncTask<String, Void, Bitmap> {

        QrCodeListener listener;

        public BitmapTask(QrCodeListener listener) {
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            for (String s : strings) {
                try {
                    if (!TextUtils.isEmpty(s)) {
                        return encodeAsBitmap(formatReceiveAddress(s));
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            listener.onQrCodeReady(bitmap);
        }

        public String formatReceiveAddress(String addr) {
            return String.format("qtum:%s?", addr);
        }

        Bitmap encodeAsBitmap(String str) throws WriterException {
            BitMatrix result;
            int qrcodeSize = qrCode.getWidth() * 4 / 5;
            try {
                result = new MultiFormatWriter().encode(str,
                        BarcodeFormat.QR_CODE, qrcodeSize, qrcodeSize, null);
            } catch (IllegalArgumentException iae) {
                // Unsupported format
                return null;
            }
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? getColor(R.color.background) : getColor(R.color.colorPrimary);
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, qrcodeSize, 0, 0, w, h);
            return bitmap;
        }
    }
}
