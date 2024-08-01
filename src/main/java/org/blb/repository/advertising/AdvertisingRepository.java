package org.blb.repository.advertising;

import org.blb.models.advertising.Advertising;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisingRepository extends JpaRepository<Advertising, Long> {
    Optional<Advertising> findByAdvertiserEmail(String email);

    @Query("SELECT a FROM Advertising a WHERE a.endData > :endDate AND a.advertisingCounter > :counter")
    List<Advertising> findAllForThoseWhoHaveNotExpiredAndCounterBiggerZero(@Param("endDate") LocalDate endDate, @Param("counter") Integer counter);
}
