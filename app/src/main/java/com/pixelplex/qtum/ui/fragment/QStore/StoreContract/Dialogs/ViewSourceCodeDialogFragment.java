package com.pixelplex.qtum.ui.fragment.QStore.StoreContract.Dialogs;

        import android.app.Dialog;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatDialogFragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.pixelplex.qtum.R;

        import butterknife.ButterKnife;
        import butterknife.OnClick;

/**
 * Created by kirillvolkov on 29.06.17.
 */

public class ViewSourceCodeDialogFragment extends AppCompatDialogFragment {

    @OnClick(R.id.btn_cancel)
    public void onCancelClick(){
        dismiss();
    }

    @OnClick(R.id.btn_copy)
    public void onConfirmClick(){
        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lyt_view_source_code_popup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
