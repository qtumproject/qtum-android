package com.pixelplex.qtum.ui.fragment.OtherTokens.Dark;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.OtherTokens.OnTokenClickListener;
import com.pixelplex.qtum.ui.fragment.OtherTokens.TokenViewHolder;
import com.pixelplex.qtum.ui.fragment.OtherTokens.TokensAdapter;
import com.pixelplex.qtum.ui.fragment.OtherTokens.UpdateSocketInstance;

import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class TokensAdapterDark extends TokensAdapter {

    public TokensAdapterDark(List<Token> tokens, UpdateSocketInstance socketInstance, OnTokenClickListener listener) {
        super(tokens, socketInstance, listener);
    }

    @Override
    public TokenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TokenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_token_list_item, parent, false), socketInstace,parent.getContext(), listener);
    }

    @Override
    public void onBindViewHolder(TokenViewHolder holder, int position) {
        holder.bind(tokens.get(position));
    }
}
