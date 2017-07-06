package com.pixelplex.qtum.ui.fragment.ProfileFragment;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PrefViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.title)
    FontTextView title;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    @BindView(R.id.arrow)
    ImageView arrow;

    @BindView(R.id.pref_switch)
    Switch mSwitch;

    OnSettingClickListener listener;

    SettingObject setting;

    public PrefViewHolder(View itemView, final OnSettingClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSettingClick(setting.getTitleRes());
            }
        });
    }

    public void bind(final SettingObject setting) {
        if(setting instanceof SettingSwitchObject){
            arrow.setVisibility(View.GONE);
            mSwitch.setVisibility(View.VISIBLE);
            mSwitch.setChecked(((SettingSwitchObject) setting).isChecked());
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    listener.onSwitchChange(setting.getTitleRes(), b);
                }
            });
        }else{
            mSwitch.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
        }
        title.setText(setting.getTitleRes());
        icon.setImageResource(setting.getImageRes());
        this.setting = setting;
    }
}
