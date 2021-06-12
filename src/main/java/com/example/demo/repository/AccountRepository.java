package com.example.demo.repository;


import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

     @Query("SELECT a FROM Account a WHERE a.phone = ?1 " )
     Account findAccountByPhone (String phoneNumber);


}
