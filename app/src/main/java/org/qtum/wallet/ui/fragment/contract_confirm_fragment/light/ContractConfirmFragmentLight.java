package org.qtum.wallet.ui.fragment.contract_confirm_fragment.light;

import android.support.v7.widget.LinearLayoutManager;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.ui.fragment.contract_confirm_fragment.ContractConfirmFragment;
import java.util.List;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class ContractConfirmFragmentLight extends ContractConfirmFragment {

    public final int LAYOUT = R.layout.lyt_contract_confirm_light;

//    @OnClick(R.id.test_click)
//    public void onTestClick(){
//        presenter.testClick();
//    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        presenter.setContractMethodParameterList((List<ContractMethodParameter>) getArguments().getSerializable(paramsKey));
        confirmList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContractConfirmAdapterLight(presenter.getContractMethodParameterList(),"4jhbr4hjb4l23342i4bn2kl4b2352l342k35bv235rl23","0.100", this);
        confirmList.setAdapter(adapter);
    }

}
