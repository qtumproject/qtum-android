package com.pixelplex.qtum.ui.fragment.AddressListFragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FontEditText;


public class AddressListFragmentPresenter extends BaseFragmentPresenterImpl{

    private AddressListFragmentView mAddressListFragmentView;
    private AddressListFragmentInteractor mAddressListFragmentInteractor;
    private Context mContext;

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
        getView().updateAddressList(getInteractor().getKeyList(), new OnAddressClickListener() {
            @Override
            public void onAddressClick() {

            }
        });
    }

    private void showRestoreDialogFragment(String addressTo){
        View view = LayoutInflater.from(getView().getMainActivity()).inflate(R.layout.dialog_transfer_balance_fragment,null);
        FontEditText mEditTextAmount = (FontEditText)view.findViewById(R.id.et_amount);


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
