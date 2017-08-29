package com.pixelplex.qtum.ui.fragment.my_contracts_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.contract_management_fragment.ContractManagementFragment;
import com.pixelplex.qtum.utils.DateCalculator;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public abstract class MyContractsFragment extends BaseFragment implements MyContractsFragmentView {

    private MyContractsFragmentPresenter mMyContractsFragmentPresenter;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    protected ContractAdapter mContractAdapter;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
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
        mMyContractsFragmentPresenter = new MyContractsFragmentPresenter(this);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mMyContractsFragmentPresenter;
    }

    class ContractViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        FontTextView mTextViewTitle;

        @BindView(R.id.date)
        FontTextView mTextViewDate;

        @BindView(R.id.contract_type)
        FontTextView mTextViewContractType;

        Contract mContract;

        public ContractViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContract.isHasBeenCreated()) {
                        BaseFragment contractManagementFragment = ContractManagementFragment.newInstance(getContext(), mContract.getUiid(), mContract.getContractAddress());
                        openFragment(contractManagementFragment);
                    }
                }
            });
        }

        public void bindContract(Contract contract){
            mContract = contract;
            if(contract.getDate()!=null){
                mTextViewDate.setText(DateCalculator.getShortDate(contract.getDate()));
            }else{
                mTextViewDate.setText(R.string.unconfirmed);
            }
            mTextViewTitle.setText(contract.getContractName());
            TinyDB tinyDB = new TinyDB(getContext());
            ContractTemplate contractTemplateByUiid = tinyDB.getContractTemplateByUiid(contract.getUiid());
            String contractType = contractTemplateByUiid.getContractType();
            mTextViewContractType.setText(contractType);
        }
    }

    protected class ContractAdapter extends RecyclerView.Adapter<ContractViewHolder>{

        List<Contract> mContractList;
        int mResId;

        public ContractAdapter(List<Contract> contractList, int resId){
            mContractList = contractList;
            mResId = resId;
        }

        @Override
        public ContractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(mResId, parent, false);
            return new ContractViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContractViewHolder holder, int position) {
            holder.bindContract(mContractList.get(position));
        }

        @Override
        public int getItemCount() {
            return mContractList.size();
        }
    }

}
