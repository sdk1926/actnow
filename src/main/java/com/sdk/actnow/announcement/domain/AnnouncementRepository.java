package com.sdk.actnow.announcement.domain;

import com.sdk.actnow.oauth.domain.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
    Optional<Announcement> findByUser(Users user);
    Optional<Announcement> findById(Long id);
    Page<Announcement> findAllByUser(Pageable pageable, Users users);
}
