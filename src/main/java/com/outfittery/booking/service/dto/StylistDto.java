package com.outfittery.booking.service.dto;

import com.outfittery.booking.config.util.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StylistDto {
    private Long stylistId;

    @NotBlank
    @Pattern(regexp = Constants.NAME_REGEX)
    @Size(min = 1, max = 50)
    private String stylistName;
}
