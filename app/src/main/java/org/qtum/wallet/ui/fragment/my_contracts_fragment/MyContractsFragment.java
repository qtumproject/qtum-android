package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.qtum.wallet.R;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.contract_management_fragment.ContractManagementFragment;
import org.qtum.wallet.utils.DateCalculator;
import org.qtum.wallet.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public abstract class MyContractsFragment extends BaseFragment implements MyContractsView {

    private MyContractsPresenter mMyContractsPresenterImpl;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    protected ContractAdapter mContractAdapter;

    @BindView(R.id.place_holder)
    FontTextView mFontTextViewPlaceHolder;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context) {

        Bundle args = new Bundle();

        BaseFragment fragment = Factory.instantiateFragment(context, MyContractsFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mMyContractsPresenterImpl = new MyContractsPresenterImpl(this, new MyContractsInteractorImpl(getContext()));
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setPlaceHolder() {
        mFontTextViewPlaceHolder.setVisibility(View.VISIBLE);
    }

    @Override
    protected MyContractsPresenter getPresenter() {
        return mMyContractsPresenterImpl;
    }

    class ContractViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        FontTextView mTextViewTitle;

        @BindView(R.id.date)
        FontTextView mTextViewDate;

        @BindView(R.id.contract_type)
        FontTextView mTextViewContractType;

        @BindView(R.id.main_container)
        RelativeLayout mRelativeLayout;

        @BindView(R.id.ll_unsubscribe)
        LinearLayout mLinearLayoutUnsubscribe;

        Contract mContract;

        public ContractViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContract.isHasBeenCreated()) {
                        BaseFragment contractManagementFragment = ContractManagementFragment.newInstance(getContext(), mContract.getUiid(), mContract.getContractAddress());
                        openFragment(contractManagementFragment);
                    }
                }
            });
        }

        public void bindContract(Contract contract, final ContractItemListener contractItemListener) {
            mContract = contract;
            mLinearLayoutUnsubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contractItemListener.onUnsubscribeClick(mContract);
                }
            });
            if (contract.getDate() != null) {
                mTextViewDate.setText(DateCalculator.getShortDate(contract.getDate()));
            } else {
                mTextViewDate.setText(R.string.unconfirmed);
            }
            mTextViewTitle.setText(contract.getContractName());
            TinyDB tinyDB = new TinyDB(getContext());
            ContractTemplate contractTemplateByUiid = tinyDB.getContractTemplateByUiid(contract.getUiid());
            String contractType = contractTemplateByUiid.getContractType();
            mTextViewContractType.setText(contractType);
        }
    }

    protected class ContractAdapter extends RecyclerView.Adapter<ContractViewHolder> {

        List<Contract> mContractList;
        int mResId;
        ContractItemListener mContractItemListener;

        public ContractAdapter(List<Contract> contractList, int resId, ContractItemListener contractItemListener) {
            mContractList = contractList;
            mResId = resId;
            mContractItemListener = contractItemListener;
        }

        @Override
        public ContractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(mResId, parent, false);
            return new ContractViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContractViewHolder holder, int position) {
            holder.bindContract(mContractList.get(position),mContractItemListener);
        }

        @Override
        public int getItemCount() {
            return mContractList.size();
        }

        public void setContractList(List<Contract> contractList) {
            mContractList = contractList;
        }
    }

    @Override
    public void updateRecyclerView(List<Contract> contracts) {
        mContractAdapter.setContractList(contracts);
        mContractAdapter.notifyDataSetChanged();
    }
}
