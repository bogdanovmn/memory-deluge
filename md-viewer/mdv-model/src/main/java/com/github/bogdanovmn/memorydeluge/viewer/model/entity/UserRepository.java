package com.github.bogdanovmn.memorydeluge.viewer.model.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findFirstByName(String name);

    User findFirstByEmail(String email);

}
