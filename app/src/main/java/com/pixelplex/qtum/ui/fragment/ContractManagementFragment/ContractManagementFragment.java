package com.pixelplex.qtum.ui.fragment.ContractManagementFragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pixelplex.qtum.R;

import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.ContractFunctionFragment.ContractFunctionFragment;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ContractManagementFragment extends BaseFragment implements ContractManagementFragmentView{

    private ContractManagementFragmentPresenter mContractManagmentFragmentPresenter;
    private static final String CONTRACT_TEMPLATE_UIID = "contract_template_uiid";
    private static final String CONTRACT_ADDRESS = "contract_address";

    @BindView(R.id.methods_list)
    RecyclerView mRecyclerView;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    private MethodAdapter mMethodAdapter;
    private String mContractAddress;

    public static ContractManagementFragment newInstance(String contractTemplateUiid, String contractAddress) {
        
        Bundle args = new Bundle();
        args.putString(CONTRACT_TEMPLATE_UIID, contractTemplateUiid);
        args.putString(CONTRACT_ADDRESS, contractAddress);
        ContractManagementFragment fragment = new ContractManagementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mContractManagmentFragmentPresenter = new ContractManagementFragmentPresenter(this);
    }

    @Override
    protected ContractManagementFragmentPresenter getPresenter() {
        return mContractManagmentFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_contract_management;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mContractAddress = getArguments().getString(CONTRACT_ADDRESS);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setRecyclerView(List<ContractMethod> contractMethodList) {
        mMethodAdapter = new MethodAdapter(contractMethodList);
        mRecyclerView.setAdapter(mMethodAdapter);
    }

    @Override
    public String getContractTemplateUiid() {
        return getArguments().getString(CONTRACT_TEMPLATE_UIID);
    }

    class MethodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.method_name)
        FontTextView mTextViewName;
        ContractMethod mContractMethod;

        MethodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContractFunctionFragment contractFunctionFragment = ContractFunctionFragment.newInstance(mContractMethod.name,getContractTemplateUiid(),getArguments().getString(CONTRACT_ADDRESS));
                    openFragment(contractFunctionFragment);
                }
            });
        }

        void bindMethod(ContractMethod contractMethod){
            mContractMethod = contractMethod;
            mTextViewName.setText(contractMethod.name);
        }

    }

    class PropertiesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.propertiy_name)
        FontTextView mTextViewPropertyName;
        @BindView(R.id.property_value)
        FontTextView mTextViewPropertyValue;
        @BindView(R.id.progress_bar)
        ProgressBar mProgressBar;

        ContractMethod mContractMethod;

        PropertiesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindProperty(ContractMethod contractMethod){
            mTextViewPropertyName.setText(contractMethod.name);
            mContractMethod = contractMethod;
            getPresenter().getPropertyValue(mContractAddress, mContractMethod, new ContractManagementFragmentPresenter.GetPropertyValueCallBack() {
                @Override
                public void onSuccess(String value) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextViewPropertyValue.setVisibility(View.VISIBLE);
                    mTextViewPropertyValue.setText(value);
                }
            });
        }

    }

    public class MethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ContractMethod> contractMethods;

        private final int TYPE_PROPERTY = 0;
        private final int TYPE_METHOD = 1;

        MethodAdapter(List<ContractMethod> list){
            contractMethods = list;
        }

        @Override
        public int getItemViewType(int position) {
            if(contractMethods.get(position).constant){
                return TYPE_PROPERTY;
            } else{
                return TYPE_METHOD;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_PROPERTY) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.item_contract_property, parent, false);
                return new PropertiesViewHolder(view);
            } else if (viewType == TYPE_METHOD) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.item_contract_method, parent, false);
                return new MethodViewHolder(view);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof PropertiesViewHolder){
                ((PropertiesViewHolder) holder).bindProperty(contractMethods.get(position));
            } else if(holder instanceof MethodViewHolder){
                ((MethodViewHolder) holder).bindMethod(contractMethods.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return contractMethods.size();
        }
    }
}
