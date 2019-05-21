package com.run.model;

import lombok.Data;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

/**
 * Account实体类
 */
@Entity
@Data
public class Account implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

}
