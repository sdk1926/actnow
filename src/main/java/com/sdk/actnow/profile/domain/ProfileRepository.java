package com.sdk.actnow.profile.domain;

import com.sdk.actnow.oauth.domain.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Page<Profile> findAll(Pageable pageable);
    Optional<Profile> findByUser(Users user);
    Optional<Profile> findById(long id);
}
