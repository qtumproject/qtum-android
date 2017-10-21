package org.qtum.wallet.model.news;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class News {

    @Element(name = "encoded")
    String contentEncoded;

    @Element(name = "title")
    String title;

    @Element(name = "pubDate")
    String pubDate;

    Document mDocument;

    public String getContentEncoded() {
        return contentEncoded;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Document getDocument() {
        if(mDocument == null){
            mDocument = Jsoup.parse(contentEncoded);
        }
        return mDocument;
    }
}
