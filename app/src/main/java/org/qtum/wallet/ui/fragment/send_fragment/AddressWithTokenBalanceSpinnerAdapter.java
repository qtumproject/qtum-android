package org.qtum.wallet.ui.fragment.send_fragment;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.utils.FontTextView;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class AddressWithTokenBalanceSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context mContext;
    private TokenBalance mTokenBalance;
    private String currency;
    private int decimalUnits;

    public AddressWithTokenBalanceSpinnerAdapter(@NonNull Context context, TokenBalance tokenBalance, String currency, int decimalUnits) {
        mContext = context;
        mTokenBalance = tokenBalance;
        this.currency = currency;
        this.decimalUnits = decimalUnits;
    }

    @Override
    public int getCount() {
        return mTokenBalance.getBalances().size();
    }

    @Override
    public Object getItem(int i) {
        return mTokenBalance.getBalances().get(i);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    public View getCustomViewDropDown(int position, @Nullable int resId, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resId, parent, false);
        FontTextView textViewAddress = (FontTextView) view.findViewById(R.id.address_name);
        final FontTextView textViewBalance = (FontTextView) view.findViewById(R.id.address_balance);
        final FontTextView textViewSymbol = (FontTextView) view.findViewById(R.id.address_symbol);
        textViewSymbol.setText(String.format(" %s", currency));
        String balance = (mTokenBalance.getBalances().get(position).getBalance() != null) ? String.valueOf(mTokenBalance.getBalances().get(position).getBalance().divide(new BigDecimal(Math.pow(10, decimalUnits)), MathContext.DECIMAL128)) : "0";
        textViewBalance.setLongNumberText(balance, textViewBalance.getContext().getResources().getDisplayMetrics().widthPixels / 2);
        textViewAddress.setText(mTokenBalance.getBalances().get(position).getAddress());
        return view;
    }

    public View getCustomView(int position, @Nullable int resId, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resId, parent, false);
        FontTextView textViewAddress = (FontTextView) view.findViewById(R.id.address_name);
        textViewAddress.setText(mTokenBalance.getBalances().get(position).getAddress());
        return view;
    }

    public TokenBalance getTokenBalance() {
        return mTokenBalance;
    }

    public void setTokenBalance(TokenBalance tokenBalance) {
        mTokenBalance = tokenBalance;
    }
}