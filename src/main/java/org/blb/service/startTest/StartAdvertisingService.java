package org.blb.service.startTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.blb.DTO.advertisingDto.AdvertisingRequestDto;
import org.blb.models.advertising.Advertising;
import org.blb.repository.advertising.AdvertisingRepository;
import org.blb.service.util.AdvertisingConverter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor

public class StartAdvertisingService {
    private final AdvertisingRepository advertisingRepository;
    private final AdvertisingConverter advertisingConverter;
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void createAdvertising() {
//        for (int i = 0; i < 10; i++) {
//            AdvertisingRequestDto advertisingRequestDto = new AdvertisingRequestDto(
//                    "Advertisement " + i,
//                    "Description for advertisement " + i,
//                    "Company " + i,
//                    "advertising" + i + "@gmail.com",
//                    "+11111111111" + i,
//                    (i * 2) + "%",
//                    LocalDate.of(2024, 12, 31),
//                    "Coupon description " + i
//
//            );
//          //  System.out.println(advertisingRequestDto);
//            Advertising advertising = advertisingConverter.fromDtoToEntity(advertisingRequestDto);
////            advertising.setAdvertiserEmail("ad@gmail.com");
//    //        System.out.println(advertising);
//            advertisingRepository.save(advertising);
//        }
//

       // advertisingRepository.deleteAll();

        entityManager.createNativeQuery("drop TABLE advertising ").executeUpdate();
        entityManager.createNativeQuery("create table advertising (id bigint not null auto_increment, description_of_the_coupon varchar(255), advertiser_email varchar(255), advertiser_name varchar(255), advertiser_phone varchar(255), advertising_counter integer, create_data date, description varchar(700), discount varchar(255), end_data date, title varchar(255), primary key (id))").executeUpdate();
//create table advertising (id bigint not null auto_increment, description_of_the_coupon varchar(255), advertiser_email varchar(255), advertiser_name varchar(255), advertiser_phone varchar(255), advertising_counter integer, create_data date, description varchar(700), discount varchar(255), end_data date, title varchar(255), primary key (id))
        // Реклама 1
        Advertising advertising1 = new Advertising();
        advertising1.setId(1L);
        advertising1.setTitle("Entwicklung von Websites");
        advertising1.setDescription("Unser Team entwickelt Websites jeglicher Komplexität, wir bieten einen vollständigen Entwicklungszyklus vom Design über die Implementierung bis hin zum Support, wir erstellen Websites, die auf allen Geräten - vom Mobiltelefon bis zum Desktop-Computer - perfekt dargestellt werden, wir optimieren Websites für eine hohe Ladegeschwindigkeit und einen reibungslosen Betrieb auch bei hoher Belastung. Wir bieten einen 24/7-Support und eine Überwachung der Website-Performance an. Wir bieten flexible Kooperationsbedingungen, einschließlich verschiedener Zahlungsoptionen und Preismodelle, die an Ihr Budget und Ihre Anforderungen angepasst werden können.");
        advertising1.setAdvertiserName("Digitales Team");
        advertising1.setAdvertiserEmail("DigitalesTeam@gmail.com");
        advertising1.setAdvertiserPhone("+4911111111111");
        advertising1.setDiscount("10%");
        advertising1.setCreateData(LocalDate.now());
        advertising1.setEndData(LocalDate.of(2050, 10, 24));
        advertising1.setAdvertisingCounter(100);
        advertising1.setDescriptionOfTheCoupon("Wenn Sie sich für unser Team entscheiden, erhalten Sie einen Rabatt auf die Website-Entwicklung " + advertising1.getDiscount());
        advertisingRepository.save(advertising1);

// Реклама 2
        Advertising advertising2 = new Advertising();
        advertising2.setTitle("Unsere Firma 2");
        advertising2.setDescription("Beschreibung für die zweite Werbung.");
        advertising2.setAdvertiserName("Werbefirma 2");
        advertising2.setAdvertiserEmail("werbung2@GMAIL.com");
        advertising2.setAdvertiserPhone("+49123456702");
        advertising2.setDiscount("15%");
        advertising2.setCreateData(LocalDate.now());
        advertising2.setEndData(LocalDate.of(2024, 11, 30));
        advertising2.setAdvertisingCounter(150);
        advertising2.setDescriptionOfTheCoupon("15% Rabatt erhalten");
        advertisingRepository.save(advertising2);

// Реклама 3
        Advertising advertising3 = new Advertising();
        advertising3.setTitle("Unsere Firma 3");
        advertising3.setDescription("Dies ist eine Beschreibung für die dritte Werbung.");
        advertising3.setAdvertiserName("Werbefirma 3");
        advertising3.setAdvertiserEmail("werbung3@GMAIL.com");
        advertising3.setAdvertiserPhone("+49123456703");
        advertising3.setDiscount("20%");
        advertising3.setCreateData(LocalDate.now());
        advertising3.setEndData(LocalDate.of(2024, 9, 15));
        advertising3.setAdvertisingCounter(200);
        advertising3.setDescriptionOfTheCoupon("Erhalten Sie 20% Rabatt auf Ihren Einkauf");
        advertisingRepository.save(advertising3);

// Реклама 4
        Advertising advertising4 = new Advertising();
        advertising4.setTitle("Unsere Firma 4");
        advertising4.setDescription("Hier ist die vierte Werbung für Ihr Unternehmen.");
        advertising4.setAdvertiserName("Werbefirma 4");
        advertising4.setAdvertiserEmail("werbung4@GMAIL.com");
        advertising4.setAdvertiserPhone("+49123456704");
        advertising4.setDiscount("25%");
        advertising4.setCreateData(LocalDate.now());
        advertising4.setEndData(LocalDate.of(2024, 12, 31));
        advertising4.setAdvertisingCounter(250);
        advertising4.setDescriptionOfTheCoupon("25% Rabatt für alle neuen Kunden");
        advertisingRepository.save(advertising4);

// Реклама 5
        Advertising advertising5 = new Advertising();
        advertising5.setTitle("Unsere Firma 5");
        advertising5.setDescription("Fünfte Werbung für die neuen Angebote.");
        advertising5.setAdvertiserName("Werbefirma 5");
        advertising5.setAdvertiserEmail("werbung5@GMAIL.com");
        advertising5.setAdvertiserPhone("+49123456705");
        advertising5.setDiscount("30%");
        advertising5.setCreateData(LocalDate.now());
        advertising5.setEndData(LocalDate.of(2024, 8, 20));
        advertising5.setAdvertisingCounter(300);
        advertising5.setDescriptionOfTheCoupon("Sichern Sie sich 30% Rabatt auf ausgewählte Artikel");
        advertisingRepository.save(advertising5);

// Реклама 6
        Advertising advertising6 = new Advertising();
        advertising6.setTitle("Unsere Firma 6");
        advertising6.setDescription("Einmalige Gelegenheit für die sechste Werbung.");
        advertising6.setAdvertiserName("Werbefirma 6");
        advertising6.setAdvertiserEmail("werbung6@GMAIL.com");
        advertising6.setAdvertiserPhone("+49123456706");
        advertising6.setDiscount("35%");
        advertising6.setCreateData(LocalDate.now());
        advertising6.setEndData(LocalDate.of(2024, 7, 14));
        advertising6.setAdvertisingCounter(350);
        advertising6.setDescriptionOfTheCoupon("35% Rabatt auf Ihre erste Bestellung");
        advertisingRepository.save(advertising6);

// Реклама 7
        Advertising advertising7 = new Advertising();
        advertising7.setTitle("Unsere Firma 7");
        advertising7.setDescription("Beschreibung für die siebte Werbung.");
        advertising7.setAdvertiserName("Werbefirma 7");
        advertising7.setAdvertiserEmail("werbung7@GMAIL.com");
        advertising7.setAdvertiserPhone("+49123456707");
        advertising7.setDiscount("40%");
        advertising7.setCreateData(LocalDate.now());
        advertising7.setEndData(LocalDate.of(2024, 12, 1));
        advertising7.setAdvertisingCounter(400);
        advertising7.setDescriptionOfTheCoupon("40% Rabatt auf alle Produkte");
        advertisingRepository.save(advertising7);

// Реклама 8
        Advertising advertising8 = new Advertising();
        advertising8.setTitle("Unsere Firma 8");
        advertising8.setDescription("Achte Werbung für die Saison.");
        advertising8.setAdvertiserName("Werbefirma 8");
        advertising8.setAdvertiserEmail("werbung8@GMAIL.com");
        advertising8.setAdvertiserPhone("+49123456708");
        advertising8.setDiscount("45%");
        advertising8.setCreateData(LocalDate.now());
        advertising8.setEndData(LocalDate.of(2024, 12, 18));
        advertising8.setAdvertisingCounter(0);
        advertising8.setDescriptionOfTheCoupon("Nutzen Sie 45% Rabatt für begrenzte Zeit");
        advertisingRepository.save(advertising8);

// Реклама 9
        Advertising advertising9 = new Advertising();
        advertising9.setTitle("Unsere Firma 9");
        advertising9.setDescription("Werben Sie mit unserer neunten Anzeige.");
        advertising9.setAdvertiserName("Werbefirma 9");
        advertising9.setAdvertiserEmail("werbung9@GMAIL.com");
        advertising9.setAdvertiserPhone("+49123456709");
        advertising9.setDiscount("50%");
        advertising9.setCreateData(LocalDate.now());
        advertising9.setEndData(LocalDate.of(2024, 11, 30));
        advertising9.setAdvertisingCounter(500);
        advertising9.setDescriptionOfTheCoupon("50% Rabatt auf alles");
        advertisingRepository.save(advertising9);

// Реклама 10
        Advertising advertising10 = new Advertising();
        advertising10.setTitle("Unsere Firma 10");
        advertising10.setDescription("Zehnte Werbung für Ihre Aufmerksamkeit.");
        advertising10.setAdvertiserName("Werbefirma 10");
        advertising10.setAdvertiserEmail("werbung10@GMAIL.com");
        advertising10.setAdvertiserPhone("+49123456710");
        advertising10.setDiscount("55%");
        advertising10.setCreateData(LocalDate.now());
        advertising10.setEndData(LocalDate.of(2024, 10, 22));
        advertising10.setAdvertisingCounter(550);
        advertising10.setDescriptionOfTheCoupon("Erhalten Sie 55% Rabatt auf Ihren nächsten Einkauf");
        advertisingRepository.save(advertising10);

    }
}
