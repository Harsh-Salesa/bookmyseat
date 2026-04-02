package com.bms.bookmyseat.repository;
import com.bms.bookmyseat.entity.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    boolean existsByName(String name);


    Page<Theater> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String name,
            String location,
            Pageable pageable
    );

}