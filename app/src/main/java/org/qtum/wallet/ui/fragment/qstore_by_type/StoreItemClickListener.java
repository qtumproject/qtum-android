package org.qtum.wallet.ui.fragment.qstore_by_type;

import org.qtum.wallet.model.gson.qstore.QstoreItem;

interface StoreItemClickListener {
    void OnItemClick(QstoreItem item);
}
