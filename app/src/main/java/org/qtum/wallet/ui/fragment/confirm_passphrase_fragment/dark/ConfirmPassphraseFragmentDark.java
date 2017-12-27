package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment.dark;

import android.support.v4.content.ContextCompat;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.confirm_passphrase_fragment.ConfirmPassphraseFragment;

import java.util.List;


public class ConfirmPassphraseFragmentDark extends ConfirmPassphraseFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_confirm_passphrase;
    }

    @Override
    public void showError() {
        mEditTextError.setVisibility(View.VISIBLE);
        mRelativeLayoutOutputContainer.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_output_seed_error));
    }

    @Override
    public void hideError() {
        mEditTextError.setVisibility(View.INVISIBLE);
        mRelativeLayoutOutputContainer.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_et_import_wallet));
    }

    @Override
    public void setUpRecyclerViews(List<String> seed) {
        super.setUpRecyclerViews(seed);
        wordsAdapterInput = new WordsAdapter(inputSeed,inputSeedListener,R.layout.item_seed_chips_input);

        wordsAdapterOutput = new WordsAdapter(outputSeed,outputSeedListener,R.layout.item_seed_chips_output);

        mRecyclerViewInput.setAdapter(wordsAdapterInput);
        mRecyclerViewOutput.setAdapter(wordsAdapterOutput);
    }
}
