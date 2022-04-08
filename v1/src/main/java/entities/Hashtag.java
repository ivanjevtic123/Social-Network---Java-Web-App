/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ji180550d
 */
@Entity
@Table(name = "hashtag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hashtag.findAll", query = "SELECT h FROM Hashtag h"),
    @NamedQuery(name = "Hashtag.findById", query = "SELECT h FROM Hashtag h WHERE h.id = :id"),
    @NamedQuery(name = "Hashtag.findByHashname", query = "SELECT h FROM Hashtag h WHERE h.hashname = :hashname")})
public class Hashtag implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "hashname")
    private String hashname;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idTag")
    private Tweethashtag tweethashtag;

    public Hashtag() {
    }

    public Hashtag(Integer id) {
        this.id = id;
    }

    public Hashtag(Integer id, String hashname) {
        this.id = id;
        this.hashname = hashname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHashname() {
        return hashname;
    }

    public void setHashname(String hashname) {
        this.hashname = hashname;
    }

    public Tweethashtag getTweethashtag() {
        return tweethashtag;
    }

    public void setTweethashtag(Tweethashtag tweethashtag) {
        this.tweethashtag = tweethashtag;
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
        if (!(object instanceof Hashtag)) {
            return false;
        }
        Hashtag other = (Hashtag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Hashtag[ id=" + id + " ]";
    }
    
}
