package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.lang.reflect.Field;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements MainActivityView {

    private static final int LAYOUT = R.layout.activity_main;
    private MainActivityPresenterImpl mMainActivityPresenterImpl;
    private static final int REQUEST_CAMERA = 0;

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
        //TODO:uncoment
        //Fabric.with(this, new Crashlytics());
        loadPermissions(Manifest.permission.CAMERA, REQUEST_CAMERA);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().processIntent(intent);
    }


    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void openRootFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(BaseFragment.BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(BaseFragment.BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack(BaseFragment.BACK_STACK_ROOT_TAG, 0);
    }

    @Override
    public void setIconChecked(int position) {
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    public void showBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void initializeViews() {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return getPresenter().onNavigationItemSelected(item);
            }
        });
    }

    public void setRootFragment(Fragment rootFragment) {
        getPresenter().setRootFragment(rootFragment);
    }
}
