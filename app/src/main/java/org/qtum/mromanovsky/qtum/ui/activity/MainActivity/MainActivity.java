package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment.NewsFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment.ProfileFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;

import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements MainActivityView {

    private static final int LAYOUT = R.layout.activity_main;
    private MainActivityPresenterImpl mMainActivityPresenterImpl;
    private static final int REQUEST_CAMERA=0;
    Fragment fragment;
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

        loadPermissions(Manifest.permission.CAMERA ,REQUEST_CAMERA);

    }

    private void loadPermissions(String perm,int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void openFragment(Fragment fragment,Fragment fragment2) {
        getSupportFragmentManager().popBackStack(BaseFragment.BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if(fragment2 != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                    .remove(fragment2)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                    .commit();
        }
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getFragments()!=null) {
            for (Fragment frag : fm.getFragments()) {
                if (frag != null && frag.isVisible()) {
                    FragmentManager childFm = frag.getChildFragmentManager();
                    if (childFm.getBackStackEntryCount() > 0) {
                        childFm.popBackStack();
                        return;
                    }
                }
            }
        }
    }

    public void showBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    public BottomNavigationView getBottomNavigationView(){
        return mBottomNavigationView;
    }

    public void hideBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void initializeViews() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment currentFragment = fragment;
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
                        fragment = SendBaseFragment.newInstance(false);
                        break;
                    default:
                        return false;
                }

                getPresenter().openFragment(fragment,currentFragment);
                return true;
            }
        });
    }
}
