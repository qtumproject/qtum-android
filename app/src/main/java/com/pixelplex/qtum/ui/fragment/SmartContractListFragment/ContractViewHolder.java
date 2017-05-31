package com.pixelplex.qtum.ui.fragment.SmartContractListFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.SmartContractsManager.ContractInfo;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class ContractViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    FontTextView title;

    @BindView(R.id.date)
    FontTextView date;

    @BindView(R.id.contract_type)
    FontTextView contractType;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    public ContractViewHolder(View itemView, final ContractSelectListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectContract(title.getText().toString());
            }
        });
    }

    public void bind(ContractInfo contract) {
        title.setText(contract.getName());

        long currentTime = (new Date()).getTime();
        long transactionTime = contract.getDate();
        long delay = currentTime - transactionTime;
        String dateString;
        if (delay < 60000) {
            dateString = delay / 1000 + " sec ago";
        } else
        if (delay < 3600000) {
            dateString = delay / 60000 + " min ago";
        } else {

            Calendar calendarNow = Calendar.getInstance();
            calendarNow.set(Calendar.HOUR_OF_DAY, 0);
            calendarNow.set(Calendar.MINUTE, 0);
            calendarNow.set(Calendar.SECOND, 0);


            Date dateTransaction = new Date(transactionTime);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTransaction);
            if ((transactionTime - calendarNow.getTimeInMillis()) > 0) {
                dateString = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
            } else {
                dateString = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + calendar.get(Calendar.DATE);
            }
        }

        date.setText(dateString);
        contractType.setText("("+contract.getContractType()+")");
    }
}
