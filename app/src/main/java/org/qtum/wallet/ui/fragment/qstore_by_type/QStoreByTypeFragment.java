package org.qtum.wallet.ui.fragment.qstore_by_type;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.qstore.QstoreItem;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesFragment;
import org.qtum.wallet.ui.fragment.store_contract.StoreContractFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.FontCheckBox;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.SearchBar;
import org.qtum.wallet.utils.SearchBarListener;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public abstract class QStoreByTypeFragment extends BaseFragment implements QStoreByTypeView, StoreItemClickListener {

    private QStoreByTypePresenter presenter;

    protected StoreSearchAdapter searchAdapter;
    private final static String TYPE = "type";

    @OnClick(R.id.ibt_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @BindView(R.id.content_list)
    protected
    RecyclerView contentList;

    @BindView(R.id.search_type_menu)
    LinearLayout searchTypeMenu;

    @BindView(org.qtum.wallet.R.id.search_bar)
    SearchBar searchBar;

    @BindView(R.id.cb_by_name)
    FontCheckBox cbByName;

    @BindView(R.id.cb_by_tag)
    FontCheckBox cbByTag;

    @BindView(R.id.tv_toolbar_qstore)
    FontTextView mTextViewToolBar;

    @BindView(R.id.ibt_categories)
    ImageButton mImageButton;

    @OnClick(R.id.ibt_categories)
    public void onCategoriesClick() {
        openFragment(StoreCategoriesFragment.newInstance(getContext()));
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, QStoreByTypeFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    public static BaseFragment newInstance(String type, Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, QStoreByTypeFragment.class);
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getType() {
        return getArguments().getString(TYPE, "");
    }

    @Override
    protected void createPresenter() {
        presenter = new QStoreByTypePresenterImpl(this, new QStoreByTypeInteractorImpl(getContext()));
    }

    @Override
    protected QStoreByTypePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        contentList.setLayoutManager(new LinearLayoutManager(getContext()));

        cbByTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbByName.setChecked(!isChecked);
                presenter.searchItems(getSeacrhBarText(), cbByTag.isChecked());
            }
        });

        cbByName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbByTag.setChecked(!isChecked);
                presenter.searchItems(getSeacrhBarText(), cbByTag.isChecked());
            }
        });

        cbByTag.setChecked(true);


        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                searchBar.setListener(new SearchBarListener() {
                    @Override
                    public void onActivate() {
                        if (searchTypeMenu != null) {
                            searchTypeMenu.setVisibility(View.VISIBLE);
                        }
                        contentList.setAdapter(searchAdapter);
                    }

                    @Override
                    public void onDeactivate() {
                        if (searchTypeMenu != null) {
                            searchTypeMenu.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onRequestSearch(String filter) {
                        subscriber.onNext(filter);
                    }
                });
            }
        })
                .debounce(6000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String s) {
                        if (cbByTag != null) {
                            presenter.searchItems(s, cbByTag.isChecked());
                        }
                    }
                });
    }

    @Override
    public void setUpTitle(String type) {
        mTextViewToolBar.setText(type);
        mImageButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setSearchBarText(String text) {
        searchBar.setText(text);
    }

    @Override
    public String getSeacrhBarText() {
        return searchBar.getText();
    }

    @Override
    public void OnItemClick(QstoreItem item) {
        openFragmentForResult(StoreContractFragment.newInstance(getContext(), item.id));
    }

    public void setSearchTag(String tag) {
        cbByName.setChecked(false);
        cbByTag.setChecked(true);
        setSearchBarText(tag);
    }
}
