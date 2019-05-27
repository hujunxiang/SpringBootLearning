package com.run.webservice.impl;

import com.run.webservice.WebServiceService2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

@Component
@WebService(
        targetNamespace = "http://com.run.ws2",
        endpointInterface = "com.run.webservice.WebServiceService2"
)
@Service
public class WebServiceServiceImp2 implements WebServiceService2 {
    @Override
    public String sayHello2(String name) {
        return "hello " + name;
    }
}
