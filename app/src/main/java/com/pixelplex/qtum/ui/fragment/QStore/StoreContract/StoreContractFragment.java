package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Dialogs.ConfirmPurchaseDialogFragment;
import com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Dialogs.ViewSourceCodeDialogFragment;
import com.pixelplex.qtum.utils.FontButton;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.OnClick;


public class StoreContractFragment extends BaseFragment implements StoreContractView {

    StoreContractPresenter presenter;

    ConfirmPurchaseDialogFragment confirmPurchase;
    ViewSourceCodeDialogFragment sourceCodeDialogFragment;

    public static StoreContractFragment newInstance() {
        Bundle args = new Bundle();
        StoreContractFragment fragment = new StoreContractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.ibt_back)
    public void onBackClick(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.tv_view_details)
    public void onViewDetailsClick(){

    }

    @OnClick(R.id.tv_view_source_code)
    public void onViewSourceCodeClick(){
        sourceCodeDialogFragment = new ViewSourceCodeDialogFragment();
        sourceCodeDialogFragment.show(getFragmentManager(), sourceCodeDialogFragment.getClass().getCanonicalName());
    }

    @OnClick(R.id.btn_purchase)
    public void onPurchaseClick(){
        confirmPurchase = new ConfirmPurchaseDialogFragment();
        confirmPurchase.show(getFragmentManager(), confirmPurchase.getClass().getCanonicalName());
    }

    @Override
    protected void createPresenter() {
        presenter = new StoreContractPresenter(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.lyt_store_contract;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
    }

}
