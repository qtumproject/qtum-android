package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseActivity;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment.StartPageFragment;


public class MainActivity extends BaseActivity implements MainActivityView{

    private static final int LAYOUT = R.layout.activity_main;
    private MainActivityPresenterImpl mMainActivityPresenterImpl;

    @Override
    protected void createPresenter() {
        mMainActivityPresenterImpl = new MainActivityPresenterImpl(this);
    }

    @Override
    protected BasePresenterImpl getPresenter() {
        return mMainActivityPresenterImpl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        StartPageFragment startPageFragment = StartPageFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main,startPageFragment,startPageFragment.getClass().getCanonicalName())
                .commit();
    }

}
