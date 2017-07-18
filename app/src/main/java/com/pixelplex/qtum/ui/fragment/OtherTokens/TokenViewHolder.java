package com.pixelplex.qtum.ui.fragment.OtherTokens;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.model.gson.tokenBalance.TokenBalance;
import com.pixelplex.qtum.dataprovider.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TokenViewHolder extends RecyclerView.ViewHolder implements TokenBalanceChangeListener {

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    @BindView(R.id.token_name)
    FontTextView tokenName;

    @BindView(R.id.token_balance)
    FontTextView tokenBalanceView;

    @BindView(R.id.spinner)
    ProgressBar spinner;

    private Token token;

    private UpdateSocketInstance socketInstance;

    public TokenViewHolder(View itemView, UpdateSocketInstance socketInstance, final OnTokenClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.socketInstance = socketInstance;

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTokenClick(getAdapterPosition());
            }
        });
    }

    public void bind (Token token) {

        if(this.token != null) {
            socketInstance.getSocketInstance().removeTokenBalanceChangeListener(token.getContractAddress());
        }

        this.token = token;

        tokenName.setText(token.getContractAddress());
        tokenBalanceView.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        socketInstance.getSocketInstance().addTokenBalanceChangeListener(token.getContractAddress(),this);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBalanceChange(final TokenBalance tokenBalance) {
        if(token.getContractAddress().equals(tokenBalance.getContractAddress())){
            token.setLastBalance(tokenBalance.getTotalBalance());
            rootLayout.post(new Runnable() {
                @Override
                public void run() {
                    spinner.setVisibility(View.GONE);
                    tokenBalanceView.setText(String.format("%f QTUM",tokenBalance.getTotalBalance()));
                    tokenBalanceView.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
