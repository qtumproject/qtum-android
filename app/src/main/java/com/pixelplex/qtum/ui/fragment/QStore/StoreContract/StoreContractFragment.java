package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.QStoreStorage;
import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.ContractManagementFragment.ContractManagementFragment;
import com.pixelplex.qtum.ui.fragment.QStore.QStoreFragment;
import com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Dialogs.ConfirmPurchaseDialogFragment;
import com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Dialogs.PurchaseClickListener;
import com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Dialogs.ViewSourceCodeDialogFragment;
import com.pixelplex.qtum.utils.DateCalculator;
import com.pixelplex.qtum.utils.FontButton;
import com.pixelplex.qtum.utils.FontTextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Dialogs.ViewSourceCodeDialogFragment.ABI;
import static com.pixelplex.qtum.ui.fragment.QStore.StoreContract.StoreContractPresenter.NON_PAID_STATUS;
import static com.pixelplex.qtum.ui.fragment.QStore.StoreContract.StoreContractPresenter.PAID_STATUS;
import static com.pixelplex.qtum.ui.fragment.QStore.StoreContract.StoreContractPresenter.PENDING_STATUS;


public class StoreContractFragment extends BaseFragment implements StoreContractView, TagClickListener, PurchaseClickListener {

    public static final String CONTRACT_ID = "CONTRACT_ID";

    private StoreContractPresenter presenter;

    private ConfirmPurchaseDialogFragment confirmPurchase;
    private ViewSourceCodeDialogFragment sourceCodeDialogFragment;

    public static StoreContractFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(CONTRACT_ID, id);
        StoreContractFragment fragment = new StoreContractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.tv_toolbar_title) public FontTextView toolbarTitle;
    @BindView(R.id.tv_cost) public FontTextView tvCost;
    @BindView(R.id.tv_description) public FontTextView tvDescription;
    @BindView(R.id.rv_tags) public RecyclerView rvTags;
    @BindView(R.id.tv_pub_date) public FontTextView tvPublishDate;
    @BindView(R.id.tv_size) public FontTextView tvSizeInBytes;
    @BindView(R.id.tv_compiled_on) public FontTextView tvCompiledOn;
    @BindView(R.id.tv_source_code) public FontTextView tvSourceCode;
    @BindView(R.id.tv_pub_by) public FontTextView tvPublishedBy;
    @BindView(R.id.tv_downloads) public FontTextView tvDownloads;

    @OnClick(R.id.ibt_back) public void onBackClick(){
        getActivity().onBackPressed();
    }

    @BindView(R.id.btn_view_source_code) FontButton btnViewSourceCode;

    @OnClick(R.id.btn_view_source_code)
    public void onViewSourceCodeClick(){
        presenter.getSourceCode();
    }

    @OnClick(R.id.tv_view_abi)
    public void onViewAbiClick(){
        presenter.getContractAbiById(presenter.getContract().id);
    }

    @OnClick(R.id.tv_view_details)
    public void onViewDetailsClick(){
        presenter.getDetails();
    }

    @BindView(R.id.btn_purchase)
    FontButton purchaseBtn;

    @OnClick(R.id.btn_purchase)
    public void onPurchaseClick(){
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConfirmPurchaseDialogFragment.CONTRACT, presenter.getContract());
        confirmPurchase = new ConfirmPurchaseDialogFragment();
        confirmPurchase.setArguments(arguments);
        confirmPurchase.setOnPurchaseListener(this);
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
        presenter.getContractById(getArguments().getString(CONTRACT_ID));
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .build();
        rvTags.setLayoutManager(chipsLayoutManager);
    }

    @Override
    public void setContractData(QstoreContract contract) {
        toolbarTitle.setText(contract.name);
        tvCost.setText(String.valueOf(contract.price));
        tvDescription.setText(contract.description);
        rvTags.setAdapter(new TagRecyclerViewAdapter(contract.tags, this));
        tvPublishDate.setText(DateCalculator.getShortDate(contract.creationDate));
        tvSizeInBytes.setText(String.valueOf(contract.sizeInBytes));
        tvCompiledOn.setText(contract.completedOn);
        tvSourceCode.setText(contract.withSourceCode? "Yes":"No");
        tvPublishedBy.setText(contract.publisherAddress);
        tvDownloads.setText(String.valueOf(contract.countDownloads));
    }

    @Override
    public void openAbiViewer(String abi) {
        Bundle args = new Bundle();
        args.putString(ABI,abi);
        sourceCodeDialogFragment = new ViewSourceCodeDialogFragment();
        sourceCodeDialogFragment.setArguments(args);
        sourceCodeDialogFragment.show(getFragmentManager(), sourceCodeDialogFragment.getClass().getCanonicalName());
    }

    @Override
    public void openDetails(String abi) {
        openFragment(ContractManagementFragment.newInstance(getContext(),abi));
    }

    @Override
    public void setContractPayStatus(String status) {
        purchaseBtn.setVisibility(View.VISIBLE);
        btnViewSourceCode.setVisibility(View.GONE);
        switch (status){
            case PAID_STATUS:
                purchaseBtn.setVisibility(View.GONE);
                btnViewSourceCode.setVisibility(View.VISIBLE);
                break;
            case NON_PAID_STATUS:
                purchaseBtn.setEnabled(true);
                break;
            case PENDING_STATUS:
                purchaseBtn.setEnabled(false);
                break;
        }
    }

    @Override
    public void onTagClick(String tag) {
        QStoreFragment invoker = (QStoreFragment)getTargetFragment();
        if(invoker != null) {
            invoker.setSearchTag(tag);
        }
        onBackClick();
    }

    @Override
    public void onPurchaseConfirm() {
        presenter.sendBuyRequest();
    }
}
