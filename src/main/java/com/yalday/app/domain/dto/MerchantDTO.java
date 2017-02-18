package com.yalday.app.domain.dto;

import com.yalday.app.domain.Merchant;
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
public class MerchantDTO {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String description;

    private String logo;

    private String firstLineOfAddress;

    private String secondLineOfAddress;

    private String city;

    @NotNull
    private String postcode;

    private String facebook;

    private String instagram;

    public Merchant toMerchant() {
        return toMerchant(Optional.empty());
    }

    public Merchant toMerchant(Optional<Long> id) {
        return Merchant.builder()
            .id(id.orElse(null))
            .name(name)
            .email(email)
            .description(description)
            .logo(logo)
            .firstLineOfAddress(firstLineOfAddress)
            .secondLineOfAddress(secondLineOfAddress)
            .city(city)
            .postcode(postcode)
            .facebook(facebook)
            .instagram(instagram)
            .build();
    }

}
