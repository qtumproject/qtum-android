package com.pixelplex.qtum.ui.fragment.ProfileFragment;

import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 22.05.17.
 */

public class PrefViewHolder extends RecyclerView.ViewHolder /*implements View.OnTouchListener*/ {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.title)
    FontTextView title;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    @BindView(R.id.arrow)
    ImageView arrow;

    OnSettingClickListener listener;

    SettingObject setting;

    public PrefViewHolder(View itemView, final OnSettingClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //rootLayout.setOnTouchListener(this);
        this.listener = listener;

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSettingClick(setting.getTitleRes());
            }
        });
    }

    public void bind(SettingObject setting) {
        title.setText(setting.getTitleRes());
        icon.setImageResource(setting.getImageRes());
        this.setting = setting;
    }
//
//    private void touchDown(){
//        rootLayout.setBackgroundResource(R.color.accent_red_color);
//        icon.setColorFilter(R.color.background);
//        title.setTextColor(ContextCompat.getColor(title.getContext(), R.color.background));
//        arrow.setColorFilter(R.color.background);
//    }
//
//    private void touchUp() {
//        rootLayout.setBackgroundResource(android.R.color.transparent);
//        icon.setColorFilter(R.color.colorPrimary);
//        title.setTextColor(ContextCompat.getColor(title.getContext(), R.color.colorPrimary));
//        arrow.setColorFilter(R.color.colorPrimary);
//    }
//
//    Rect rect;
//
//    private void NotifyTouch(View v, MotionEvent event){
//
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//                touchDown();
//                break;
//            case MotionEvent.ACTION_UP:
//                touchUp();
//                listener.onSettingClick(setting.getTitleRes());
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(rect!=null)
//                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
//                        touchUp();
//                    }
//                break;
//            default: break;
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        NotifyTouch(v, event);
//        return true;
//    }
}
