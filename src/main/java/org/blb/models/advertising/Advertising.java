package org.blb.models.advertising;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Advertising {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String advertiserName;
    private String advertiserEmail;
    private String advertiserPhone;
    private String discount;
    private LocalDate createData;
    private LocalDate endData;
    private Integer advertisingCounter;
    private String DescriptionOfTheCoupon;

}
