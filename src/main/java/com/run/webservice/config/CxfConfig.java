package com.run.webservice.config;

import com.run.webservice.WebServiceService;
import com.run.webservice.WebServiceService2;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfConfig {
    @Autowired
    private WebServiceService webServiceService;
    @Autowired
    private WebServiceService2 webServiceService2;

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus bus() {
        SpringBus bus = new SpringBus();
        bus.setId("version1");
        return bus;
    }

    /**
     * 设置cxf访问路径，cxf默认的是services（可见ServletController），http://localhost:8081/services/ws?wsdl
     * 此处将其设置为myWs http://localhost:8081/myWs?ws.wsdl
     * @return
     */
    @Bean
    public ServletRegistrationBean cxfServlet(){
        CXFServlet servlet = new CXFServlet();
        servlet.setBus(bus());
        ServletRegistrationBean bean = new ServletRegistrationBean(servlet,"/myWs/*");
        bean.setName("bean1");
        return bean;
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus(), webServiceService);
        endpoint.publish("/ws");
        return endpoint;
    }

    @Bean
    public Endpoint endpoint2(){
        EndpointImpl endpoint = new EndpointImpl(bus(), webServiceService2);
        endpoint.publish("/ws2");
        return endpoint;
    }
}
