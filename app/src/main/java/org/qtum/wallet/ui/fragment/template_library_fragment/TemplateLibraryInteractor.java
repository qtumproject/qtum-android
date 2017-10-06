package org.qtum.wallet.ui.fragment.template_library_fragment;

import org.qtum.wallet.model.ContractTemplate;

import java.util.List;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public interface TemplateLibraryInteractor {
    List<ContractTemplate> getContactTemplates();

    int compareDates(String date, String date1);
}
