package com.indiagold.notificationhub.repository;

import com.indiagold.notificationhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
