package com.pixelplex.qtum.ui.fragment.AddressesFragment;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bitcoinj.crypto.DeterministicKey;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;
import com.pixelplex.qtum.utils.CurrentNetParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressesFragment extends BaseFragment implements AddressesFragmentView {

    private AddressesFragmentPresenterImpl mAddressesFragmentPresenter;
    private AddressAdapter mAddressAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static AddressesFragment newInstance() {

        Bundle args = new Bundle();

        AddressesFragment fragment = new AddressesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mAddressesFragmentPresenter = new AddressesFragmentPresenterImpl(this);
    }

    @Override
    protected AddressesFragmentPresenterImpl getPresenter() {
        return mAddressesFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_addresses;
    }

    @Override
    public void initializeViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void updateAddressList(List<DeterministicKey> deterministicKeys) {
        mAddressAdapter = new AddressAdapter(deterministicKeys);
        mRecyclerView.setAdapter(mAddressAdapter);
    }

    @Override
    public void setAdapterNull() {
        mAddressAdapter = null;
    }

    class AddressHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_single_string)
        TextView mTextViewAddress;
        @BindView(R.id.iv_check_indicator)
        ImageView mImageViewCheckIndicator;
        @BindView(R.id.ll_single_item)
        LinearLayout mLinearLayoutAddress;

        int defaultTextColor, selectedTextColor;

        AddressHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int oldPosition = KeyStorage.getInstance().getCurrentKeyPosition();
                    KeyStorage.getInstance().setCurrentKeyPosition(getAdapterPosition());
                    mAddressAdapter.notifyItemChanged(oldPosition);
                    mAddressAdapter.notifyItemChanged(getAdapterPosition());
                    ((ReceiveFragment) getTargetFragment()).onChangeAddress();
                }
            });
            ButterKnife.bind(this, itemView);

            defaultTextColor = ContextCompat.getColor(mTextViewAddress.getContext(), R.color.colorPrimary);
            selectedTextColor = ContextCompat.getColor(mTextViewAddress.getContext(), R.color.background);
        }

        void bindAddress(String address, int position) {
            if (position == KeyStorage.getInstance().getCurrentKeyPosition()) {
                mImageViewCheckIndicator.setVisibility(View.VISIBLE);
                mTextViewAddress.setTextColor(selectedTextColor);
                mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.accent_red_color));
            } else {
                mImageViewCheckIndicator.setVisibility(View.GONE);
                mTextViewAddress.setTextColor(defaultTextColor);
                mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.transparent));
            }
            mTextViewAddress.setText(address);
        }
    }

    private class AddressAdapter extends RecyclerView.Adapter<AddressHolder> {

        private List<DeterministicKey> mDeterministicKeys;
        private String mAddress;


        AddressAdapter(List<DeterministicKey> deterministicKeys) {
            mDeterministicKeys = deterministicKeys;
        }

        @Override
        public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_single_checkable, parent, false);
            return new AddressHolder(view);
        }

        @Override
        public void onBindViewHolder(AddressHolder holder, int position) {
            mAddress = mDeterministicKeys.get(position).toAddress(CurrentNetParams.getNetParams()).toString();
            holder.bindAddress(mAddress, position);
        }

        @Override
        public int getItemCount() {
            return mDeterministicKeys.size();
        }
    }
}