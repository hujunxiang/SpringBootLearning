package com.run.webservice.impl;

import com.run.webservice.WebServiceService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

@Component
@WebService(
        targetNamespace = "http://com.run.ws",
        endpointInterface = "com.run.webservice.WebServiceService"
)
@Service
public class WebServiceServiceImp implements WebServiceService {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
