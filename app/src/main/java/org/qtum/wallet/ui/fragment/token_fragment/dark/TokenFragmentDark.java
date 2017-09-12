package org.qtum.wallet.ui.fragment.token_fragment.dark;

import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.token_fragment.TokenFragment;
import org.qtum.wallet.utils.ResizeWidthAnimation;

import butterknife.BindView;


public class TokenFragmentDark extends TokenFragment {

    private final int LAYOUT = R.layout.lyt_token_fragment;


    @BindView(R.id.fade_divider)
    View fadeDivider;

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        //mSwipeRefreshLayout.setEnabled(false);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                percents = (((getTotalRange() - Math.abs(verticalOffset))*1.0f)/getTotalRange());

                balanceView.setAlpha((percents>0.5f)? percents : 1 - percents);

                if(percents == 0){
                    doDividerExpand();
                } else {
                    doDividerCollapse();
                }

                final float textPercent = (percents >= .5f)? percents : .5f;
                final float textPercent3f = (percents >= .3f)? percents : .3f;

                if(uncomfirmedBalanceTitle.getVisibility() == View.VISIBLE) {
                    animateText(percents, mLinearLayoutBalance, .5f);
                    mLinearLayoutBalance.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (mLinearLayoutBalance.getWidth() * textPercent) / 2) - mLinearLayoutBalance.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));
                    mLinearLayoutBalance.setY(balanceView.getHeight() / 2 - balanceTitle.getHeight() * percents - mLinearLayoutBalance.getHeight() * percents - mLinearLayoutBalance.getHeight() * (1 - percents));

                    animateText(percents, balanceTitle, .7f);
                    balanceTitle.setX(balanceView.getWidth() / 2 * percents - (balanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                    balanceTitle.setY(balanceView.getHeight() / 2 - balanceTitle.getHeight() * percents - balanceTitle.getHeight() * (1 - percents) );

                    animateText(percents, uncomfirmedBalanceValue, .5f);
                    uncomfirmedBalanceValue.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (uncomfirmedBalanceValue.getWidth() * textPercent) / 2) - uncomfirmedBalanceValue.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));

                    animateText(percents, uncomfirmedBalanceTitle, .7f);
                    uncomfirmedBalanceTitle.setY(balanceView.getHeight() / 2 + uncomfirmedBalanceValue.getHeight() * percents - (uncomfirmedBalanceTitle.getHeight() * percents * (1 - percents)));
                    uncomfirmedBalanceTitle.setX(balanceView.getWidth() / 2 * percents - (uncomfirmedBalanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                } else {
                    animateText(percents, balanceTitle, .7f);
                    balanceTitle.setX(balanceView.getWidth() / 2 * percents - (balanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                    balanceTitle.setY(balanceView.getHeight() / 2 + balanceTitle.getHeight() / 2 * percents - balanceTitle.getHeight() / 2 * (1-percents));

                    animateText(percents, mLinearLayoutBalance, .5f);
                    mLinearLayoutBalance.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (mLinearLayoutBalance.getWidth() * textPercent) / 2) - mLinearLayoutBalance.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));
                    mLinearLayoutBalance.setY(balanceView.getHeight() / 2 - mLinearLayoutBalance.getHeight() * percents - mLinearLayoutBalance.getHeight() / 2 * (1-percents));
                }
                collapseLinearLayout.collapseFromPercents(percents);
                prevPercents = percents;
            }

        });
        doDividerCollapse();
    }

    protected void doDividerExpand() {
        if(!expanded) {
            expanded = true;
            fadeDivider.clearAnimation();
            ResizeWidthAnimation anim = new ResizeWidthAnimation(fadeDivider, getResources().getDisplayMetrics().widthPixels);
            anim.setDuration(300);
            anim.setFillEnabled(true);
            anim.setFillAfter(true);
            fadeDivider.startAnimation(anim);
        }
    }

    protected void doDividerCollapse() {
        if(expanded) {
            fadeDivider.clearAnimation();
            fadeDivider.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams lp = fadeDivider.getLayoutParams();
            lp.width = 0;
            fadeDivider.setLayoutParams(lp);
            expanded = false;
        }
    }

    @Override
    public void setBalance(String balance) {
        mTextViewBalance.setText(String.valueOf(balance));
    }

    @Override
    public void onContractPropertyUpdated(String propName, String propValue) {
        switch (propName){
            case totalSupply:
                totalSupplyValue.setText(propValue);
                break;
            case decimals:
                decimalsValue.setText(propValue);
                break;
            case symbol:
                mTextViewCurrency.setText(" " + propValue);
                break;
            case name:
                mTextViewTokenName.setText(propValue);
                break;
        }
    }

}
