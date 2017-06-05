package com.pixelplex.qtum.ui.fragment.ContractManagementFragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.QtumService;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.CallSmartContractRequest;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.CallSmartContractResponse;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.ContractFunctionFragment.ContractFunctionFragment;
import com.pixelplex.qtum.utils.FontTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContractManagementFragment extends BaseFragment implements ContractManagementFragmentView{

    ContractManagementFragmentPresenter mContractManagmentFragmentPresenter;
    private static final String CONTRACT_TEMPLATE_NAME = "contract_template_name";
    private static final String CONTRACT_ADDRESS = "contract_address";

    @BindView(R.id.methods_list)
    RecyclerView mRecyclerView;

    MethodAdapter mMethodAdapter;

    public static ContractManagementFragment newInstance(String contractTemplateName, String contractAddress) {
        
        Bundle args = new Bundle();
        args.putString(CONTRACT_TEMPLATE_NAME, contractTemplateName);
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

    @Override
    public void initializeViews() {
        super.initializeViews();

//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put("18160ddd");
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("hashes", jsonArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONStringer jsonStringer = new JSONStringer();
//        try {
//            jsonStringer.object().key("hashes").array().value("18160ddd").endArray().endObject();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String[] hashes = new String[]{"18160ddd"};

        QtumService.newInstance().callSmartContract(getArguments().getString(CONTRACT_ADDRESS), new CallSmartContractRequest(hashes))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String s = e.toString();
                        int i = 2;
                    }

                    @Override
                    public void onNext(JSONObject callSmartContractResponse) {
                        JSONObject s = callSmartContractResponse;
                        int i = 2;
                    }
                });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setRecyclerView(List<ContractMethod> contractMethodList) {
        mMethodAdapter = new MethodAdapter(contractMethodList);
        mRecyclerView.setAdapter(mMethodAdapter);
    }

    @Override
    public String getContractTemplateName() {
        return getArguments().getString(CONTRACT_TEMPLATE_NAME);
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
                    ContractFunctionFragment contractFunctionFragment = ContractFunctionFragment.newInstance(mContractMethod.name,getContractTemplateName());
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
