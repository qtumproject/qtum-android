package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;

/**
 * Created by Romanovsky on 03.02.2017.
 */

public interface NewsFragmentInteractor {
    void getNewsList(NewsFragmentInteractorImpl.GetNewsListCallBack callBack);
}
