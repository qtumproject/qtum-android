package com.pixelplex.qtum.ui.fragment.contract_management_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.contract_function_fragment.ContractFunctionFragment;
import com.pixelplex.qtum.utils.ContractManagementHelper;
import com.pixelplex.qtum.utils.FontTextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class ContractManagementFragment extends BaseFragment implements ContractManagementFragmentView{

    private ContractManagementFragmentPresenter mContractManagmentFragmentPresenter;
    private static final String CONTRACT_TEMPLATE_UIID = "contract_template_uiid";
    private static final String CONTRACT_ADDRESS = "contract_address";
    private static final String CONTRACT_ABI = "contract_abi";

    @BindView(R.id.methods_list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.tv_toolbar_profile)
    FontTextView titleView;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    protected MethodAdapter mMethodAdapter;
    protected String mContractAddress;

    public static BaseFragment newInstance(Context context, String contractTemplateUiid, String contractAddress) {
        Bundle args = new Bundle();
        args.putString(CONTRACT_TEMPLATE_UIID, contractTemplateUiid);
        args.putString(CONTRACT_ADDRESS, contractAddress);
        BaseFragment fragment = Factory.instantiateFragment(context, ContractManagementFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    public static BaseFragment newInstance(Context context, String abi) {
        Bundle args = new Bundle();
        args.putString(CONTRACT_ABI, abi);
        BaseFragment fragment = Factory.instantiateFragment(context, ContractManagementFragment.class);
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
    public void initializeViews() {
        super.initializeViews();
        mContractAddress = getArguments().getString(CONTRACT_ADDRESS);
        if(!TextUtils.isEmpty(mContractAddress)){
            getPresenter().getAbiFromFile();
        } else {
            titleView.setText(getString(R.string.contract_details));
            getPresenter().getAbiFromString(getArguments().getString(CONTRACT_ABI));
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public String getContractTemplateUiid() {
        return getArguments().getString(CONTRACT_TEMPLATE_UIID);
    }

    class MethodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.method_name)
        FontTextView mTextViewName;
        ContractMethod mContractMethod;

        @BindView(R.id.arrow)
        ImageView arrowIcon;

        MethodViewHolder(View itemView, boolean needToGetValue) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if(needToGetValue) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BaseFragment contractFunctionFragment = ContractFunctionFragment.newInstance(getContext(), mContractMethod.name, getContractTemplateUiid(), getArguments().getString(CONTRACT_ADDRESS));
                        openFragment(contractFunctionFragment);
                    }
                });
            }else {
                arrowIcon.setVisibility(View.GONE);
                itemView.setClickable(false);
            }
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

        boolean needToGetValue;

        PropertiesViewHolder(View itemView, boolean needToGetValue) {
            super(itemView);
            this.needToGetValue = needToGetValue;
            ButterKnife.bind(this, itemView);
        }

        void bindProperty(ContractMethod contractMethod){
            mTextViewPropertyName.setText(contractMethod.name);
            mContractMethod = contractMethod;
            if(needToGetValue) {
                ContractManagementHelper.getPropertyValue(mContractAddress, mContractMethod, new ContractManagementHelper.GetPropertyValueCallBack() {
                    @Override
                    public void onSuccess(String value) {
                        mProgressBar.setVisibility(View.GONE);
                        mTextViewPropertyValue.setVisibility(View.VISIBLE);
                        mTextViewPropertyValue.setText(value);
                    }
                });
            } else {
                mProgressBar.setVisibility(View.GONE);
                itemView.setClickable(false);
            }
        }

    }

    public class MethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ContractMethod> contractMethods;

        private final int TYPE_PROPERTY = 0;
        private final int TYPE_METHOD = 1;

        int mResIdProperty;
        int mResIdMethod;

        boolean needToGetValue;

        public MethodAdapter(List<ContractMethod> list, int resIdProperty, int resIdMethod, boolean needToGetValue){
            contractMethods = list;
            mResIdMethod = resIdMethod;
            mResIdProperty = resIdProperty;
            this.needToGetValue = needToGetValue;
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
                View view = layoutInflater.inflate(mResIdProperty, parent, false);
                return new PropertiesViewHolder(view, needToGetValue);
            } else if (viewType == TYPE_METHOD) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(mResIdMethod, parent, false);
                return new MethodViewHolder(view, needToGetValue);
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
