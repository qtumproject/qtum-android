package com.pixelplex.qtum.ui.fragment.ProcessingDialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.pixelplex.qtum.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class ProcessingDialogFragment extends DialogFragment{

    @BindView(R.id.root_layout)
    protected
    RelativeLayout mRootLayout;

}
