package com.yalday.app.domain.dto;

import com.yalday.app.domain.Customer;
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
public class CustomerDTO {

    @NotNull
    private String name;

    @NotNull
    private String email;

    private String firstLineOfAddress;

    private String secondLineOfAddress;

    private String city;

    @NotNull
    private String postcode;

    private String facebook;

    private String instagram;

    public Customer toCustomer(){return toCustomer(Optional.empty());}

    public Customer toCustomer(Optional<Long> id){
        return Customer.builder()
                .id(id.orElse(null))
                .name(name)
                .email(email)
                .firstLineOfAddress(firstLineOfAddress)
                .secondLineOfAddress(secondLineOfAddress)
                .city(city)
                .postcode(postcode)
                .facebook(facebook)
                .instagram(instagram)
                .build();
    }

}
