/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemas;

import java.io.Serializable;
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
    private String []hashTags = new String[5];
    private String createdBy;
    private String createdAt;

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public String getTweetBody() {
        return tweetBody;
    }

    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    @XmlElementWrapper(name = "hashTags")
    @XmlElement(name = "hashTag")
    public String[] getHashTags() {
        return hashTags;
    }

    public void setHashTags(String[] hashTags) {
        this.hashTags = hashTags;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    
}
