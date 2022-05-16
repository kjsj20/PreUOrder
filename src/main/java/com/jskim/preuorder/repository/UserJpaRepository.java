package com.jskim.preuorder.repository;

import com.jskim.preuorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
