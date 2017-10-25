package org.qtum.wallet.ui.fragment.token_cash_management_fragment.light;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.qtum.wallet.R;
import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;
import org.qtum.wallet.ui.fragment.qtum_cash_management_fragment.AddressListPresenterImpl;
import org.qtum.wallet.ui.fragment.token_cash_management_fragment.AddressesListFragmentToken;
import org.qtum.wallet.ui.fragment.token_cash_management_fragment.TokenAddressesAdapter;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.CurrentNetParams;
import org.qtum.wallet.utils.FontTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public class AddressesListFragmentTokenLight extends AddressesListFragmentToken {
    @Override
    protected int getLayout() {
        return R.layout.fragment_address_list_light;
    }

    @Override
    public void updateAddressList(List<DeterministicKeyWithTokenBalance> deterministicKeyWithBalance, String currency) {
        if (mRecyclerView != null) {
            adapter = new TokenAddressesAdapter(deterministicKeyWithBalance, R.layout.item_address_light, this, currency, getPresenter().getDecimalUnits());
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(DeterministicKeyWithTokenBalance item) {
        List<DeterministicKeyWithTokenBalance> deterministicKeyWithBalances = new ArrayList<>(getPresenter().getKeysWithTokenBalance());
        deterministicKeyWithBalances.remove(item);
        showTransferDialogFragment(item, deterministicKeyWithBalances, getPresenter().getDecimalUnits());
    }

    protected void showTransferDialogFragment(final DeterministicKeyWithTokenBalance keyWithBalanceTo, List<DeterministicKeyWithTokenBalance> keyWithBalanceList, int decimalUnits) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_transfer_balance_fragment_light, null);

        final TextInputEditText mEditTextAmount = (TextInputEditText) view.findViewById(R.id.et_amount);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner_transfer);
        FontTextView mEditTextAddressTo = (FontTextView) view.findViewById(R.id.tv_address_to);

        mEditTextAddressTo.setText(keyWithBalanceTo.getKey().toAddress(CurrentNetParams.getNetParams()).toString());

        AddressesWithTokenBalanceSpinnerAdapterLight spinnerAdapter = new AddressesWithTokenBalanceSpinnerAdapterLight(getContext(), keyWithBalanceList, getPresenter().getCurrency(), decimalUnits);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPresenter().setKeyWithTokenBalanceFrom((DeterministicKeyWithTokenBalance) spinner.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        view.findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTransferDialog.dismiss();
            }
        });

        view.findViewById(R.id.bt_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressDialog();
                getPresenter().transfer(keyWithBalanceTo, getPresenter().getKeyWithTokenBalanceFrom(), mEditTextAmount.getText().toString());
            }
        });

        mTransferDialog = new AlertDialog
                .Builder(getContext())
                .setView(view)
                .create();

        if (mTransferDialog.getWindow() != null) {
            mTransferDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mTransferDialog.setCanceledOnTouchOutside(false);
        mTransferDialog.show();
    }
}
