package com.pixelplex.qtum.ui.fragment.MyContractsFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractInfo;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.ContractManagementFragment.ContractManagementFragment;
import com.pixelplex.qtum.utils.DateCalculator;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by max-v on 6/2/2017.
 */

public class MyContractsFragment extends BaseFragment implements MyContractsFragmentView {

    MyContractsFragmentPresenter mMyContractsFragmentPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    ContractAdapter mContractAdapter;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static MyContractsFragment newInstance() {

        Bundle args = new Bundle();

        MyContractsFragment fragment = new MyContractsFragment();
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

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_contracts;
    }

    @Override
    public void updateRecyclerView(List<ContractInfo> contractInfoList) {
        mContractAdapter = new ContractAdapter(contractInfoList);
        mRecyclerView.setAdapter(mContractAdapter);
    }

    class ContractViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        FontTextView mTextViewTitle;

        @BindView(R.id.date)
        FontTextView mTextViewDate;

        @BindView(R.id.contract_type)
        FontTextView mTextViewContractType;

        ContractInfo mContractInfo;

        public ContractViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContractInfo.isHasBeenCreated()) {
                        ContractManagementFragment contractManagementFragment = ContractManagementFragment.newInstance(mContractInfo.getTemplateName(), mContractInfo.getContractAddress());
                        openFragment(contractManagementFragment);
                    }
                }
            });
        }

        public void bindContract(ContractInfo contractInfo){
            mContractInfo = contractInfo;
            if(contractInfo.getDate()!=null){
                mTextViewDate.setText(DateCalculator.getDate(contractInfo.getDate()*1000));
            }else{
                mTextViewDate.setText(R.string.not_confirmed);
            }
            mTextViewTitle.setText(contractInfo.getContractAddress().substring(0,8));
            mTextViewContractType.setText("(token)");
        }
    }

    class ContractAdapter extends RecyclerView.Adapter<ContractViewHolder>{

        List<ContractInfo> mContractInfoList;

        ContractAdapter(List<ContractInfo> contractInfoList){
            mContractInfoList = contractInfoList;
        }

        @Override
        public ContractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.lyt_contract_list_item, parent, false);
            return new ContractViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContractViewHolder holder, int position) {
            holder.bindContract(mContractInfoList.get(position));
        }

        @Override
        public int getItemCount() {
            return mContractInfoList.size();
        }
    }

}
