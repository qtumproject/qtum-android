package org.qtum.wallet.ui.fragment.template_library_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.utils.DateCalculator;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public class TemplateLibraryInteractorImpl implements TemplateLibraryInteractor {

    private WeakReference<Context> mContext;

    public TemplateLibraryInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public List<ContractTemplate> getContactTemplates() {
        TinyDB tinyDB = new TinyDB(mContext.get());
        return tinyDB.getContractTemplateList();
    }

    @Override
    public int compareDates(String date, String date1) {
        return DateCalculator.equals(date, date1);
    }
}
