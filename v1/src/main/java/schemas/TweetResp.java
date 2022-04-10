/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemas;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ji180550d
 */
@XmlRootElement(name = "tweetResp")
public class TweetResp implements Serializable {
    private int tweetId;
    private String tweetBody;
    private String[] hashTags = new String[0];
    private String createdBy;
    private String createdAt;

    public TweetResp(int tweetId, String tweetBody, String createdBy, String createdAt, String[] hashTags) {
        this.tweetId = tweetId;
        this.tweetBody = tweetBody;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.hashTags = hashTags;
    }

    public TweetResp() {
    }
    
    @JsonGetter()
    public int getTweetId() {
        return tweetId;
    }

    @JsonSetter()
    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    @JsonGetter()
    public String getTweetBody() {
        return tweetBody;
    }

    @JsonSetter()
    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    @XmlElementWrapper(name = "hashTags")
    @XmlElement(name = "hashTag")
    @JsonGetter()
    public String[] getHashTags() {
        return hashTags;
    }

    @JsonSetter()
    public void setHashTags(String[] hashTags) {
        this.hashTags = hashTags;
    }

    @JsonGetter()
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonSetter()
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonGetter()
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonSetter()
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
