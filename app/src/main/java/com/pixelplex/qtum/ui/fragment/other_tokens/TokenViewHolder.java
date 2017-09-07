package com.pixelplex.qtum.ui.fragment.other_tokens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.model.gson.token_balance.TokenBalance;
import com.pixelplex.qtum.dataprovider.services.update_service.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.ui.fragment.token_fragment.TokenFragment;
import com.pixelplex.qtum.utils.ContractManagementHelper;
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

    @BindView(R.id.token_symbol)
    FontTextView mTextViewSymbol;

    @BindView(R.id.spinner)
    ProgressBar spinner;

    private Token token;
    private Context mContext;

    private UpdateSocketInstance socketInstance;

    public TokenViewHolder(View itemView, UpdateSocketInstance socketInstance, Context context, final OnTokenClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.socketInstance = socketInstance;
        mContext = context;
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

        ContractManagementHelper.getPropertyValue(TokenFragment.symbol, token, mContext, new ContractManagementHelper.GetPropertyValueCallBack() {
            @Override
            public void onSuccess(String value) {
                spinner.setVisibility(View.GONE);
                if(TextUtils.isEmpty(tokenBalanceView.getText().toString())){
                    tokenBalanceView.setVisibility(View.VISIBLE);
                    tokenBalanceView.setText("0.0");
                }
                mTextViewSymbol.setVisibility(View.VISIBLE);
                mTextViewSymbol.setText(String.format(" %s", value));
            }
        });

        tokenName.setText(token.getContractName());
        tokenBalanceView.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        socketInstance.getSocketInstance().addTokenBalanceChangeListener(token.getContractAddress(),this);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBalanceChange(final TokenBalance tokenBalance) {
        if (token.getContractAddress().equals(tokenBalance.getContractAddress())) {
            token.setLastBalance(tokenBalance.getTotalBalance());

            if (token.getDecimalUnits() == null) {
                ContractManagementHelper.getPropertyValue(TokenFragment.decimals, token, itemView.getContext(), new ContractManagementHelper.GetPropertyValueCallBack() {
                    @Override
                    public void onSuccess(String value) {
                        token = new TinyDB(itemView.getContext()).setTokenDecimals(token, Integer.valueOf(value));
                        updateBalance();
                    }
                });
            } else {
                updateBalance();
            }
        }
    }

    private void updateBalance() {
        rootLayout.post(new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.GONE);

                String s = token.getTokenBalanceWithDecimalUnits().toPlainString();

                tokenBalanceView.setText(String.valueOf(s));
                tokenBalanceView.setVisibility(View.VISIBLE);
            }
        });
    }
}
