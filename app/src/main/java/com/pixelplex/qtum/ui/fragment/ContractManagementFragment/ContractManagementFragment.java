package com.pixelplex.qtum.ui.fragment.ContractManagementFragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContractManagementFragment extends BaseFragment implements ContractManagementFragmentView{

    ContractManagementFragmentPresenter mContractManagmentFragmentPresenter;
    private static final String CONTRACT_ADDRESS = "contract_address";

    @BindView(R.id.methods_list)
    RecyclerView mRecyclerView;

    public static ContractManagementFragment newInstance(String contractAddress) {
        
        Bundle args = new Bundle();
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
    protected BaseFragmentPresenterImpl getPresenter() {
        return mContractManagmentFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_contract_management;
    }

    class MethodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.method_name)
        FontTextView mTextViewName;

        MethodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        void bindMethod(ContractMethod contractMethod){
            mTextViewName.setText(contractMethod.name);
        }

    }

    class PropertiesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.propertiy_name)
        FontTextView mTextViewPropertyName;
        @BindView(R.id.property_value)
        FontTextView mTextViewPropertyValue;

        PropertiesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindProperty(ContractMethod contractMethod){
            mTextViewPropertyName.setText(contractMethod.name);
            //TODO get property with spinner
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
                return 0;
            } else{
                return 1;
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
