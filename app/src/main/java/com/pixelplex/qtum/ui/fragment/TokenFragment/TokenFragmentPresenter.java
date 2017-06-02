package com.pixelplex.qtum.ui.fragment.TokenFragment;

import android.content.Context;
import android.widget.Toast;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

/**
 * Created by kirillvolkov on 01.06.17.
 */

public class TokenFragmentPresenter extends BaseFragmentPresenterImpl {

    TokenFragmentView view;
    Context mContext;

    public TokenFragmentPresenter(TokenFragmentView view){
        this.view = view;
        this.mContext = getView().getContext();
    }

    @Override
    public TokenFragmentView getView() {
        return view;
    }

    public void onRefresh() {
        Toast.makeText(mContext,"Refreshing...", Toast.LENGTH_SHORT).show();
    }
}

