package com.pixelplex.qtum.ui.fragment.AddressesListFragmentToken;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;
import com.pixelplex.qtum.utils.FontTextView;
import java.util.List;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public abstract class AddressesWithTokenBalanceSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context mContext;
    private List<DeterministicKeyWithTokenBalance> mKeyWithBalanceList;
    String currency;

    public AddressesWithTokenBalanceSpinnerAdapter(@NonNull Context context, List<DeterministicKeyWithTokenBalance> keyWithBalanceList, String currency) {
        mContext = context;
        mKeyWithBalanceList = keyWithBalanceList;
        this.currency = currency;
    }

    @Override
    public int getCount() {
        return mKeyWithBalanceList.size();
    }

    @Override
    public Object getItem(int i) {
        return mKeyWithBalanceList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    public View getCustomView(int position, @Nullable int resId, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resId,parent,false);
        FontTextView textViewAddress = (FontTextView) view.findViewById(R.id.address_name);
        final FontTextView textViewBalance = (FontTextView) view.findViewById(R.id.address_balance);
        final FontTextView textViewSymbol = (FontTextView) view.findViewById(R.id.address_symbol);
        textViewSymbol.setText(String.format(" %s", currency));
        textViewBalance.setText((mKeyWithBalanceList.get(position).getBalance() != null)? String.valueOf(mKeyWithBalanceList.get(position).getBalance()) : "0");
        textViewAddress.setText(mKeyWithBalanceList.get(position).getAddress());

        return view;
    }
}
