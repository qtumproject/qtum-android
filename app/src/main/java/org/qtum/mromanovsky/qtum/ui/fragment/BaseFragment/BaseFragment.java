package org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseContextView;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment implements BaseContextView{

    protected abstract void createPresenter();

    protected abstract BaseFragmentPresenterImpl getPresenter();

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume(getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPresenter();
        getPresenter().onCreate(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().onViewCreated();
        bindView(view);
    }


    @Override
    public void finish() {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {

    }

    @Override
    public void hideKeyBoard() {

    }

    @Override
    public void hideKeyBoard(View v) {

    }

    protected void bindView(View view) {
        ButterKnife.bind(this,view);
    }

    @Override
    public Context getContext() {
        return getActivity().getBaseContext();
    }
}
