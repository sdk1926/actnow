package com.sdk.actnow.profile.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {
    List<ProfileImage> findAllByProfileId(long id);
}
