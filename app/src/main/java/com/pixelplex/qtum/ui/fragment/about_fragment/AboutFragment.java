package com.pixelplex.qtum.ui.fragment.about_fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment_factory.Factory;

import butterknife.OnClick;

public abstract class AboutFragment extends BaseFragment implements AboutFragmentView{

    AboutFragmentPresenter mAboutFragmentPresenter;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context) {

        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, AboutFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mAboutFragmentPresenter = new AboutFragmentPresenter(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mAboutFragmentPresenter;
    }
}
