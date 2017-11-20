package org.qtum.wallet.ui.fragment.news_detail_fragment.tag_view_holders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Element;
import org.qtum.wallet.R;
import org.qtum.wallet.utils.FontTextView;

import java.util.concurrent.Callable;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TagFigureViewHolder extends TagViewHolder {

    @BindView(R.id.tv_figcaption)
    FontTextView mTextViewFigcaption;
    @BindView(R.id.iv_image)
    ImageView mImageView;
    Subscription mSubscription;

    public TagFigureViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindElement(final Element element) {
        mTextViewFigcaption.setText(element.select("figcaption").text());
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mImageView.setImageBitmap(null);
        mSubscription = (Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                return Picasso.with(mImageView.getContext()).load(element.select("img").attr("src")).get();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        int resizeWidth = mImageView.getWidth();
                        int resizeHeight;
                        double multiplier = 1;
                        multiplier = Integer.valueOf(bitmap.getWidth()).doubleValue() / resizeWidth;
                        resizeHeight = Double.valueOf(Math.ceil(bitmap.getHeight() / multiplier)).intValue();

                        mImageView.getLayoutParams().width = resizeWidth;
                        mImageView.getLayoutParams().height = resizeHeight;

                        Picasso.with(mImageView.getContext())
                                .load(element.select("img").attr("src"))
                                .resize(resizeWidth, resizeHeight)
                                .centerInside()
                                .into(mImageView);
                    }
                }));
    }
}
