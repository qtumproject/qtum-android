package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment.light;


import android.support.v4.content.ContextCompat;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.confirm_passphrase_fragment.ConfirmPassphraseFragment;

import java.util.List;


public class ConfirmPassphraseFragmentLight extends ConfirmPassphraseFragment{

    @Override
    protected int getLayout() {
        return R.layout.fragment_confirm_passphrase_light;
    }

    @Override
    public void showError() {
        mEditTextError.setVisibility(View.VISIBLE);
        mRelativeLayoutOutputContainer.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_output_seed_error_light));
    }

    @Override
    public void hideError() {
        mEditTextError.setVisibility(View.INVISIBLE);
        mRelativeLayoutOutputContainer.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_et_import_wallet_light));
    }

    @Override
    public void setUpRecyclerViews(List<String> seed) {
        super.setUpRecyclerViews(seed);
        wordsAdapterInput = new WordsAdapter(inputSeed,inputSeedListener,R.layout.item_seed_chips_input_light);

        wordsAdapterOutput = new WordsAdapter(outputSeed,outputSeedListener,R.layout.item_seed_chips_output_light);

        mRecyclerViewInput.setAdapter(wordsAdapterInput);
        mRecyclerViewOutput.setAdapter(wordsAdapterOutput);
    }
}
