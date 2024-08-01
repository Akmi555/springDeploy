package org.blb.DTO.advertisingDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisingRequestDto {

    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Description is mandatory")
    @Size(max = 500,message ="Description can have a maximum of 500 characters" )
    private String description;
    @NotBlank
    @NotNull
    @Size(min = 2, max = 15)
    private String advertiserName;

    @Email
    @NotNull
    private String advertiserEmail;

    @NotBlank(message = "Advertiser Phone is mandatory")
    private String advertiserPhone;

    @NotBlank(message = "Discount is mandatory")
    @Pattern(regexp = "\\d{1,2}%|\\d{1,2}\\.?\\d{0,2}", message = "Discount should be a valid percentage or a number")
    private String discount;

    @NotNull(message = "Create Date is mandatory")
    private LocalDate createData;

}
