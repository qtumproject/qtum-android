package org.qtum.wallet.ui.fragment.addresses_detail_fragment;


import org.qtum.wallet.model.gson.history.History;


public interface AddressesDetailInteractor {
    History getHistory(int position);
}
