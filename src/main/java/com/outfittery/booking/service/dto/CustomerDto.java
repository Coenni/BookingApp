package com.outfittery.booking.service.dto;

import com.outfittery.booking.config.util.Constants;
import com.outfittery.booking.domain.Customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long customerId;

    @NotBlank
    @Pattern(regexp = Constants.NAME_REGEX)
    @Size(min = 1, max = 50)
    private String customerName;

}
