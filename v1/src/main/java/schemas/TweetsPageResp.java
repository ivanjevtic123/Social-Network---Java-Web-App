/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemas;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import javax.annotation.Nullable;

/**
 *
 * @author ji180550d
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TweetsPageResp {
    
    private TweetResp[] tweets;
    
    @Nullable 
    private String nextPage;

    @JsonGetter()
    public TweetResp[] getTweets() {
        return tweets;
    }

    @JsonSetter()
    public void setTweets(TweetResp[] tweets) {
        this.tweets = tweets;
    }

    @JsonGetter()
    public String getNextPage() {
        return nextPage;
    }

    @JsonSetter()
    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
