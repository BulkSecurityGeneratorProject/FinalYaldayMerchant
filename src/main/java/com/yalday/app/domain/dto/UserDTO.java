package com.yalday.app.domain.dto;

import com.google.common.collect.Sets;
import com.yalday.app.config.Constants;
import com.yalday.app.domain.Authority;
import com.yalday.app.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    private String login;

    @Size(min = 4, max = 40)
    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private Boolean activated;

    private String langKey;

    private String activationKey;

    private String resetKey;

    private Set<String> authorities;

    public Set<Authority> buildAuthorities(final Set<String> authorities){
        Set<Authority> authoritySet = Sets.newHashSet();
        for(String authority : authorities){
            Authority auth  = new Authority();
            auth.setName(authority);
            authoritySet.add(auth);
        }
        return authoritySet;
    }

    public User toUser(){return toUser(Optional.empty());}

    public User toUser(Optional<Long> id){

        return User.builder()
            .id(id.orElse(null))
            .login(login)
            .password(password)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .activated(activated)
            .langKey(langKey)
            .activationKey(activationKey)
            .resetKey(resetKey)
            .authorities(buildAuthorities(authorities))
            .build();
    }
}
