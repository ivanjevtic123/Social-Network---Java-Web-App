/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ji180550d
 */
@Entity
@Table(name = "tweethashtag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tweethashtag.findAll", query = "SELECT t FROM Tweethashtag t"),
    @NamedQuery(name = "Tweethashtag.findById", query = "SELECT t FROM Tweethashtag t WHERE t.id = :id")})
public class Tweethashtag implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "idTag", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Hashtag idTag;
    @JoinColumn(name = "idTwe", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Tweet idTwe;

    public Tweethashtag() {
    }

    public Tweethashtag(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Hashtag getIdTag() {
        return idTag;
    }

    public void setIdTag(Hashtag idTag) {
        this.idTag = idTag;
    }

    public Tweet getIdTwe() {
        return idTwe;
    }

    public void setIdTwe(Tweet idTwe) {
        this.idTwe = idTwe;
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
        if (!(object instanceof Tweethashtag)) {
            return false;
        }
        Tweethashtag other = (Tweethashtag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Tweethashtag[ id=" + id + " ]";
    }
    
}
