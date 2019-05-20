package com.run.dao;

import com.run.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDao extends JpaRepository<Account,Integer> {
    @Query("from Account where id = ?1")
    Account findAccountById(int id);
}
