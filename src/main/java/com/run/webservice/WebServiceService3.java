package com.run.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://com.run.ws3")
public interface WebServiceService3 {
    @WebMethod
    public String sayHello3(String name);
}
