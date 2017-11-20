package org.qtum.wallet.model.news;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rss", strict = false)
public class RssFeed {
    @ElementList(name = "item", inline = true)
    @Path("channel")
    public List<News> mNewses;

    public List<News> getNewses() {
        return mNewses;
    }
}
