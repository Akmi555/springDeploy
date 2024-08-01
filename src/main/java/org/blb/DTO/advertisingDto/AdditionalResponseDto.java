package org.blb.DTO.advertisingDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Advertising Data", description = "Detailed information about an advertisement")
public class AdditionalResponseDto {
    //         private Long id;
    //         private String description;

    @Schema(name = "title", example = "some title", description = "The title of the advertising")
    private String title;

    @Schema(name = "description", example = "some text description", description = "The description of the advertising")
    private String description;

    @Schema(name = "advertiserName", example = "Ivan Ivanov", description = "The name of the advertiser")
    private String advertiserName;

    @Schema(name = "advertiserEmail", example = "ivanov@example.com", description = "The email of the advertiser")
    private String advertiserEmail;

    @Schema(name = "advertiserPhone", example = "+111111111111", description = "The phone number of the advertiser")
    private String advertiserPhone;

    @Schema(name = "discount", example = "10%", description = "The discount offered by the advertisement")
    private String discount;

    @Schema(name = "descriptionOfTheCoupon", example = "Get 10% off on your next purchase", description = "The description of the coupon offered in the advertisement")
    private String descriptionOfTheCoupon;

    @Schema(name = "createData", example = "2024-07-10", description = "The creation date of the advertisement")
    private LocalDate createData;

    @Schema(name = "endData", example = "2024-12-31", description = "The end date of the advertisement")
    private LocalDate endData;
}
