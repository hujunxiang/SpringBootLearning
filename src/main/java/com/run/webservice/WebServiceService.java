package com.run.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://com.run.ws")
public interface WebServiceService {
    @WebMethod
    public String sayHello(String name);
}
