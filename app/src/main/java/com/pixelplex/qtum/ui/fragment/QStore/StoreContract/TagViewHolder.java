package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.utils.FontButton;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 09.08.17.
 */

public class TagViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.bt_title)
    FontButton title;

    String tagValue;

    public TagViewHolder(View itemView, final TagClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTagClick(tagValue);
            }
        });
    }

    public void bind(String tag){
        tagValue = tag;
        title.setText(String.format("#%s", tagValue));
    }
}
