package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class ConfirmPassphraseFragment extends BaseFragment implements ConfirmPassphraseView{

    private static final String SEED = "seed";

    ConfirmPassphrasePresenter mConfirmPassphrasePresenter;

    @BindView(R.id.rv_input_seed)
    RecyclerView mRecyclerViewInput;
    @BindView(R.id.rv_output_seed)
    RecyclerView mRecyclerViewOutput;

    List<String> inputSeed;
    List<String> outputSeed;
    WordsAdapter wordsAdapterInput;
    WordsAdapter wordsAdapterOutput;

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
        outputSeed = new ArrayList<>();

        wordsAdapterInput = new WordsAdapter(inputSeed,new OnSeedClickListener() {
            @Override
            public void onSeedClick(int position) {
                outputSeed.add(inputSeed.get(position));
                inputSeed.remove(position);
                wordsAdapterInput.notifyDataSetChanged();
                wordsAdapterOutput.notifyDataSetChanged();
            }
        },R.layout.item_seed_chips);

        wordsAdapterOutput = new WordsAdapter(outputSeed,new OnSeedClickListener() {
            @Override
            public void onSeedClick(int position) {
                inputSeed.add(outputSeed.get(position));
                outputSeed.remove(position);
                wordsAdapterInput.notifyDataSetChanged();
                wordsAdapterOutput.notifyDataSetChanged();
            }
        },R.layout.item_seed_chips);

        mRecyclerViewInput.setAdapter(wordsAdapterInput);
        mRecyclerViewOutput.setAdapter(wordsAdapterOutput);
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

    class WordsAdapter extends RecyclerView.Adapter<WordsHolder>{

        protected List<String> seed;
        protected OnSeedClickListener listener;
        protected int resId;

        WordsAdapter(List<String> seed, OnSeedClickListener listener, int resId){
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
