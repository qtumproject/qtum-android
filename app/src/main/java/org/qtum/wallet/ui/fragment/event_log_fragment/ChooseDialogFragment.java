package org.qtum.wallet.ui.fragment.event_log_fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.subgraph.orchid.encoders.Hex;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.history.DisplayedData;
import org.qtum.wallet.utils.ThemeUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChooseDialogFragment extends DialogFragment {

    static final String MARGIN_TOP = "margin_top";
    static final String DATA = "data";

    @BindView(R.id.tv_hex)
    TextView mTextViewHex;
    @BindView(R.id.tv_address)
    TextView mTextViewAddress;
    @BindView(R.id.tv_number)
    TextView mTextViewNumber;
    @BindView(R.id.tv_text)
    TextView mTextViewText;
    @BindView(R.id.view_type_container)
    LinearLayout mViewTypeContainer;


    public static ChooseDialogFragment newInstance(int marginTop, String data) {

        Bundle args = new Bundle();
        args.putInt(MARGIN_TOP, marginTop);
        args.putString(DATA, data);
        ChooseDialogFragment fragment = new ChooseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK) ? R.layout.dialog_fragment_choose : R.layout.dialog_fragment_choose_light, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getContext());
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        return dialog;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getDialog() != null)
            getDialog().dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        Resources r = getResources();
        int marginTop = getArguments().getInt(MARGIN_TOP);
        int pxW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 116, r.getDisplayMetrics());
        int pxH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 134, r.getDisplayMetrics());
        int pxMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y - getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));
        if ((height - marginTop) < pxH) {
            marginTop = height - pxH;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pxW, pxH);
        params.setMargins(pxMargin, marginTop, 0, 0);
        mViewTypeContainer.setLayoutParams(params);
        final String data = getArguments().getString(DATA);

        mTextViewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newData = data.substring(24, 64);
                ((EventLogFragment) getTargetFragment()).onViewTypeChoose(new DisplayedData("Address", data, newData));
                getDialog().dismiss();
            }
        });
        mTextViewHex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EventLogFragment) getTargetFragment()).onViewTypeChoose(new DisplayedData("Hex", data, data));
                getDialog().dismiss();
            }
        });
        mTextViewNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newData = new BigInteger(Hex.decode(data)).toString();
                ((EventLogFragment) getTargetFragment()).onViewTypeChoose(new DisplayedData("Number", data, newData));
                getDialog().dismiss();
            }
        });
        mTextViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newData = "";
                try {
                    newData = new String(Hex.decode(data), "ASCII");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ((EventLogFragment) getTargetFragment()).onViewTypeChoose(new DisplayedData("Text", data, newData));
                getDialog().dismiss();
            }
        });

    }

}
