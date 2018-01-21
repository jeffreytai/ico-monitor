package com.crypto.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "IcoInformation")
public class ICOInformation {

    /***************
     * Fields
     ***************/

    /**
     * Auto-generated ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;

    /**
     * ICO name
     */
    @Column(name = "Name", nullable = false)
    private String name;

    /**
     * Optional codename of ICO if exists (given by Balina)
     */
    @Column(name = "CodeName")
    private String codeName;

    /**
     * ICO drops url
     */
    @Column(name = "Url")
    private String url;

    /**
     * Timestamp added to database
     */
    @Column(name = "Timestamp")
    private Date timestamp;


    /****************
     * Constructors
     ****************/

    public ICOInformation() {}

    public ICOInformation(String name, String codeName, String url, Date timestamp) {
        this.name = name;
        this.codeName = codeName;
        this.url = url;
        this.timestamp = timestamp;
    }


    /****************
     * Getters and setters
     ****************/

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
