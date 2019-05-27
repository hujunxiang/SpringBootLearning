package com.run.webservice.impl;

import com.run.webservice.WebServiceService3;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

@Component
@WebService(
        targetNamespace = "http://com.run.ws3",
        endpointInterface = "com.run.webservice.WebServiceService3"
)
@Service
public class WebServiceServiceImp3 implements WebServiceService3 {
    @Override
    public String sayHello3(String name) {
        return "hello " + name;
    }
}
