package com.yalday.app.domain.dto;

import com.yalday.app.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private Boolean activated;

    private String langKey;

    private String activationKey;

    private String resetKey;

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
                .build();
    }
}
