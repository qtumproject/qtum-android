package org.qtum.wallet.ui.fragment.store_contract;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.source_code.SourceCodeFragment;
import org.qtum.wallet.ui.fragment.store_contract.dialogs.ViewABIDialogFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.contract_management_fragment.ContractManagementFragment;
import org.qtum.wallet.ui.fragment.qstore.QStoreFragment;
import org.qtum.wallet.ui.fragment.store_contract.dialogs.ConfirmPurchaseDialogFragment;
import org.qtum.wallet.ui.fragment.store_contract.dialogs.PurchaseClickListener;
import org.qtum.wallet.utils.FontButton;
import org.qtum.wallet.utils.FontTextView;

import org.qtum.wallet.model.gson.qstore.PurchaseItem;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class StoreContractFragment extends BaseFragment implements StoreContractView, TagClickListener, PurchaseClickListener {

    public static final String CONTRACT_ID = "CONTRACT_ID";

    private StoreContractPresenter presenter;

    private ConfirmPurchaseDialogFragment confirmPurchase;
    private ViewABIDialogFragment ABIDialogFragment;

    public static BaseFragment newInstance(Context context, String id) {
        Bundle args = new Bundle();
        args.putString(CONTRACT_ID, id);
        BaseFragment fragment = Factory.instantiateFragment(context, StoreContractFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.tv_toolbar_title)
    public FontTextView toolbarTitle;
    @BindView(R.id.tv_cost)
    public FontTextView tvCost;
    @BindView(R.id.tv_description)
    public FontTextView tvDescription;
    @BindView(R.id.rv_tags)
    public RecyclerView rvTags;
    @BindView(R.id.tv_pub_date)
    public FontTextView tvPublishDate;
    @BindView(R.id.tv_size)
    public FontTextView tvSizeInBytes;
    @BindView(R.id.tv_compiled_on)
    public FontTextView tvCompiledOn;
    @BindView(R.id.tv_source_code)
    public FontTextView tvSourceCode;
    @BindView(R.id.tv_pub_by)
    public FontTextView tvPublishedBy;
    @BindView(R.id.tv_downloads)
    public FontTextView tvDownloads;

    @OnClick(R.id.ibt_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @BindView(R.id.btn_view_source_code)
    FontButton btnViewSourceCode;

    @OnClick(R.id.btn_view_source_code)
    public void onViewSourceCodeClick() {
        presenter.getSourceCode();
    }

    @OnClick(R.id.tv_view_details)
    public void onViewDetailsClick() {
        presenter.getDetails();
    }

    @BindView(R.id.btn_purchase)
    FontButton purchaseBtn;

    @OnClick(R.id.btn_purchase)
    public void onPurchaseClick() {
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
    protected StoreContractPresenter getPresenter() {
        return presenter;
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
    public void openSourceCode(String sourceCode) {
        BaseFragment sourceCodeFragment = SourceCodeFragment.newInstance(getContext(), sourceCode);
        openFragment(sourceCodeFragment);
    }

    @Override
    public void openAbiViewer(String abi) {
        Bundle args = new Bundle();
        args.putString(ViewABIDialogFragment.ABI, abi);
        ABIDialogFragment = new ViewABIDialogFragment();
        ABIDialogFragment.setArguments(args);
        ABIDialogFragment.show(getFragmentManager(), ABIDialogFragment.getClass().getCanonicalName());
    }

    @Override
    public void openDetails(String abi) {
        openFragment(ContractManagementFragment.newInstance(getContext(), abi));
    }

    @Override
    public void setContractPayStatus(String status) {
        purchaseBtn.setVisibility(View.VISIBLE);
        btnViewSourceCode.setVisibility(View.GONE);
        switch (status) {
            case PurchaseItem.PAID_STATUS:
                purchaseBtn.setVisibility(View.GONE);
                btnViewSourceCode.setVisibility(View.VISIBLE);
                break;
            case PurchaseItem.NON_PAID_STATUS:
                purchaseBtn.setEnabled(true);
                break;
            case PurchaseItem.PENDING_STATUS:
                purchaseBtn.setEnabled(false);
                break;
        }
    }

    @Override
    public void onTagClick(String tag) {
        QStoreFragment invoker = (QStoreFragment) getTargetFragment();
        if (invoker != null) {
            invoker.setSearchTag(tag);
        }
        onBackClick();
    }

    @Override
    public void onPurchaseConfirm() {
        presenter.sendBuyRequest();
    }

    @Override
    public void disablePurchase() {
        purchaseBtn.setEnabled(false);
    }
}
