
1.搭建springboot环境（使用mysql数据库）

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
    (3).在pom.xml文件中添加相关依赖
    
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
2.集成mybatis
    
    
    (2).
