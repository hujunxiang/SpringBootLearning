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

#集成AOP，记录日志信息

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
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                SystemLog log = new SystemLog();
                log = getControllerMethodDescription(joinPoint, request, log);
                systemLogDao.save(log);
                logger.info(log.toString());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("record log error:{}", e.getMessage());
            }
        }
    
        public static SystemLog getControllerMethodDescription(JoinPoint joinPoint, HttpServletRequest request, SystemLog log) throws Exception {
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
        public Page<Account> findAccountByConditions(@RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                                                     @RequestParam(value = "pageNum", defaultValue = "0", required = true) int pageNum) {
            return accountService.findAccountByConditions(pageNum, pageSize);
        }
       	 