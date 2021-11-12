package com.sdk.actnow.profile.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<Specialty,Long> {
    List<Specialty> findAllByProfileId(long profileId);
}
