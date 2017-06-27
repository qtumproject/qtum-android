package com.pixelplex.qtum.ui.fragment.ProfileFragment;

/**
 * Created by max-v on 6/27/2017.
 */

public class SettingSwitchObject extends SettingObject {

    boolean mIsChecked;

    public SettingSwitchObject(int title, int image, int section) {
        super(title, image, section);
    }

    public  SettingSwitchObject(int title, int image, int section, boolean isChecked){
        this(title,image,section);
        mIsChecked = isChecked;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }
}
