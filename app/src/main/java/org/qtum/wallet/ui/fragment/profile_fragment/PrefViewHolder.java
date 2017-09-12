package org.qtum.wallet.ui.fragment.profile_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.utils.FontTextView;

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

    private OnSettingClickListener listener;

    private SettingObject setting;

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
            arrow.setVisibility(View.INVISIBLE);
            mSwitch.setVisibility(View.VISIBLE);
            mSwitch.setChecked(((SettingSwitchObject) setting).isChecked());
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    listener.onSwitchChange(setting.getTitleRes(), b);
                }
            });
            mSwitch.setChecked(QtumSharedPreference.getInstance().isTouchIdEnable(title.getContext()));
        }else{
            mSwitch.setVisibility(View.INVISIBLE);
            arrow.setVisibility(View.VISIBLE);
        }
        title.setText(setting.getTitleRes());

        icon.setImageResource(setting.getImageRes());
        this.setting = setting;
    }
}
