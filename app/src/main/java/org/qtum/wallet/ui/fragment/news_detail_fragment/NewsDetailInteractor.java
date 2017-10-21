package org.qtum.wallet.ui.fragment.news_detail_fragment;


import org.jsoup.nodes.Element;

import java.util.List;

public interface NewsDetailInteractor {
    List<Element> getElements(int newsPosition);
}
