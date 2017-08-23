package com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.ui.fragment.QStore.StoreContract.StoreContractFragment;
import com.pixelplex.qtum.ui.fragment.QStore.StoreContract.TagRecyclerViewAdapter;
import com.pixelplex.qtum.ui.wave_visualizer.WaveHelper;
import com.pixelplex.qtum.ui.wave_visualizer.WaveView;
import com.pixelplex.qtum.utils.DateCalculator;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 18.08.17.
 */

public class StoreContractFragmentLight extends StoreContractFragment {


    @BindView(R.id.wave_view)
    WaveView waveView;
    private WaveHelper mWaveHelper;

    @Override
    protected int getLayout() {
        return R.layout.lyt_store_contract_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(waveView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

    @Override
    public void onPause() {
        mWaveHelper.cancel();
        super.onPause();
    }

    @OnClick(R.id.ibt_view_abi)
    public void onViewAbiClick(){
        getPresenter().getContractAbiById(getPresenter().getContract().id);
    }

    @Override
    public void setContractData(QstoreContract contract) {
        toolbarTitle.setText(contract.name);
        tvCost.setText(String.valueOf(contract.price));
        tvDescription.setText(contract.description);
        rvTags.setAdapter(new TagRecyclerViewAdapter(contract.tags, this, R.layout.item_tag_light));
        tvPublishDate.setText(DateCalculator.getShortDate(contract.creationDate));
        tvSizeInBytes.setText(String.valueOf(contract.sizeInBytes));
        tvCompiledOn.setText(contract.completedOn);
        tvSourceCode.setText(contract.withSourceCode? "Yes":"No");
        tvPublishedBy.setText(contract.publisherAddress);
        tvDownloads.setText(String.valueOf(contract.countDownloads));
    }

}
