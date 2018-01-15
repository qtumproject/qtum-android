package org.qtum.wallet.model.news;

import com.google.gson.annotations.SerializedName;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.qtum.wallet.utils.DateCalculator;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Root(name = "item", strict = false)
public class News {
    @SerializedName("encoded")
    @Element(name = "encoded")
    String contentEncoded;

    @SerializedName("title")
    @Element(name = "title")
    String title;

    @SerializedName("pubDate")
    @Element(name = "pubDate")
    String pubDate = "";

    transient Document mDocument;

    transient String formattedData = "";

    transient String fullFormattedData = "";

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getFormattedPubDate() {
        if (formattedData.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
            Date date;
            try {
                date = sdf.parse(pubDate);
                formattedData = DateCalculator.getShortDate(date.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return formattedData;
    }

    public String getFullFormattedPubDate(){
        if (fullFormattedData.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
            Date date;
            try {
                date = sdf.parse(pubDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy \nhh:mm aaa", Locale.ENGLISH);
                fullFormattedData = displayFormat.format(date).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fullFormattedData;
    }

    public Document getDocument() {
        if (mDocument == null) {
            mDocument = Jsoup.parse(contentEncoded);
        }
        return mDocument;
    }
}
