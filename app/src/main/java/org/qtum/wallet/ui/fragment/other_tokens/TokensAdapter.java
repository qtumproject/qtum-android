package org.qtum.wallet.ui.fragment.other_tokens;

import android.support.v7.widget.RecyclerView;

import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;

import java.util.List;

public abstract class TokensAdapter extends RecyclerView.Adapter<TokenViewHolder> {

    protected final UpdateSocketInstance socketInstace;
    protected List<Token> tokens;
    protected OnTokenClickListener listener;

    public TokensAdapter(List<Token> tokens, UpdateSocketInstance socketInstance, OnTokenClickListener listener) {
        this.tokens = tokens;
        this.socketInstace = socketInstance;
        this.listener = listener;
    }

    public Contract get(int adapterPosition) {
        return tokens.get(adapterPosition);
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }
}
