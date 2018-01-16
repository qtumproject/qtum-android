package org.qtum.wallet.ui.fragment.overview_fragment;


public class CopyableOverviewItem {

    private String title;
    private String value;
    private boolean isCopyable;

    public CopyableOverviewItem(String title, String value, boolean isCopyable) {
        this.title = title;
        this.value = value;
        this.isCopyable = isCopyable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCopyable() {
        return isCopyable;
    }

    public void setCopyable(boolean copyable) {
        isCopyable = copyable;
    }
}
