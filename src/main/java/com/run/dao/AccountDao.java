package com.run.dao;

import com.run.model.Account;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDao extends JpaRepository<Account, Integer> {
    @Query("from Account where id = ?1")
    Account findAccountById(int id);

    @Query("from Account where name like concat('%',?1,'%') ")
    @Cacheable(value = "myCache")
    //将查询结果缓存到redis中
    List<Account> getAccountListFormRedisOrDB(String name, String email);

    @Query("from Account where name like concat('%',?1,'%') ")
    @Cacheable(value = "myCache", key = "#p0")
    //将查询结果缓存到redis中，（key="#p0"）指定传入的第一个参数作为redis的key。
    List<Account> getAccountListFormRedisOrDBByName(String name);

    @Query("from Account where email like concat('%',?1,'%') ")
    @CacheEvict(value = "myCache")
    //用来标注在需要清除缓存元素的方法或类上的
    List<Account> getAccountListFormRedisOrDBByEmail(String email);
}
