package org.qtum.wallet.model.news;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Root(name = "item", strict = false)
public class News {

    @Element(name = "encoded")
    String contentEncoded;

    @Element(name = "title")
    String title;

    @Element(name = "pubDate")
    String pubDate;

    Document mDocument;

    String formattedData = "";

    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    public String getContentEncoded() {
        return contentEncoded;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        if(formattedData.isEmpty()){
            Date date = null;
            try{
                date = sdf.parse(pubDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                formattedData = String.format(Locale.US, "%s, %d", calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US), calendar.get(Calendar.DAY_OF_MONTH));
            }catch (Exception e){ e.printStackTrace(); }
        }
        return formattedData;
    }

    public Document getDocument() {
        if(mDocument == null){
            mDocument = Jsoup.parse(contentEncoded);
        }
        return mDocument;
    }
}
