package org.qtum.wallet.ui.fragment.news_detail_fragment;

import org.jsoup.select.Elements;

public interface NewsDetailInteractor {
    Elements getElements(int newsPosition);
}
