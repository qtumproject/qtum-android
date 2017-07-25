package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.ConstructorAdapter;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.SetYourTokenFragment;
import com.pixelplex.qtum.utils.FontEditText;
import com.pixelplex.qtum.utils.FontTextView;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;


public class SetYourTokenFragmentDark extends SetYourTokenFragment {

    private final int LAYOUT = R.layout.fragment_set_your_token;

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        getActivity().onBackPressed();
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void onContractConstructorPrepared(List<ContractMethodParameter> params) {
        adapter = new ConstructorAdapterDark(params, this);
        constructorList.setAdapter(adapter);
    }
}
