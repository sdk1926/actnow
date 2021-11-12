package com.sdk.actnow.profile.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career,Long> {
    List<Career> findAllByProfileId(long id);
}
