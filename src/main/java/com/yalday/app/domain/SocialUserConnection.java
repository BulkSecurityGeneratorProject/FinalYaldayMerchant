package com.yalday.app.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Social user.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "social_user_connection")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SocialUserConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "user_id", length = 255, nullable = false)
    private String userId;

    @NotNull
    @Column(name = "provider_id", length = 255, nullable = false)
    private String providerId;

    @NotNull
    @Column(name = "provider_user_id", length = 255, nullable = false)
    private String providerUserId;

    @NotNull
    @Column(nullable = false)
    private Long rank;

    @Column(name = "display_name", length = 255)
    private String displayName;

    @Column(name = "profile_url", length = 255)
    private String profileURL;

    @Column(name = "image_url", length = 255)
    private String imageURL;

    @NotNull
    @Column(name = "access_token", length = 255, nullable = false)
    private String accessToken;

    @Column(length = 255)
    private String secret;

    @Column(name = "refresh_token", length = 255)
    private String refreshToken;

    @Column(name = "expire_time")
    private Long expireTime;

    public SocialUserConnection(String userId,
                                String providerId,
                                String providerUserId,
                                Long rank,
                                String displayName,
                                String profileUrl,
                                String imageUrl,
                                String accessToken,
                                String secret,
                                String refreshToken,
                                Long expireTime) {
        this.userId = userId;
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.rank = rank;
        this.displayName = displayName;
        this.profileURL = profileUrl;
        this.imageURL = imageUrl;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SocialUserConnection user = (SocialUserConnection) o;

        if (!id.equals(user.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SocialUserConnection{" +
                "id=" + id +
                ", userId=" + userId +
                ", providerId='" + providerId + '\'' +
                ", providerUserId='" + providerUserId + '\'' +
                ", rank=" + rank +
                ", displayName='" + displayName + '\'' +
                ", profileURL='" + profileURL + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", secret='" + secret + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}

