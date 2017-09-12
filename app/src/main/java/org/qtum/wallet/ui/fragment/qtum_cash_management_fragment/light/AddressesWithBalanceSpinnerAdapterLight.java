package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment.light;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.R;
import org.qtum.wallet.model.DeterministicKeyWithBalance;
import org.qtum.wallet.ui.fragment.qtum_cash_management_fragment.AddressesWithBalanceSpinnerAdapter;

import java.util.List;

/**
 * Created by kirillvolkov on 01.08.17.
 */

public class AddressesWithBalanceSpinnerAdapterLight extends AddressesWithBalanceSpinnerAdapter {

    public AddressesWithBalanceSpinnerAdapterLight(@NonNull Context context, List<DeterministicKeyWithBalance> keyWithBalanceList) {
        super(context, keyWithBalanceList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, R.layout.item_address_spinner_light, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, R.layout.item_address_spinner_dropdown_light, parent);
    }
}
