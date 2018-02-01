package org.qtum.wallet.ui.fragment.wallet_fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.dataprovider.services.update_service.listeners.TransactionListener;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.qtum_cash_management_fragment.AddressListFragment;
import org.qtum.wallet.ui.fragment.receive_fragment.ReceiveFragment;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.ClipboardUtils;
import org.qtum.wallet.utils.FontTextView;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.OrderedCollectionChangeSet;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public abstract class WalletFragment extends BaseFragment implements WalletView, TransactionClickListener {

    private boolean OPEN_QR_CODE_FRAGMENT_FLAG = false;
    private static final int REQUEST_CAMERA = 3;
    private NetworkStateListener mNetworkStateListener;
    protected WalletPresenter mWalletFragmentPresenter;
    protected TransactionAdapter mTransactionAdapter;
    protected LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
    protected int visibleItemCount;
    protected int totalItemCount;
    protected int pastVisibleItems;
    protected boolean mLoadingFlag = false;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

//    @BindView(R.id.swipe_refresh)
//    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.app_bar)
    protected AppBarLayout mAppBarLayout;

    @BindView(R.id.bt_qr_code)
    protected ImageButton mButtonQrCode;

    @BindView(R.id.tv_wallet_name)
    protected TextView mTextViewWalletName;

    @BindView(R.id.fade_divider_root)
    RelativeLayout fadeDividerRoot;

    @BindView(R.id.tv_public_key)
    protected FontTextView publicKeyValue;

    @BindView(R.id.tv_balance)
    protected FontTextView balanceValue;

    @BindView(R.id.ll_balance)
    protected LinearLayout balanceLayout;

    @BindView(R.id.available_balance_title)
    protected FontTextView balanceTitle;

    @BindView(R.id.tv_unconfirmed_balance)
    protected FontTextView uncomfirmedBalanceValue;

    @BindView(R.id.unconfirmed_balance_title)
    protected FontTextView uncomfirmedBalanceTitle;

    @BindView(R.id.balance_view)
    protected FrameLayout balanceView;

    @BindView(R.id.toolbar_layout)
    protected CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.histories_placeholder)
    TextView mTextViewHistoriesPlaceholder;

    private NetworkStateReceiver mNetworkStateReceiver;
    private UpdateService mUpdateService;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMainActivity().subscribeServiceConnectionChangeEvent(new MainActivity.OnServiceConnectionChangeListener() {
            @Override
            public void onServiceConnectionChange(boolean isConnecting) {
                if (isConnecting) {
                    mUpdateService = getMainActivity().getUpdateService();
                    mUpdateService.addTransactionListener(new TransactionListener() {
                        @Override
                        public void onNewHistory(History history) {
                            getPresenter().onNewHistory(history);
                        }

                        @Override
                        public boolean getVisibility() {
                            return getPresenter().getVisibility();
                        }
                    });
                    initBalanceListener();
                }
            }
        });

        getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == REQUEST_CAMERA) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        OPEN_QR_CODE_FRAGMENT_FLAG = true;
                    }
                }
            }
        });
        mNetworkStateReceiver = getMainActivity().getNetworkReceiver();
        mNetworkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                getPresenter().onNetworkStateChanged(networkConnectedFlag);
            }
        };
        mNetworkStateReceiver.addNetworkStateListener(mNetworkStateListener);
    }

    public void initBalanceListener() {
        mUpdateService.addBalanceChangeListener(balanceListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().updateVisibility(true);
        if (mUpdateService != null) {
            mUpdateService.clearNotification();
        }
        if (OPEN_QR_CODE_FRAGMENT_FLAG) {
            openQrCodeFragment();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().updateVisibility(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mNetworkStateListener != null) {
            mNetworkStateReceiver.removeNetworkStateListener(mNetworkStateListener);
        }
        if (mUpdateService != null) {
            mUpdateService.removeTransactionListener();
            mUpdateService.removeBalanceChangeListener(balanceListener);
        }
        if (getMainActivity() != null) {
            getMainActivity().removePermissionResultListener();
        }
        setAdapterNull();
    }

    @OnClick({R.id.ll_receive, R.id.iv_receive})
    public void onReceiveClick() {
        BaseFragment receiveFragment = ReceiveFragment.newInstance(getContext(), null, null, null);
        openFragmentForResult(receiveFragment);
    }

    @OnClick(R.id.iv_choose_address)
    public void onChooseAddressClick() {
        BaseFragment addressListFragment = AddressListFragment.newInstance(getContext());
        openFragment(addressListFragment);
    }

    @OnClick({R.id.bt_qr_code})
    public void onClick(View view) {
        if (getMainActivity().checkPermission(Manifest.permission.CAMERA)) {
            openQrCodeFragment();
        } else {
            getMainActivity().loadPermissions(Manifest.permission.CAMERA, REQUEST_CAMERA);
        }
    }

    private void openQrCodeFragment() {
        OPEN_QR_CODE_FRAGMENT_FLAG = false;
        SendFragment sendFragment = (SendFragment) SendFragment.newInstance(true, null, null, null, getContext());
        openRootFragment(sendFragment);
        getMainActivity().setRootFragment(sendFragment);
    }

    @OnLongClick(R.id.tv_public_key)
    public boolean onAddressLongClick() {
        ClipboardUtils.copyToClipBoard(getContext(), publicKeyValue.getText().toString(), new ClipboardUtils.CopyCallback() {
            @Override
            public void onCopyToClipBoard() {
                showToast(getString(R.string.copied));
            }
        });
        return true;
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, WalletFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    protected float percents = 1;

    public int getTotalRange() {
        return mAppBarLayout.getTotalScrollRange();
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO
                if (dy > 0) {
                    if (!mLoadingFlag) {
                        visibleItemCount = mLinearLayoutManager.getChildCount();
                        totalItemCount = mLinearLayoutManager.getItemCount();
                        pastVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 1) {
                            mLoadingFlag = true;
                            getPresenter().onLastItem(totalItemCount - 1);
                        }
                    }
                }
            }
        });
        createAdapter();
    }

    protected abstract void createAdapter();

    @Override
    protected WalletPresenter getPresenter() {
        return mWalletFragmentPresenter;
    }

    @Override
    protected void createPresenter() {
        mWalletFragmentPresenter = new WalletPresenterImpl(this, new WalletInteractorImpl(getContext()));
    }

    @Override
    public void onTransactionClick(String txHash) {
        getPresenter().openTransactionFragment(txHash);
    }

    public void updateHistory(TransactionAdapter adapter) {
        mTransactionAdapter = adapter;
        if(mTransactionAdapter.getItemCount()==1){
            mTextViewHistoriesPlaceholder.setVisibility(View.VISIBLE);
        }else{
            mTextViewHistoriesPlaceholder.setVisibility(View.GONE);
        }
        mRecyclerView.setAdapter(mTransactionAdapter);
        //mSwipeRefreshLayout.setRefreshing(false);
        mLoadingFlag = false;
    }

    protected TransactionClickListener getAdapterListener() {
        return this;
    }

    @Override
    public void setAdapterNull() {
    }

    @Override
    public void updateHistory(RealmResults<History> histories, @javax.annotation.Nullable OrderedCollectionChangeSet changeSet, int visibleItemCount) {
        mLoadingFlag = false;
        mTransactionAdapter.setHistoryList(histories.subList(0,visibleItemCount));
        if (changeSet == null) {
            mTransactionAdapter.notifyDataSetChanged();
            return;
        }
        // For deletions, the adapter has to be notified in reverse order.
        OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
        for (int i = deletions.length - 1; i >= 0; i--) {
            OrderedCollectionChangeSet.Range range = deletions[i];
            if(range.startIndex<=visibleItemCount) {
                int length = range.length;
                if(range.startIndex+range.length>visibleItemCount){
                    length = visibleItemCount - range.startIndex;
                }
                mTransactionAdapter.notifyItemRangeRemoved(range.startIndex, length);
            }
        }

        OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
        for (OrderedCollectionChangeSet.Range range : insertions) {
            if(range.startIndex<=visibleItemCount) {
                int length = range.length;
                if (range.startIndex + range.length > visibleItemCount) {
                    length = visibleItemCount - range.startIndex;
                }
                mTransactionAdapter.notifyItemRangeInserted(range.startIndex, length);
            }
        }

        OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
        for (OrderedCollectionChangeSet.Range range : modifications) {
            if(range.startIndex<=visibleItemCount) {
                int length = range.length;
                if (range.startIndex + range.length > visibleItemCount) {
                    length = visibleItemCount - range.startIndex;
                }
                mTransactionAdapter.notifyItemRangeChanged(range.startIndex, length);
            }
        }
    }

    @Override
    public void updatePubKey(String pubKey) {
        publicKeyValue.setText(pubKey);
    }

    @Override
    public void startRefreshAnimation() {
        //mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefreshRecyclerAnimation() {
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
    }

    @Override
    public void addHistory(int positionStart, int itemCount, List<History> historyList) {
        mTransactionAdapter.setHistoryList(historyList);
        mTransactionAdapter.setLoadingFlag(false);
        if(mTransactionAdapter.getItemCount()==1){
            mTextViewHistoriesPlaceholder.setVisibility(View.VISIBLE);
        }else{
            mTextViewHistoriesPlaceholder.setVisibility(View.GONE);
        }
        mLoadingFlag = false;
        mTransactionAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void loadNewHistory() {
        mLoadingFlag = true;
        mTransactionAdapter.setLoadingFlag(true);
        mTransactionAdapter.notifyItemChanged(totalItemCount - 1);
    }

    @Override
    public void notifyNewHistory() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mTransactionAdapter.getItemCount()==1){
                    mTextViewHistoriesPlaceholder.setVisibility(View.VISIBLE);
                }else{
                    mTextViewHistoriesPlaceholder.setVisibility(View.GONE);
                }
                mTransactionAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void notifyConfirmHistory(final int notifyPosition) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTransactionAdapter.notifyItemChanged(notifyPosition);
            }
        });
    }

    BalanceChangeListener balanceListener = new BalanceChangeListener() {
        @Override
        public void onChangeBalance(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
            getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String balanceString = balance.toString();
                    if (balanceString != null) {
                        String unconfirmedBalanceString = unconfirmedBalance.toString();
                        if (!TextUtils.isEmpty(unconfirmedBalanceString) && !unconfirmedBalanceString.equals("0")) {
                            updateBalance(balanceString, String.valueOf(unconfirmedBalance.floatValue()));
                        } else {
                            updateBalance(balanceString, null);
                        }
                    }
                }
            });
        }
    };

    @Override
    public void openTransactionsFragment(String txHash) {
        Fragment fragment = TransactionFragment.newInstance(getContext(), txHash);
        openFragment(fragment);
    }
}