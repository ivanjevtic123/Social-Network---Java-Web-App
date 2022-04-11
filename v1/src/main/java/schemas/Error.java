/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemas;

import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.annotation.Nullable;

import java.util.Objects;

/**
 *
 * @author ji180550d
 */
@XmlRootElement(name = "postTweetReq")
public class Error {
    private int httpCode;
    private int errorCode;
    private String message;

    @JsonProperty("httpCode")
    public int getHttpCode() {
        return httpCode;
    }
    
    @JsonSetter("httpCode")
    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    @JsonProperty("errorCode")
    public int getErrorCode() {
        return errorCode;
    }

    @JsonSetter("errorCode")
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonSetter("message")
    public void setMessage(String message) {
        this.message = message;
    }
}
