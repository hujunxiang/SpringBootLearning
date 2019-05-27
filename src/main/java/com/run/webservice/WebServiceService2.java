package com.run.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://com.run.ws2")
public interface WebServiceService2 {
    @WebMethod
    public String sayHello2(String name);
}
