package com.pixelplex.qtum.ui.fragment.OtherTokens;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.Token;

import java.util.List;


class TokensAdapter extends RecyclerView.Adapter<TokenViewHolder> {

    private final UpdateSocketInstance socketInstace;
    private List<Token> tokens;
    private OnTokenClickListener listener;

    public TokensAdapter(List<Token> tokens, UpdateSocketInstance socketInstance, OnTokenClickListener listener) {
        this.tokens = tokens;
        this.socketInstace = socketInstance;
        this.listener = listener;
    }

    public Contract get(int adapterPosition) {
        return tokens.get(adapterPosition);
    }

    @Override
    public TokenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TokenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_token_list_item, parent, false), socketInstace, listener);
    }

    @Override
    public void onBindViewHolder(TokenViewHolder holder, int position) {
        holder.bind(tokens.get(position));
    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }
}
