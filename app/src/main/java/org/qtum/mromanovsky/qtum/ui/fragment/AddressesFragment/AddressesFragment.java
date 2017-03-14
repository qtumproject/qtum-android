package org.qtum.mromanovsky.qtum.ui.fragment.AddressesFragment;


import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.utils.CurrentNetParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressesFragment extends BaseFragment implements AddressesFragmentView{

    public final int LAYOUT = R.layout.fragment_addresses;
    AddressesFragmentPresenterImpl mAddressesFragmentPresenter;
    AddressAdapter mAddressAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static AddressesFragment newInstance(){
        AddressesFragment addressesFragment = new AddressesFragment();
        return  addressesFragment;
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
        return LAYOUT;
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

    public class AddressHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_address)
        TextView mTextViewAddress;
        @BindView(R.id.iv_check_indicator)
        ImageView mImageViewCheckIndicator;
        @BindView(R.id.ll_address)
        LinearLayout mLinearLayoutAddress;

        AddressHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int s = KeyStorage.getInstance().getCurrentKeyPosition();
                    KeyStorage.getInstance().setCurrentKeyPosition(getAdapterPosition());
                    mAddressAdapter.notifyItemChanged(s);
                    mAddressAdapter.notifyItemChanged(getAdapterPosition());
                }
            });
            ButterKnife.bind(this,itemView);
        }

        void bindAddress(String address, int position){
            if(position == KeyStorage.getInstance().getCurrentKeyPosition()){
                mImageViewCheckIndicator.setVisibility(View.VISIBLE);
                mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.grey20));
            } else {
                mImageViewCheckIndicator.setVisibility(View.GONE);
                mLinearLayoutAddress.setBackgroundColor(Color.WHITE);
            }
            mTextViewAddress.setText(address);
        }
    }

    public class AddressAdapter extends RecyclerView.Adapter<AddressHolder>{

        private List<DeterministicKey> mDeterministicKeys;
        private String mAddress;


        AddressAdapter(List<DeterministicKey> deterministicKeys){
            mDeterministicKeys = deterministicKeys;
        }

        @Override
        public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_address, parent, false);
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
