package com.run.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class SystemLog implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id", length = 32)
    private String id;
    /**
     * 模块名称
     */
    @Column(name = "module_name",length = 100)
    private String moduleName;
    /**
     * 描述
     */
    @Column(name = "description",length = 2000)
    private String description;
    /**
     * 操作类型
     */
    @Column(name = "opt_type",length = 10)
    private String optType;
    /**
     * ip
     */
    @Column(name = "ip",length = 20)
    private String ip;
    /**
     * 方法名称
     */
    @Column(name = "method_url",length = 100)
    private String methodUrl;
    /**
     * 参数
     */
    @Column(name = "params",length = 200)
    private String params;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date")
    private Date createDate;
}
