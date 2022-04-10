/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemas;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ji180550d
 */

@XmlRootElement(name = "postTweetReq")
public class PostTweetReq implements Serializable {
    private String tweetBody;
    private String []hashTags = new String[5];

    @JsonGetter("tweetBody")
    public String getTweetBody() {
        return tweetBody;
    }

    @JsonSetter("tweetBody")
    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    @JsonGetter("hashTags")
    public String[] getHashTags() {
        return hashTags;
    }

    @XmlElementWrapper(name = "hashTags")
    @XmlElement(name = "hashTag")
    @JsonSetter("hashTags")
    public void setHashTags(String[] hashTags) {
        this.hashTags = hashTags;
    }
}
