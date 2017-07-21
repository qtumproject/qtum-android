package com.pixelplex.qtum.ui.fragment.TokenFragment.Dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.ui.fragment.TokenFragment.TokenFragment;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by kirillvolkov on 20.07.17.
 */

public class ShareDialogFragment extends AppCompatDialogFragment {

    public static String ABI = "S_ABI";
    public static String ADDR = "S_ADDR";

    @OnClick(R.id.btn_ok)
    public void onCancelClick(){
        dismiss();
    }

    @OnClick(R.id.copy_addr_btn)
    public void onCopyAddrClick(){
        onClipBoard(address.getText().toString());
    }

    @OnClick(R.id.copy_abi_btn)
    public void onCopyAbiClick(){
        onClipBoard(abi.getText().toString());
    }

    @BindView(R.id.tv_address)
    FontTextView address;

    @BindView(R.id.tv_abi)
    FontTextView abi;

    public static ShareDialogFragment newInstance(String addr, String abi) {
        Bundle args = new Bundle();
        ShareDialogFragment fragment = new ShareDialogFragment();
        args.putSerializable(ADDR,addr);
        args.putSerializable(ABI,abi);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lyt_view_share_abi_address, container, false);
        ButterKnife.bind(this, view);
        address.setText(getArguments().getString(ADDR));
        abi.setText(getArguments().getString(ABI));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onClipBoard(String str){
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", str);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(),"Copied",Toast.LENGTH_SHORT).show();
    }
}