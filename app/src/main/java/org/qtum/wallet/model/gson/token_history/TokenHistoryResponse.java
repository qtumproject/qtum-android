package org.qtum.wallet.model.gson.token_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TokenHistoryResponse {

    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<TokenHistory> items = null;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<TokenHistory> getItems() {
        return items;
    }

    public void setItems(List<TokenHistory> items) {
        this.items = items;
    }

    //for unit_test
    public TokenHistoryResponse(Integer limit, Integer offset, Integer count, List<TokenHistory> items) {
        this.limit = limit;
        this.offset = offset;
        this.count = count;
        this.items = items;
    }
}