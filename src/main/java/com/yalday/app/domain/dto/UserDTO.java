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
    private String email;

    public User toUser(){return toUser(Optional.empty());}

    public User toUser(Optional<Long> id){
        return User.builder()
                .id(id.orElse(null))
                .email(email)
                .build();
    }
}
