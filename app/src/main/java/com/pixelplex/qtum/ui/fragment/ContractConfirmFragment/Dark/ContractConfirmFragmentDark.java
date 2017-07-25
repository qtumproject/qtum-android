package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment.Dark;

import android.support.v7.widget.LinearLayoutManager;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.ContractConfirmFragment.ContractConfirmAdapter;
import com.pixelplex.qtum.ui.fragment.ContractConfirmFragment.ContractConfirmFragment;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class ContractConfirmFragmentDark extends ContractConfirmFragment {

    public final int LAYOUT = R.layout.lyt_contract_confirm;

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        getActivity().onBackPressed();
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        presenter.setContractMethodParameterList((List<ContractMethodParameter>) getArguments().getSerializable(paramsKey));
        confirmList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContractConfirmAdapterDark(presenter.getContractMethodParameterList(),"4jhbr4hjb4l23342i4bn2kl4b2352l342k35bv235rl23","0.100", this);
        confirmList.setAdapter(adapter);
    }

}
