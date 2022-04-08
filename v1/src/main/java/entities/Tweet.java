/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ji180550d
 */
@Entity
@Table(name = "tweet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tweet.findAll", query = "SELECT t FROM Tweet t"),
    @NamedQuery(name = "Tweet.findById", query = "SELECT t FROM Tweet t WHERE t.id = :id"),
    @NamedQuery(name = "Tweet.findByContent", query = "SELECT t FROM Tweet t WHERE t.content = :content"),
    @NamedQuery(name = "Tweet.findByCreatedAt", query = "SELECT t FROM Tweet t WHERE t.createdAt = :createdAt")})
public class Tweet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 280)
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idTwe")
    private Tweethashtag tweethashtag;
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User idUser;

    public Tweet() {
    }

    public Tweet(Integer id) {
        this.id = id;
    }

    public Tweet(Integer id, String content, Date createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Tweethashtag getTweethashtag() {
        return tweethashtag;
    }

    public void setTweethashtag(Tweethashtag tweethashtag) {
        this.tweethashtag = tweethashtag;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tweet)) {
            return false;
        }
        Tweet other = (Tweet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Tweet[ id=" + id + " ]";
    }
    
}
