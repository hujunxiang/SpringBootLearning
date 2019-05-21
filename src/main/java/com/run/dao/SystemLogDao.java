package com.run.dao;

import com.run.model.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogDao extends JpaRepository<SystemLog,String> {
}
