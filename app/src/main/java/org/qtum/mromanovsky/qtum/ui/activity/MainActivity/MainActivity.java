package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment.NewsFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment.ProfileFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SendFragment.SendFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements MainActivityView {

    private static final int LAYOUT = R.layout.activity_main;
    private MainActivityPresenterImpl mMainActivityPresenterImpl;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void createPresenter() {
        mMainActivityPresenterImpl = new MainActivityPresenterImpl(this);
    }

    @Override
    protected MainActivityPresenterImpl getPresenter() {
        return mMainActivityPresenterImpl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void openFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(BaseFragment.BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .commit();
    }

    public void showBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void initializeViews() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.item_wallet:
                        fragment = WalletFragment.newInstance();
                        break;
                    case R.id.item_profile:
                        fragment = ProfileFragment.newInstance();
                        break;
                    case R.id.item_news:
                        fragment = NewsFragment.newInstance();
                        break;
                    case R.id.item_send:
                        fragment = SendFragment.newInstance();
                        break;
                    default:
                        return false;
                }
                getPresenter().openFragment(fragment);
                return true;
            }
        });
    }
}
