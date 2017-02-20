package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.text.DecimalFormat;

import butterknife.BindView;


public class TransactionFragment extends BaseFragment implements TransactionFragmentView {

    AnimatedVectorDrawable drawableBottom;
    AnimatedVectorDrawable drawableTop;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_value)
    TextView mTextViewValue;
    @BindView(R.id.tv_received_time)
    TextView mTextViewReceivedTime;
    @BindView(R.id.tv_from)
    TextView mTextViewFrom;
    @BindView(R.id.tv_to)
    TextView mTextViewTo;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.iv_bottom_wave)
    ImageView mImageViewBottomWave;
    @BindView(R.id.iv_top_wave)
    ImageView mImageViewTopWave;

    public final int LAYOUT = R.layout.fragment_transaction;
    final static String POSITION = "position";

    TransactionFragmentPresenterImpl mTransactionFragmentPresenter;

    public static TransactionFragment newInstance(int position) {
        TransactionFragment transactionFragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        transactionFragment.setArguments(args);
        return transactionFragment;
    }

    @Override
    protected void createPresenter() {
        mTransactionFragmentPresenter = new TransactionFragmentPresenterImpl(this);
    }

    @Override
    protected TransactionFragmentPresenterImpl getPresenter() {
        return mTransactionFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().openTransactionView(getArguments().getInt(POSITION));
    }

    @Override
    public void initializeViews() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_indicator);
        }
//        drawableBottom = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_bottom,getActivity().getTheme());
//        mImageViewBottomWave.setImageDrawable(drawableBottom);
//        drawableBottom.start();
//        drawableTop = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_top,getActivity().getTheme());
//        mImageViewTopWave.setImageDrawable(drawableTop);
//        drawableTop.start();
    }


    @Override
    public void setUpTransactionData(double value, String receivedTime, String from, String to) {

        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(8);
        mTextViewValue.setText(df.format(value));
        mTextViewReceivedTime.setText(receivedTime);
        mTextViewFrom.setText(from);
        mTextViewTo.setText(to);
        if (value > 0) {
            mAppBarLayout.setBackgroundResource(R.drawable.background_tb_sent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getFragmentActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.green));
            }
        } else {
            mAppBarLayout.setBackgroundResource(R.drawable.background_tb_received);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getFragmentActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.pink_lite));
            }
        }
    }

    @Override
    public void startAnimation() {
        super.startAnimation();
//        drawableBottom.start();
//        drawableTop.start();
    }

    @Override
    public void stopAnimation() {
        super.stopAnimation();
//        drawableBottom.stop();
//        drawableTop.stop();
    }
}
