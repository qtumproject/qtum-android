package com.pixelplex.qtum.ui.fragment.AddressListFragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.DeterministicKeyWithBalance;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.CurrentNetParams;
import com.pixelplex.qtum.utils.FontTextView;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.ArrayList;
import java.util.List;


public class AddressListFragmentPresenter extends BaseFragmentPresenterImpl{

    private AddressListFragmentView mAddressListFragmentView;
    private AddressListFragmentInteractor mAddressListFragmentInteractor;
    private Context mContext;
    private List<DeterministicKeyWithBalance> mKeyWithBalanceList = new ArrayList<>();


    private AlertDialog mTransferDialog;

    AddressListFragmentPresenter(AddressListFragmentView addressListFragmentView){
        mAddressListFragmentView = addressListFragmentView;
        mContext = getView().getContext();
        mAddressListFragmentInteractor = new AddressListFragmentInteractor(mContext);
    }

    @Override
    public AddressListFragmentView getView() {
        return mAddressListFragmentView;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        for(DeterministicKey deterministicKey : getInteractor().getKeyList()){
            mKeyWithBalanceList.add(new DeterministicKeyWithBalance(deterministicKey));
        }
        getView().updateAddressList(mKeyWithBalanceList, new OnAddressClickListener() {
            @Override
            public void onItemClickClick(DeterministicKeyWithBalance deterministicKeyWithBalance) {
                showRestoreDialogFragment(deterministicKeyWithBalance, mKeyWithBalanceList);
            }
        });
    }

    private void showRestoreDialogFragment(DeterministicKeyWithBalance keyWithBalanceTo, List<DeterministicKeyWithBalance> keyWithBalanceList){
        View view = LayoutInflater.from(getView().getMainActivity()).inflate(R.layout.dialog_transfer_balance_fragment,null);
        TextInputEditText mEditTextAmount = (TextInputEditText)view.findViewById(R.id.et_amount);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_transfer);
        FontTextView mEditTextAddressTo = (FontTextView)view.findViewById(R.id.tv_address_to);

        mEditTextAddressTo.setText(keyWithBalanceTo.getKey().toAddress(CurrentNetParams.getNetParams()).toString());

        AddressWithBalanceSpinnerAdapter spinnerAdapter = new AddressWithBalanceSpinnerAdapter(mContext,keyWithBalanceList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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
        mTransferDialog = new AlertDialog
                .Builder(mContext)
                .setView(view)
                .create();

        if(mTransferDialog.getWindow() != null) {
            mTransferDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mTransferDialog.setCanceledOnTouchOutside(false);
        mTransferDialog.show();
    }

    public AddressListFragmentInteractor getInteractor() {
        return mAddressListFragmentInteractor;
    }
}
