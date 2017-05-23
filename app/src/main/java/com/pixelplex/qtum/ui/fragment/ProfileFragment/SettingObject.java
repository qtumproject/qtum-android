package com.pixelplex.qtum.ui.fragment.ProfileFragment;

/**
 * Created by kirillvolkov on 22.05.17.
 */

public class SettingObject {

    public SettingObject(int title, int image, int section) {
        this.titleRes = title;
        this.imageRes = image;
        this.sectionNumber = section;
    }

    int titleRes;
    int imageRes;
    int sectionNumber;

    public int getTitleRes(){
        return titleRes;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

}
