package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenBalance.Balance;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenBalanceChangeListener;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenParams;
import org.qtum.mromanovsky.qtum.dataprovider.UpdateService;
import org.qtum.mromanovsky.qtum.datastorage.TokenList;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;

public class WalletAppBarFragmentPresenter {

    UpdateService mUpdateService;

    WalletAppBarFragmentView mWalletAppBarFragmentView;

    TokenParams mTokenParams;

    public WalletAppBarFragmentPresenter(WalletAppBarFragmentView walletAppBarFragmentView){
        mWalletAppBarFragmentView = walletAppBarFragmentView;
    }

    public void onReceiveClick(){
        ReceiveFragment receiveFragment = ReceiveFragment.newInstance();
        getView().openFragment(receiveFragment);
    }

    public void setPosition(int position){
         if(position!=-1)
            mTokenParams = TokenList.getTokenList().getList().get(position);
    }

    public void onViewCreated(){
        if(mTokenParams!=null) {
            mUpdateService = ((MainActivity) getView().getFragmentActivity()).getUpdateService();
            mUpdateService.addTokenBalanceChangeListener(mTokenParams.getAddress(), new TokenBalanceChangeListener() {
                @Override
                public void onBalanceChange() {
                    int balanceInt = 0;
                    for (Balance balance : mTokenParams.getBalance().getBalances()) {
                        balanceInt += balance.getBalance();
                    }
                    final int finalBalanceInt = balanceInt;
                    getView().getFragmentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().updateBalance(String.valueOf(finalBalanceInt), null);
                        }
                    });
                }
            });

            if(mTokenParams.getBalance()!=null){
                int balanceInt = 0;
                for (Balance balance : mTokenParams.getBalance().getBalances()) {
                    balanceInt += balance.getBalance();
                }
                getView().updateBalance(String.valueOf(balanceInt), null);
            }
            getView().setSymbol(mTokenParams.getSymbol());
        } else {
            getView().setSymbol("QTUM");
        }
    }

    public void onDestroyView(){
        if(mTokenParams!=null) {
            mUpdateService.removeTokenBalanceChangeListener(mTokenParams.getAddress());
        }
    }

    public WalletAppBarFragmentView getView(){
        return mWalletAppBarFragmentView;
    }

}
