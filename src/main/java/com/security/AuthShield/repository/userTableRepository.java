package com.security.AuthShield.repository;

import com.security.AuthShield.model.userDtoAdmin;
import com.security.AuthShield.model.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface userTableRepository extends JpaRepository<users,String>{

    @Query(nativeQuery = true,value = "SELECT email,username,is_account_verified FROM users_table ORDER BY created")
    List<userDtoAdmin> findByAdmin();
}
