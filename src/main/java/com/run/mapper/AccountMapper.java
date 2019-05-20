package com.run.mapper;

import com.github.pagehelper.Page;
import com.run.model.Account;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    Account findAccountById(int id);

    List<Account> findAccountByConditions(Account account);
}
