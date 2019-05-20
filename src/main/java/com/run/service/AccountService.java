package com.run.service;

import com.run.dao.AccountDao;
import com.run.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service层
 */
@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountDao accountDao;

    public List<Account> getAllAccount() {
        return accountDao.findAll();
    }

    public int saveAccount(Account account) {
        return accountDao.save(account).getId();
    }

    public Account findAccountById(int id) {
        return accountDao.findAccountById(id);
    }

    public void deleteAccountById(Integer id) {
        accountDao.deleteById(id);
    }

    public Page<Account> findAccountByConditions(int pageNum, int pageSize) {
        Pageable page = PageRequest.of(pageNum,pageSize);
        Page<Account> result = accountDao.findAll(page);
        return result;
    }
}
