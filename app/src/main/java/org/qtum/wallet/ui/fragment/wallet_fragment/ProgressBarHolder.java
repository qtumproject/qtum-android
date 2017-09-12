package org.qtum.wallet.ui.fragment.wallet_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public class ProgressBarHolder extends RecyclerView.ViewHolder{

    @BindView(org.qtum.wallet.R.id.progressBar)
    ProgressBar mProgressBar;

    public ProgressBarHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bindProgressBar(boolean mLoadingFlag){
        if(mLoadingFlag){
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}