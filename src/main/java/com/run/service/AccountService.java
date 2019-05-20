package com.run.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.run.dao.AccountDao;
import com.run.mapper.AccountMapper;
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
    @Autowired
    private AccountMapper accountMapper;

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
        Pageable page = PageRequest.of(pageNum, pageSize);
        Page<Account> result = accountDao.findAll(page);
        return result;
    }

    public Account findAccountByIdUseMyBatis(int id) {
        return accountMapper.findAccountById(id);
    }

    public List<Account> findAccountList(int pageSize, int pageNum, Account account) {
        com.github.pagehelper.Page<Account> page = PageHelper.startPage(pageNum, pageSize);
        page.setReasonable(true);//设置合理化
        accountMapper.findAccountByConditions(account);
        return page.getResult();
    }
}
