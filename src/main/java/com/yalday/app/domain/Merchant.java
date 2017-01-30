package com.yalday.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Merchant.
 */
@Entity
@Table(name = "merchant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(min = 4, max = 40)
    @Column(name = "email", length = 40, nullable = false)
    private String email;

    @NotNull
    @Size(min = 5, max = 256)
    @Column(name = "description", length = 256, nullable = false)
    private String description;

    @Column(name = "logo")
    private String logo;

    @Column(name = "first_line_of_address")
    private String firstLineOfAddress;

    @Column(name = "second_line_of_address")
    private String secondLineOfAddress;

    @Column(name = "city")
    private String city;

    @NotNull
    @Size(min = 5, max = 10)
    @Column(name = "postcode", length = 10, nullable = false)
    private String postcode;

    @Size(min = 5, max = 20)
    @Column(name = "facebook", length = 20)
    private String facebook;

    @Size(min = 5, max = 20)
    @Column(name = "instagram", length = 20)
    private String instagram;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_edited")
    private ZonedDateTime lastEdited;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Merchant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Merchant email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public Merchant description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public Merchant logo(String logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFirstLineOfAddress() {
        return firstLineOfAddress;
    }

    public Merchant firstLineOfAddress(String firstLineOfAddress) {
        this.firstLineOfAddress = firstLineOfAddress;
        return this;
    }

    public void setFirstLineOfAddress(String firstLineOfAddress) {
        this.firstLineOfAddress = firstLineOfAddress;
    }

    public String getSecondLineOfAddress() {
        return secondLineOfAddress;
    }

    public Merchant secondLineOfAddress(String secondLineOfAddress) {
        this.secondLineOfAddress = secondLineOfAddress;
        return this;
    }

    public void setSecondLineOfAddress(String secondLineOfAddress) {
        this.secondLineOfAddress = secondLineOfAddress;
    }

    public String getCity() {
        return city;
    }

    public Merchant city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public Merchant postcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getFacebook() {
        return facebook;
    }

    public Merchant facebook(String facebook) {
        this.facebook = facebook;
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public Merchant instagram(String instagram) {
        this.instagram = instagram;
        return this;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public Merchant dateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getLastEdited() {
        return lastEdited;
    }

    public Merchant lastEdited(ZonedDateTime lastEdited) {
        this.lastEdited = lastEdited;
        return this;
    }

    public void setLastEdited(ZonedDateTime lastEdited) {
        this.lastEdited = lastEdited;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Merchant merchant = (Merchant) o;
        if(merchant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, merchant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", email='" + email + "'" +
            ", description='" + description + "'" +
            ", logo='" + logo + "'" +
            ", firstLineOfAddress='" + firstLineOfAddress + "'" +
            ", secondLineOfAddress='" + secondLineOfAddress + "'" +
            ", city='" + city + "'" +
            ", postcode='" + postcode + "'" +
            ", facebook='" + facebook + "'" +
            ", instagram='" + instagram + "'" +
            ", dateCreated='" + dateCreated + "'" +
            ", lastEdited='" + lastEdited + "'" +
            '}';
    }
}
