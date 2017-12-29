package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public abstract class ConfirmPassphraseFragment extends BaseFragment implements ConfirmPassphraseView{

    private static final String SEED = "seed";

    ConfirmPassphrasePresenter mConfirmPassphrasePresenter;

    @BindView(R.id.rv_input_seed)
    protected RecyclerView mRecyclerViewInput;
    @BindView(R.id.rv_output_seed)
    protected RecyclerView mRecyclerViewOutput;

    @BindView(R.id.bt_reset_all)
    Button mButtonResetAll;
    @BindView(R.id.tv_error_text)
    protected TextView mEditTextError;
    @BindView(R.id.rl_output_container)
    protected RelativeLayout mRelativeLayoutOutputContainer;

    @OnClick({R.id.bt_reset_all,R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.bt_reset_all:
                getPresenter().onResetAllClick();
                break;
        }
    }

    protected List<String> inputSeed;
    protected List<String> outputSeed;
    protected WordsAdapter wordsAdapterInput;
    protected WordsAdapter wordsAdapterOutput;
    protected OnSeedClickListener inputSeedListener = new OnSeedClickListener() {
        @Override
        public void onSeedClick(int position) {
            if(position>-1) {
                outputSeed.add(inputSeed.get(position));
                inputSeed.remove(position);
                wordsAdapterInput.notifyDataSetChanged();
                wordsAdapterOutput.notifyDataSetChanged();
                getPresenter().seedEntered(outputSeed);
            }
        }
    };
    protected OnSeedClickListener outputSeedListener = new OnSeedClickListener() {
        @Override
        public void onSeedClick(int position) {
            if(position>-1) {
                inputSeed.add(outputSeed.get(position));
                outputSeed.remove(position);
                wordsAdapterInput.notifyDataSetChanged();
                wordsAdapterOutput.notifyDataSetChanged();
                getPresenter().seedEntered(outputSeed);
            }
        }
    };

    public static BaseFragment newInstance(Context context, String seed) {

        Bundle args = new Bundle();
        args.putString(SEED, seed);
        BaseFragment fragment = Factory.instantiateFragment(context, ConfirmPassphraseFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ConfirmPassphrasePresenter getPresenter() {
        return mConfirmPassphrasePresenter;
    }

    @Override
    protected void createPresenter() {
        mConfirmPassphrasePresenter = new ConfirmPassphrasePresenterImpl(this, new ConfirmPassphraseInteractorImpl(getContext()));
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        ChipsLayoutManager chipsLayoutManagerInput = ChipsLayoutManager.newBuilder(getContext())
                .build();
        ChipsLayoutManager chipsLayoutManagerOutput = ChipsLayoutManager.newBuilder(getContext())
                .build();
        mRecyclerViewInput.setLayoutManager(chipsLayoutManagerInput);
        mRecyclerViewOutput.setLayoutManager(chipsLayoutManagerOutput);
    }

    @Override
    public void setUpRecyclerViews(List<String> seed) {
        inputSeed = new ArrayList<>(seed);
        Collections.shuffle(inputSeed);
        outputSeed = new ArrayList<>();
    }

    @Override
    public void resetAll(List<String> seed) {
        inputSeed.clear();
        inputSeed.addAll(seed);
        Collections.shuffle(inputSeed);
        outputSeed.clear();
        wordsAdapterInput.notifyDataSetChanged();
        wordsAdapterOutput.notifyDataSetChanged();
    }

    @Override
    public void confirmSeed() {
        final WalletMainFragment walletFragment = WalletMainFragment.newInstance(getContext());
        getMainActivity().setRootFragment(walletFragment);
        openRootFragment(walletFragment);
    }

    @Override
    public void onLogin() {
        getMainActivity().onLogin();
    }

    @Override
    public String getSeed() {
        return getArguments().getString(SEED);
    }

    class WordsHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.bt_seed)
        Button mButtonSeed;

        public WordsHolder(View itemView, final OnSeedClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mButtonSeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSeedClick(getAdapterPosition());
                }
            });
        }

        void bindSeed(String seed){
            mButtonSeed.setText(seed);
        }

    }

    protected class WordsAdapter extends RecyclerView.Adapter<WordsHolder>{

        protected List<String> seed;
        protected OnSeedClickListener listener;
        protected int resId;

        public WordsAdapter(List<String> seed, OnSeedClickListener listener, int resId){
            this.seed = seed;
            this.resId = resId;
            this.listener = listener;
        }

        @Override
        public WordsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(resId, parent, false);
            return new WordsHolder(view,listener);
        }

        @Override
        public void onBindViewHolder(WordsHolder holder, int position) {
            holder.bindSeed(seed.get(position));
        }

        @Override
        public int getItemCount() {
            return seed.size();
        }

    }

    interface OnSeedClickListener{
        void onSeedClick(int position);
    }
}
