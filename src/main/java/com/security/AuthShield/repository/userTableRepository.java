package com.security.AuthShield.repository;

import com.security.AuthShield.model.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userTableRepository extends JpaRepository<users,String>{
}
