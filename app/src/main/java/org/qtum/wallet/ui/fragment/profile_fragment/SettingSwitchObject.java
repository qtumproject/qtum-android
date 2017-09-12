package org.qtum.wallet.ui.fragment.profile_fragment;


class SettingSwitchObject extends SettingObject {

    private boolean mIsChecked;

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
