
# 搭建springboot环境（使用mysql数据库）

    (1).在GitHub上创建repository
    (2).在本地用idea创建项目，然后上传到GitHub
      (a).cd 到项目内，执行：git init
      (b).将项目上所有的文件添加到仓库中，执行：git add .
      (c).添加提交的注释，执行：git commit -m "init project"
      (d).将本地资源库关联到GitHub上，执行：git remote add origin https://github.com/hujunxiang/SpringBootLearning.git
      (e).将代码上传到GitHub，执行：git pull --rebase origin master和 git push -u origin master
      (f).创建一个名为springboot_mybatis的分支
        (aa).先将项目clone到本地，执行：git clone https://github.com/hujunxiang/SpringBootLearning.git
        (bb).关联GitHub，执行：git remote add origin https://github.com/hujunxiang/SpringBootLearning.git
        (cv).创建分支，执行：git branch springboot_mybatis
        (dd).切换分支，执行：git checkout springboot_mybatis
        (ee).查看分支，执行：git branch -a 或者git branch -r
        (ff).将分支推送到GitHub，执行： git push origin springboot_mybatis
        (gg).删除本地分支，执行：git branch -d springboot_mybatis
        (hh).删除GitHub上的分支，执行：git push origin:springboot_mybatis
        
    在pom.xml文件中添加相关依赖
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- import mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
        </dependency>
        <!--import lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
    配置application.properties文件内容
        debug=false
        # running on port 8081
        server.port=8081
        spring.jpa.show-sql=true
        #create、drop、update、validate
        spring.jpa.hibernate.ddl-auto=update
        #configure jpa database mysql engine InnoDB,default MyISAM
        spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&characterEncoding=utf-8
        spring.datasource.username=root
        spring.datasource.password=root
        #not allow for lazy loading in web views
        spring.jpa.open-in-view=false
        
    创建entity
        @Entity
        @Data
        public class Account {
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
        
    创建dao
        @Repository
        public interface AccountDao extends JpaRepository<Account,Integer> {
            @Query("from Account where id = ?1")
            Account findAccountById(int id);
        }
        
    创建service
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
        
    创建Controller
        @RestController
        @RequestMapping("test")
        public class AccountController {
            @Autowired
            private AccountService accountService;
            /**
             * 获取所有account信息
             *
             * @return
             */
            @RequestMapping("getAllAccount")
            public List<Account> getAllAccount() {
                return accountService.getAllAccount();
            }
            /**
             * 分页查询
             *
             * @param pageSize 每页大小
             * @param pageNum  第几页
             * @return
             */
            @RequestMapping("findAccountByConditions")
            public Page<Account> findAccountByConditions(
                @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                @RequestParam(value = "pageNum", defaultValue = "0", required = true) int pageNum) {
                    return accountService.findAccountByConditions(pageNum, pageSize);
            }
            /**
             * 初始化account信息
             *
             * @return
             */
            @RequestMapping(value = "initAccount/{index}", method = RequestMethod.GET)
            public String initAccount(@PathVariable int index) {
                List<Integer> idList = new LinkedList<>();
                for (int i = index; i < index + 100; i++) {
                    Account account = new Account();
                    account.setName("张三" + i);
                    account.setEmail("90598269" + i + "@qq.com");
                    accountService.saveAccount(account);
                    idList.add(account.getId());
                }
                return idList.toString();
            }
            @RequestMapping("findAccountById")
            public String findAccountById(int id) {
                Account account = accountService.findAccountById(id);
                if (StringUtils.isEmpty(account)) {
                    return "can not find record by id = " + id;
                } else {
                    return account.getId().toString();
                }
            }
            @RequestMapping("deleteAccountById")
            public String deleteAccountById(int id) {
                String flag = "success";
                try {
                    accountService.deleteAccountById(id);
                } catch (Exception e) {
                    flag = "fail";
                    e.printStackTrace();
                }
                return flag;
            }
        }
