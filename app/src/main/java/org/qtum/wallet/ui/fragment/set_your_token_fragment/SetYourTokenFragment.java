package org.qtum.wallet.ui.fragment.set_your_token_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Button;
import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import org.qtum.wallet.utils.FontEditText;



import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public abstract class SetYourTokenFragment extends BaseFragment implements SetYourTokenFragmentView, OnValidateParamsListener {

    protected final static String CONTRACT_TEMPLATE_UIID = "mUiid";

    protected String templateUiid;

    protected ConstructorAdapter adapter;

    public static BaseFragment newInstance(Context context, String uiid) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, SetYourTokenFragment.class);
        args.putString(CONTRACT_TEMPLATE_UIID,uiid);
        fragment.setArguments(args);
        return fragment;
    }

    protected SetYourTokenFragmentPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    protected
    RecyclerView constructorList;


    @BindView(R.id.tv_template_name)
    FontEditText mTextViewTemplateName;

    @OnClick({R.id.ibt_back})
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @BindView(R.id.confirm)
    Button confirmBtn;

    @OnClick(R.id.confirm)
    public void onConfirmClick(){
        if(adapter != null) {
            String name = mTextViewTemplateName.getText().toString();
           if(adapter.validateMethods() && !name.isEmpty()) {
               presenter.confirm(adapter.getParams(), getArguments().getString(CONTRACT_TEMPLATE_UIID), name);
           }
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new SetYourTokenFragmentPresenterImpl(this);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        constructorList.setLayoutManager(new LinearLayoutManager(getContext()));
        templateUiid = getArguments().getString(CONTRACT_TEMPLATE_UIID);
        presenter.getConstructorByUiid(templateUiid);

        String templateName = "";
        TinyDB tinyDB = new TinyDB(getContext());
        List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();
        for(ContractTemplate contractTemplate : contractTemplateList){
            if(contractTemplate.getUuid().equals(templateUiid)) {
                templateName = contractTemplate.getName();
                break;
            }
        }

        mTextViewTemplateName.setText(templateName);
    }

    @Override
    public void onResume() {
        hideBottomNavView(false);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        showBottomNavView(false);
    }

    @Override
    public void onValidate() {
        confirmBtn.setEnabled(adapter.validateMethods());
    }
}
