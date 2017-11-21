package org.qtum.wallet.ui.fragment.news_detail_fragment.light;

import org.jsoup.select.Elements;
import org.qtum.wallet.R;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.news_detail_fragment.NewsDetailFragment;

public class NewsDetailFragmentLight extends NewsDetailFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_news_detail_light;
    }

    @Override
    public void initializeViews() {
        ((MainActivity) getActivity()).showBottomNavigationView(org.qtum.wallet.R.color.title_color_light);
        super.initializeViews();
    }

    @Override
    public void setupElements(Elements elements) {
        if (elements != null) {
            ElementsAdapterLight elementsAdapter = new ElementsAdapterLight(elements);
            mRecyclerView.setAdapter(elementsAdapter);
        } else {
            setAlertDialog(R.string.error, getString(R.string.no_internet_connection), R.string.ok, PopUpType.error, new AlertDialogCallBack() {
                @Override
                public void onButtonClick() {
                    dismiss();
                }

                @Override
                public void onButton2Click() {
                }
            });
        }
    }
}
