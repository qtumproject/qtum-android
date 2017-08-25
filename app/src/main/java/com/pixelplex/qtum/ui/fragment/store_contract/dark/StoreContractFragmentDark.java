package com.pixelplex.qtum.ui.fragment.store_contract.dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.qstore.QstoreContract;
import com.pixelplex.qtum.ui.fragment.store_contract.StoreContractFragment;
import com.pixelplex.qtum.ui.fragment.store_contract.TagRecyclerViewAdapter;
import com.pixelplex.qtum.utils.DateCalculator;

import butterknife.OnClick;

/**
 * Created by kirillvolkov on 18.08.17.
 */

public class StoreContractFragmentDark extends StoreContractFragment {

    @Override
    protected int getLayout() {
        return R.layout.lyt_store_contract;
    }

    @OnClick(R.id.tv_view_abi)
    public void onViewAbiClick(){
        getPresenter().getContractAbiById(getPresenter().getContract().id);
    }

    @Override
    public void setContractData(QstoreContract contract) {
        toolbarTitle.setText(contract.name);
        tvCost.setText(String.valueOf(contract.price));
        tvDescription.setText(contract.description);
        rvTags.setAdapter(new TagRecyclerViewAdapter(contract.tags, this, R.layout.item_tag));
        tvPublishDate.setText(DateCalculator.getShortDate(contract.creationDate));
        tvSizeInBytes.setText(String.valueOf(contract.sizeInBytes));
        tvCompiledOn.setText(contract.completedOn);
        tvSourceCode.setText(contract.withSourceCode? "Yes":"No");
        tvPublishedBy.setText(contract.publisherAddress);
        tvDownloads.setText(String.valueOf(contract.countDownloads));
    }

}
