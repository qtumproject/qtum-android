package com.pixelplex.qtum.ui.fragment.token_cash_management_fragment.Dark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;
import com.pixelplex.qtum.ui.fragment.token_cash_management_fragment.AddressesWithTokenBalanceSpinnerAdapter;

import java.util.List;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public class AddressesWithTokenBalanceSpinnerAdapterDark extends AddressesWithTokenBalanceSpinnerAdapter {

    public AddressesWithTokenBalanceSpinnerAdapterDark(@NonNull Context context, List<DeterministicKeyWithTokenBalance> keyWithBalanceList, String currency) {
        super(context, keyWithBalanceList, currency);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, R.layout.item_address_spinner, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, R.layout.item_address_spinner_dropdown, parent);
    }
}
