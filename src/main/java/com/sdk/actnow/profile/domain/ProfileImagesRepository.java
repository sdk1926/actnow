package com.sdk.actnow.profile.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImagesRepository extends JpaRepository<ProfileImages,Long> {
    Optional<ProfileImages> findById(Long id);
}
