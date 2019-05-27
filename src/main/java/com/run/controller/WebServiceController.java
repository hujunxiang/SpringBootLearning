package com.run.controller;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ws")
public class WebServiceController {

    @RequestMapping("callWs/{message}")
    public String callWs(@PathVariable String message){
        String wsUrl = "http://localhost:8081/myWs/ws?wsdl";
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = clientFactory.createClient(wsUrl);
        Object[] objs = new Object[0];
        try {
            objs = client.invoke("sayHello",message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs[0].toString();
    }
    @RequestMapping("callWs2/{message}")
    public String callWs2(@PathVariable String message){
        String wsUrl = "http://localhost:8081/myWs/ws2?wsdl";
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = clientFactory.createClient(wsUrl);
        Object[] objs = new Object[0];
        try {
            objs = client.invoke("sayHello2",message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs[0].toString();
    }
    @RequestMapping("callWs3/{message}")
    public String callWs3(@PathVariable String message){
        String wsUrl = "http://localhost:8081/myWs3/ws3?wsdl";
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = clientFactory.createClient(wsUrl);
        Object[] objs = new Object[0];
        try {
            objs = client.invoke("sayHello3",message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs[0].toString();
    }
}
