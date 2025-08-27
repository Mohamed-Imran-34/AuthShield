package com.security.AuthShield.repository;


import com.security.AuthShield.model.usersLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface userLogRepository extends JpaRepository<usersLog,Integer> {

    @Query (nativeQuery = true , value = "SELECT * FROM users_log ORDER BY time DESC")
    List<usersLog> findByAllOrder();
}
