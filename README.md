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

# 集成mybatis
个人见解：springboot框架中本身可以使用jpa进行增、删、改、查，但是由于在复杂查询的时候，使用jpa查询会写很多代码，而且维护起来不太方便，所以引入mybatis进行复杂查询，毕竟是在xml文件中写native sql，这给后期优化查询sql带来极大便利。所以建议mybatis只做复杂查询，jpa做增、删、改及简单查询。
这样也可以避免数据库事务的多重复杂配置。

 
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

# 集成AOP，记录日志信息

 1.pom.xml文件引入依赖
    
    <!-- import spring-boot-starter-aop -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
        <version>2.1.5.RELEASE</version>
    </dependency>
    <!-- automatic restart dev时支持热部署 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <version>2.1.5.RELEASE</version>
        <optional>true</optional>
    </dependency> 
    
 2.创建aop切面相关信息
 
    AopLogAspect类：
    @Aspect
    @Configuration
    public class AopLogAspect {
        private static Logger logger = LoggerFactory.getLogger(AopLog.class);
    
        @Autowired
        private SystemLogDao systemLogDao;
    
        @Pointcut("@annotation(com.run.aop.AopLog)")
        public void controllerAspect() {
        }
    
        @After("controllerAspect()")
        public void doAfter(JoinPoint joinPoint) {
            try {
                logger.info("start record log ...");
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.
                    getRequestAttributes()).getRequest();
                SystemLog log = new SystemLog();
                log = getControllerMethodDescription(joinPoint, request, log);
                systemLogDao.save(log);
                logger.info(log.toString());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("record log error:{}", e.getMessage());
            }
        }
    
        public static SystemLog getControllerMethodDescription(JoinPoint joinPoint, 
                HttpServletRequest request, SystemLog log) 
            throws Exception {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methodArr = targetClass.getMethods();
            String description = "";
            String moduleName = "";
            String optType = "";
            String methodUrl = request.getRequestURI();
            String ip = CommonUtils.getIpAddr(request);
            String params = "";
            ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
            for (Method mt : methodArr) {
                if (mt.getName().equals(methodName)) {
                    Class[] clz = mt.getParameterTypes();
                    if (clz.length == arguments.length) {
                        String[] parameterNames = pnd.getParameterNames(mt);
                        Map<String, Object> paramMap = new HashMap<>();
                        for (int i = 0; i < parameterNames.length; i++) {
                            paramMap.put(parameterNames[i], arguments[i]);
                        }
                        params = paramMap.toString();
                        description = mt.getAnnotation(AopLog.class).description();
                        moduleName = mt.getAnnotation(AopLog.class).moduleName();
                        optType = mt.getAnnotation(AopLog.class).operType();
                        break;
                    }
                }
            }
            log.setParams(params);
            log.setMethodUrl(methodUrl);
            log.setIp(ip);
            log.setDescription(description);
            log.setOptType(optType);
            log.setModuleName(moduleName);
            return log;
        }
    }
    
    AopLog类：
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface AopLog {
        /**
         * 操作内容
         */
        String description();
        /**
         * 操作模块名称
         */
        String moduleName();
        /**
         * 操作类型 : 见OptType
         */
        String operType();
    }
    
    OptType类：
    /**
     *  操作类型
     **/
    public class OptType {
    
    	/**
    	 * 保存
    	 */
    	public static final String SAVE = "SAVE";
    	/**
    	 * 修改
    	 */
    	public static final String EDIT = "EDIT";
    	/**
    	 * 删除
    	 */
    	public static final String DELETE = "DEL";
    	/**
    	 * 查询
    	 */
    	public static final String QUERY = "QUERY";
    	/**
    	 * 审核
    	 */
    	public static final String CHECK = "AUDIT";
    	/**
    	 * 登录
    	 */
    	public static final String LOGIN = "LOGIN";
    	/**
    	 * 退出
    	 */
    	public static final String LOGOUT = "LOGOUT";
    	/**
    	 * 维护
    	 */
    	public static final String MAINTAIN= "MAINTAIN";
    	/**
    	 * 申请
    	 */
    	public static final String APPLY = "APPLY";
    	/**
    	 * 导出
    	 */
    	public static final String EXPORT = "EXPORT";
    	/**
    	 * 导入
    	 */
    	public static final String IMPORT = "IMPORT";
    	/**
    	 * 打印
    	 */
    	 
 3.controller层使用：
 
    使用@AopLog自定义注解即可：
    
        /**
         * 分页查询
         *
         * @param pageSize 每页大小
         * @param pageNum  第几页
         * @return
         */
        @RequestMapping("findAccountByConditions")
        @AopLog(moduleName = "查询Account列表",description = "分页查询列表信息",operType = OptType.QUERY)
        public Page<Account> findAccountByConditions(
            @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "0", required = true) int pageNum) {
            return accountService.findAccountByConditions(pageNum, pageSize);
        }
       	 
# 集成redis

  1.pom.xml文件中添加依赖：
    
    <!-- import redis-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
        <version>2.1.5.RELEASE</version>
    </dependency> 
    
  2.application.properties文件中添加配置项：
  
        #redis
        spring.redis.database=0
        spring.redis.host=127.0.0.1
        spring.redis.port=6379
        spring.redis.password=
        spring.redis.jedis.pool.max-wait=-1ms
        spring.redis.jedis.pool.max-idle=8
        spring.redis.jedis.pool.min-idle=0
        spring.redis.timeout=1800
  3.修改启动入口类DemoApplication，添加上@EnableCaching，开启缓存功能
  
  4.编写redis配置类：
  
        /**
         * Redis缓存配置类
         */
        @Configuration
        @EnableCaching//开启缓存功能
        @EnableTransactionManagement//开启事务
        public class RedisConfig extends CachingConfigurerSupport {
        
            private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);
        
            @Value("${spring.redis.host}")
            private String host;
            @Value("${spring.redis.port}")
            private String port;
            @Value("${spring.redis.timeout}")
            private int timeout;
        
            /**
             * RedisTemplate 对象
             *
             * @param factory
             * @return
             */
            @Bean
            @ConditionalOnMissingBean
            public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
                logger.info(">>>>>>>>>>>>>rebuild redisTemplate  ...");
                RedisTemplate template = new RedisTemplate();
                template.setConnectionFactory(factory);
                Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
                ObjectMapper mapper = new ObjectMapper();
                mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                serializer.setObjectMapper(mapper);
                StringRedisSerializer redisSerializer = new StringRedisSerializer();
                //key采用String的序列化方式
                template.setKeySerializer(redisSerializer);
                //hash的key也采用String的序列化方式
                template.setHashKeySerializer(redisSerializer);
                //value序列化方式采用jackson
                template.setValueSerializer(redisSerializer);
                //hash的value序列化方式采用jackson
                template.setHashValueSerializer(redisSerializer);
                template.afterPropertiesSet();
                return template;
            }
        
            /**
             * 缓存管理器
             *
             * @param factory
             * @return
             */
            @Bean
            public CacheManager cacheManager(RedisConnectionFactory factory) {
                RedisSerializer<String> redisSerializer = new StringRedisSerializer();
                Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
                //解决查询缓存转换异常的问题
                ObjectMapper om = new ObjectMapper();
                om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                jackson2JsonRedisSerializer.setObjectMapper(om);
                // 配置序列化（解决乱码的问题）
                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                        //设置缓存管理器管理的缓存的默认过期时间
                        .entryTtl(Duration.ofSeconds(timeout))
                        //设置 key为string序列化
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                        //设置value为json序列化
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                        //不缓存空值
                        .disableCachingNullValues();
                RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                        .cacheDefaults(config)
                        .build();
                return cacheManager;
            }
        
            /**
             * 自定义的缓存key生成策略：方法名
             * 此处需要注意，如果加参数名，则controller层传参数不能使用@PathVariable注解传值，
             * 否则得到的是参数值而不是参数名称
             *
             * @return
             */
            @Bean
            public KeyGenerator keyGenerator() {
                return new KeyGenerator() {
                    @Override
                    public Object generate(Object target, Method method, Object... params) {
                        StringBuilder sb = new StringBuilder();
        //                sb.append(target.getClass().getName());
                        sb.append(method.getName());
        //                for(Object obj:params){
        //                    sb.append(obj.toString());
        //                }
                        logger.info(">>>>>>>>>>>>>rebuild keyGenerator ...");
                        logger.info(">>>>>>>>>>>>>cache key is :" + sb.toString());
                        return sb.toString();
                    }
                };
            }
        }
        
  5.redis工具类：RedisUtils
  
  6.在dao层或者service层使用@Cacheable或者@CacheEvict注解
    
    dao层使用：
    
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
  
  7.controller层调用：
  
    @RestController
    @RequestMapping("redis")
    public class RedisController {
        private static Logger logger = LoggerFactory.getLogger(RedisController.class);
    
        @Autowired
        private RedisUtils redisUtils;
        @Autowired
        private AccountService accountService;
    
        @RequestMapping("setInfo2Redis/{key}/{value}")
        public String setInfo2Redis(@PathVariable String key, @PathVariable String value) {
            redisUtils.set(key, value);
            return redisUtils.get(key) + ":" + value;
        }
    
        @RequestMapping("getInfoFromRedisByKey/{key}")
        public String getInfoFromRedisByKey(@PathVariable String key) {
            return "key:" + redisUtils.get(key);
        }
    
        @RequestMapping("delInfoFromRedisByKeys/{key}")
        public boolean delInfoFromRedisByKeys(@PathVariable String key) {
            return redisUtils.deleteByKeys(key);
        }
    
        @RequestMapping("getAccountListFormRedisOrDB/{name}/{email}")
        public List<Account> getAccountListFormRedisOrDB(@PathVariable String name, @PathVariable String email) {
            return accountService.getAccountListFormRedisOrDB(name, email);
        }
    
        @RequestMapping("getAccountListFormRedisOrDBByName")
        public List<Account> getAccountListFormRedisOrDBByName(String name) {
            return accountService.getAccountListFormRedisOrDBByName(name);
        }
    
        @RequestMapping("getAccountListFormRedisOrDBByEmail")
        public List<Account> getAccountListFormRedisOrDBByEmail(String email) {
            return accountService.getAccountListFormRedisOrDBByEmail(email);
        }
    }      
        
  8.可以查看打印日志，如果有查询 sql输出则表示查询数据库数据，如果没有则表示查询的是redis缓存数据。也可以
  通过使用RedisClient工具进行查看缓存中的数据信息。
  
# 集成ActiveMQ

    将从mq中获取的数据持久化到mysql数据库中

  1.pom.xml文件中引入依赖
  
    <!-- import activemq -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-activemq</artifactId>
        <version>2.1.5.RELEASE</version>
    </dependency>
  2.application.properties文件中添加activemq的配置项
  
    #activemq
    spring.activemq.broker-url=tcp://127.0.0.1:61616
    spring.activemq.user=admin
    spring.activemq.password=admin
    activemq.queue.name=myQueue
    activemq.topic.name=myTopic
  3.创建生产者类：
  
    @Component
    public class ActiveMQProducer {
    
        @Autowired
        private JmsTemplate jmsTemplate;
    
        @Value("${activemq.queue.name}")
        private String queueName;
    
        @Value("${activemq.topic.name}")
        private String topicName;
    
        /**
         * 发送消息
         *
         * @param isUseQueue 发生消息类型，true时使用queue，false时使用topic
         * @param message    待发送的消息
         */
        public void sendMessage(boolean isUseQueue, final String message) {
            Destination destination = null;
            if (isUseQueue) {
                destination = new ActiveMQQueue(queueName);
            } else {
                destination = new ActiveMQTopic(topicName);
            }
            jmsTemplate.setExplicitQosEnabled(true);//默认false，是否开启是否开启 deliveryMode, priority, timeToLive的配置
            jmsTemplate.setDeliveryMode(1);//设置是否持久化，1非持久化，2,持久化，默认2
            jmsTemplate.setTimeToLive(5000);//消息过期时间,单位为毫秒
            jmsTemplate.convertAndSend(destination, message);
        }
    }
  4.创建消费者类：
  
    @Component
    public class ActiveMQConsumer {
        private static Logger logger = LoggerFactory.getLogger(ActiveMQConsumer.class);
    
        @Autowired
        private SystemLogDao systemLogDao;
    
        /**
         * 接收queue里的消息并持久化到mysql数据库中
         * @param text
         */
        @JmsListener(destination = "myQueue")
        public void receiveQueueMsg(String text) {
            logger.info("收到Queue的报文为:" + text);
            SystemLog log = new SystemLog();
            log.setDescription(text);
            log.setCreateDate(new Date());
            log.setParams(text);
            log.setMethodUrl("myQueue");
            log.setOptType(OptType.SAVE);
            systemLogDao.save(log);
        }
    
        /**
         * JmsListener注解默认只接收queue消息,如果要接收topic消息,需要设置containerFactory
         *
         * @param factory
         * @return
         */
        @Bean
        public JmsListenerContainerFactory<?> topicContainerFactory(ConnectionFactory factory) {
            DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
            containerFactory.setPubSubDomain(true);
            containerFactory.setConnectionFactory(factory);
            return containerFactory;
        }
    
        /**
         * 接收topic里的消息并持久化到mysql数据库中
         * @param text
         */
        @JmsListener(destination = "myTopic", containerFactory = "topicContainerFactory")
        public void receiveTopicMsg(String text) {
            logger.info("收到Topic的报文为:" + text);
            SystemLog log = new SystemLog();
            log.setDescription(text);
            log.setCreateDate(new Date());
            log.setParams(text);
            log.setMethodUrl("myTopic");
            log.setOptType(OptType.SAVE);
            systemLogDao.save(log);
        }
    }
  5.在controller层调用：
  
    @RestController
    @RequestMapping("mq")
    public class ActiveMQController {
        private static Logger logger = LoggerFactory.getLogger(ActiveMQController.class);
    
        @Autowired
        private ActiveMQProducer producer;
    
        @RequestMapping("sendQueueMsg/{num}")
        public String sendQueueMsg(@PathVariable int num) {
            for (int i = 0; i < num; i++) {
                producer.sendMessage(true,"jack count num is :" + i);
            }
            return "send queue message to activemq success";
        }
    
        @RequestMapping("sendTopicMsg/{num}")
        public String sendTopicMsg(@PathVariable int num) {
            for (int i = 0; i < num; i++) {
                producer.sendMessage(false,"jack count num is :" + i);
            }
            return "send topic message to activemq success";
        }
    }
  
# 集成kafka

        在官网下载安装包，并解压（注意由于是在windows系统环境下，安装包所在路径不要过长，否则在执行相关bat时会报错导致启动失败）
    kafka安装包中自带zookeeper，所以不用下载zookeeper。到bin/windows目录下：
        启动服务，
            先启动zookeeper服务，执行：zookeeper-server-start.bat ../../config/zookeeper.properties
            再启动kafka服务，执行：kafka-server-start.bat ../../config/server.properties
        关闭服务：
            先关闭kafka服务，执行：kafka-server-stop.bat
            再关闭zookeeper服务，执行：zookeeper-server-stop.bat

    kafka的使用：
        创建topic：
            方法一、自动创建
                kafka-console-producer.bat --broker-list 127.0.0.1:9092 --topic test
            方法二、创建一个拥有3个副本,1个分区的topic
                kafka-topics.bat --create --zookeeper 127.0.0.1:2181 --replication-factor 1 -partitions 3 --topic test1
        删除topic：
            kafka-topics.bat --delete --zookeeper 127.0.0.1:2181 --topic test
        查看topic：
            查看topic列表
                kafka-topics.bat --zookeeper 127.0.0.1:2181 --list
            查看topic名称为test对应的具体信息﻿​
                kafka-topics.bat --describe --zookeeper 127.0.0.1:2181 --topic test
        启动生产者：
            kafka-console-producer.bat --broker-list 127.0.0.1:9092 --topic test
        启动消费者：
            kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test --from-beginning
            
    kafka可视化工具Kafka Toll下载地址：http://www.kafkatool.com/download.html，或者kafka-manager工具
    
  1.pom.xml文件中引入依赖：
  
    <!-- import kafka -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
        <version>2.2.6.RELEASE</version>
    </dependency>
  2.application.properties文件中添加配置项：
  
    #kafka
    spring.kafka.bootstrap-servers=localhost:9092
    spring.kafka.consumer.group-id=myGroup
  
  3.创建生产者：
        
        @Component
        public class KafkaProducer {
            private static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
        
            @Autowired
            private KafkaTemplate kafkaTemplate;
        
            @Value("${spring.kafka.consumer.group-id}")
            public String topicName;
        
            /**
             * 向kafka发送消息
             * @param obj
             */
            public void sendMsg(Object obj) {
                logger.info("准备发送消息为：{}", obj.toString());
                ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, obj);
                future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                    @Override
                    public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                        logger.info(topicName + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
                    }
        
                    @Override
                    public void onFailure(Throwable throwable) {
                        logger.info(topicName + " - 生产者 发送消息失败：" + throwable.getMessage());
                    }
                });
            }
        }
   
  4.创建消费者：
    
        @Component
        public class KafkaConsumer {
            private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
        
            /**
             * 从kafka接受消息
             * @param record
             * @param topic
             */
            @KafkaListener(topics = "myGroupId", groupId = "myGroupId")
            public void receiveMsg(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
                logger.info("myGroupId start receive msg ...");
                Optional message = Optional.ofNullable(record.value());
                if (message.isPresent()) {
                    Object msg = message.get();
                    logger.info("被myGroupId消费了：Topic:" + topic + ",Record:" + record + ",Message:" + msg);
                }
            }
        }  
  5.controller层调用：
        
        @RestController
        @RequestMapping("kafka")
        public class KafkaController {
            private static Logger logger = LoggerFactory.getLogger(KafkaController.class);
        
            @Autowired
            private KafkaProducer producer;
        
            /**
             * 向kafka发送消息
             * @return
             */
            @RequestMapping("sendMsg")
            public String sendMsg() {
                Random r = new Random();
                int num = r.nextInt(100) + 1;
                for (int i = 0; i < num; i++) {
                    producer.sendMsg("hello kafka , i am producer " + i);
                }
                return "producer get total num is :" + num;
            }
        } 