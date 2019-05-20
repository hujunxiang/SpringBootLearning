# 集成mybatis
个人见解：springboot框架中本身可以使用jpa进行增、删、改、查，但是由于在复杂查询的时候，使用jpa查询会写很多代码，而且维护起来不太方便，所以引入mybatis
进行复杂查询，毕竟是在xml文件中写native sql，这给后期优化查询sql带来极大便利。所以建议mybatis只做复杂查询，jpa做增、删、改及简单查询。这样也可以避免数据库事务的
多重复杂配置。

 
1.pom.xml文件引入依赖

    <!-- import druid-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.0</version>
    </dependency>
    <!-- import mybatis -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.2</version>
    </dependency>
    <!-- import PageHelper -->
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-starter</artifactId>
        <version>1.2.10</version>
    </dependency>
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-autoconfigure</artifactId>
        <version>1.2.10</version>
    </dependency>
2.application.properties添加mybatis配置项
    
    #mybatis
    mybatis.mapper-locations=classpath*:mapper/*Mapper.xml
    mybatis.type-aliases-package=com.run.mapper
    mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
3.新建mapper
    
    @Mapper
    public interface AccountMapper {
        Account findAccountById(int id);
        List<Account> findAccountByConditions(Account account);
    }
4.service层调用mapper
    
    @Autowired
    private AccountMapper accountMapper;
    
    public Account findAccountByIdUseMyBatis(int id) {
        return accountMapper.findAccountById(id);
    }

    public List<Account> findAccountList(int pageSize, int pageNum, Account account) {
        com.github.pagehelper.Page<Account> page = PageHelper.startPage(pageNum, pageSize);
        page.setReasonable(true);//设置合理化
        accountMapper.findAccountByConditions(account);
        return page.getResult();
    }
 5.controller层调用
    
    @Autowired
    private AccountService accountService;
    
    /**
     * 使用mybatis查询单条记录
     *
     * @param id
     * @return
     */
    @RequestMapping("findAccountByIdUseMyBatis/{id}")
    public Account findAccountByIdUseMyBatis(@PathVariable int id) {
        return accountService.findAccountByIdUseMyBatis(id);
    }

    /**
     * 使用PageHelper进行分页查询
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping("findAccountList")
    public List<Account> findAccountList(int pageSize, int pageNum,Account account) {
        return accountService.findAccountList(pageSize, pageNum, account);
    }