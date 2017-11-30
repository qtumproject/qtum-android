package org.qtum.wallet.ui.fragment.contract_function_fragment.contract_constant_function_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.contract_function_fragment.ParameterAdapter;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class ContractFunctionConstantFragment extends BaseFragment implements ContractFunctionConstantView {

    private ContractFunctionConstantPresenter mContractFunctionPresenterImpl;
    private final static String CONTRACT_TEMPLATE_UIID = "contract_template_uiid";
    private final static String METHOD_NAME = "method_name";
    private final static String CONTRACT_ADDRESS = "contract_address";

    @BindView(org.qtum.wallet.R.id.recycler_view)
    protected RecyclerView mParameterList;
    protected ParameterAdapter mParameterAdapter;
    @BindView(R.id.tv_query_result)
    TextView mTextViewQueryResult;
    @BindView(R.id.ll_query_result_container)
    LinearLayout mLinearLayoutQueryResultContainer;
    @BindView(R.id.bt_query)
    Button mButtonQuery;

    @OnClick({R.id.ibt_back, R.id.bt_query, R.id.ibt_reload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.bt_query:
            case R.id.ibt_reload:
                getPresenter().onQueryClick(mParameterAdapter.getParams(),getArguments().getString(CONTRACT_ADDRESS),getArguments().getString(METHOD_NAME));
                break;
        }
    }

    public static BaseFragment newInstance(Context context, String methodName, String uiid, String contractAddress) {
        Bundle args = new Bundle();
        args.putString(CONTRACT_TEMPLATE_UIID, uiid);
        args.putString(METHOD_NAME, methodName);
        args.putString(CONTRACT_ADDRESS, contractAddress);
        BaseFragment fragment = Factory.instantiateFragment(context, ContractFunctionConstantFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mContractFunctionPresenterImpl = new ContractFunctionConstantPresenterImpl(this, new ContractFunctionConstantInteractorImpl(getContext()));
    }

    @Override
    protected ContractFunctionConstantPresenter getPresenter() {
        return mContractFunctionPresenterImpl;
    }


    @Override
    public void initializeViews() {
        super.initializeViews();
        mParameterList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLinearLayoutQueryResultContainer.setVisibility(View.GONE);
        mButtonQuery.setVisibility(View.VISIBLE);
    }

    @Override
    public String getContractTemplateUiid() {
        return getArguments().getString(CONTRACT_TEMPLATE_UIID);
    }

    @Override
    public void updateQueryResult(String queryResult){
        mLinearLayoutQueryResultContainer.setVisibility(View.VISIBLE);
        mButtonQuery.setVisibility(View.GONE);
        mTextViewQueryResult.setText(queryResult);
    }

    @Override
    public String getMethodName() {
        return getArguments().getString(METHOD_NAME);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }
}
