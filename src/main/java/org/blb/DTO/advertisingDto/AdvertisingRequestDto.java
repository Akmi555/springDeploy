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
    @Size(max = 1000,message ="Description can have a maximum of 500 characters" )
    private String description;
    @NotBlank
    @NotNull
    @Size(min = 2, max = 50)
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
    @NotNull(message = "End Date is mandatory")
    private LocalDate endData;

    private Integer advertisingCounter;

    private String descriptionOfTheCoupon;

    public AdvertisingRequestDto(String title, String description, String advertiserName, String advertiserEmail, String advertiserPhone, String discount, LocalDate endData,String Description) {
        this.title = title;
        this.description = description;
        this.advertiserName = advertiserName;
        this.advertiserEmail = advertiserEmail;
        this.advertiserPhone = advertiserPhone;
        this.discount = discount;
        this.createData = LocalDate.now();
        this.endData = endData;
        this.advertisingCounter = 100;
        this.descriptionOfTheCoupon = description;
    }
}
