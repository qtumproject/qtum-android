package com.pixelplex.qtum.ui.fragment.processing_dialog;

import android.support.v4.app.DialogFragment;
import android.widget.RelativeLayout;
import com.pixelplex.qtum.R;
import butterknife.BindView;


public abstract class ProcessingDialogFragment extends DialogFragment{

    @BindView(R.id.root_layout)
    protected
    RelativeLayout mRootLayout;

}
