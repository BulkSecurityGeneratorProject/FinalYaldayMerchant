package com.yalday.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A Merchant.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "merchant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
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

    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private Timestamp dateCreated;

    @UpdateTimestamp
    @Column(name = "last_edited")
    private Timestamp lastEdited;

}
