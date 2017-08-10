package com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConfirmPurchaseDialogFragment extends AppCompatDialogFragment {

    public static final String CONTRACT = "CONTRACT";

    @BindView(R.id.contract_name)
    FontTextView tvContractName;

    @BindView(R.id.contract_type)
    FontTextView tvContractType;

    @BindView(R.id.price)
    FontTextView tvPrice;

    @BindView(R.id.miner_address)
    FontTextView tvMinerAddress;

    @OnClick(R.id.btn_cancel)
    public void onCancelClick(){
        dismiss();
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmClick(){
        if(listener != null){
            listener.onPurchaseConfirm();
        }
        dismiss();
    }

    PurchaseClickListener listener;

    public void setOnPurchaseListener(PurchaseClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lyt_confirm_purchase, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QstoreContract c = (QstoreContract) getArguments().getSerializable(CONTRACT);

        if(c != null) {
            tvContractName.setText(c.name);
            tvContractType.setText(c.type);
            tvPrice.setText(String.valueOf(c.price));
            tvMinerAddress.setText(c.publisherAddress);
        } else {
            dismiss();
        }
    }
}
