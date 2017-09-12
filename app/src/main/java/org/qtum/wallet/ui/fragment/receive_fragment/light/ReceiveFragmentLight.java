package org.qtum.wallet.ui.fragment.receive_fragment.light;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.receive_fragment.ReceiveFragment;
import org.qtum.wallet.utils.FontManager;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class ReceiveFragmentLight extends ReceiveFragment {

    @BindView(R.id.til_amount)
    TextInputLayout mTilAmount;

    @BindView(R.id.et_amount)
    TextInputEditText mEtAmount;

    @Override
    protected int getLayout() {
        return R.layout.fragment_receive_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getPresenter().setQrColors(ContextCompat.getColor(getContext(),R.color.qr_code_background), ContextCompat.getColor(getContext(),R.color.qr_code_tint_color));
        mTilAmount.setTypeface(FontManager.getInstance().getFont(getResources().getString(R.string.proximaNovaRegular)));
        mEtAmount.setTypeface(FontManager.getInstance().getFont(getResources().getString(R.string.proximaNovaSemibold)));
    }


}
