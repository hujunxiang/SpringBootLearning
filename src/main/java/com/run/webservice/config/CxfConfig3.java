package com.run.webservice.config;

import com.run.webservice.WebServiceService3;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfConfig3 {
    @Autowired
    private WebServiceService3 webServiceService3;

    @Bean
    public SpringBus bus3() {
        SpringBus bus = new SpringBus();
        bus.setId("version3");
        return bus;
    }

    /**
     * 设置cxf访问路径，cxf默认的是services（可见ServletController），http://localhost:8081/services/ws3?wsdl
     * 此处将其设置为myWs http://localhost:8081/myWs3?ws3.wsdl
     * @return
     */
    @Bean
    public ServletRegistrationBean cxfServlet3(){
        CXFServlet servlet3 = new CXFServlet();
        servlet3.setBus(bus3());
        ServletRegistrationBean bean = new ServletRegistrationBean(servlet3,"/myWs3/*");
        bean.setName("bean3");
        return bean;
    }

    @Bean
    public Endpoint endpoint3() {
        EndpointImpl endpoint = new EndpointImpl(bus3(), webServiceService3);
        endpoint.publish("/ws3");
        return endpoint;
    }
}