package com.dla.org.client;

import com.dla.org.model.Department;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component("departmentClient")
public class DepartmentClientImpl implements DepartmentClient{
     private static final String SERVICE="department-service";
    @Autowired
    EurekaClient eurekaClient;
    @Override
    @HystrixCommand(fallbackMethod = "callDepartmentServiceAndGetData_Fallback")
    public List<Department> findByOrganization(Long organizationId) {
        List<Department> departments=null;
        String url=this.getUrl()+"/organization/"+organizationId;
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<Department[]>response=restTemplate.getForEntity(url,Department[].class);
        return Arrays.asList(response.getBody());
    }

    @Override
    public List<Department> findByOrganizationWithEmployees(Long organizationId) {
        return null;
    }
    private String getUrl() {
        List<Application> applications = eurekaClient.getApplications().getRegisteredApplications();
        String url1=null;
        Application info=applications.stream().findFirst().get();
        if(info.getName().equalsIgnoreCase(SERVICE)) {
            InstanceInfo instanceInfo = applications.stream().findFirst().get().getInstances().get(0);
            url1 =instanceInfo.getHomePageUrl();
        }
        return url1;
    }
    public List<Department> callDepartmentServiceAndGetData_Fallback(Long organizationId) {
        System.out.println("Student Service is down!!! fallback route enabled...");
        List<Department> list=new ArrayList<>();
        list.add(new Department("CIRCUIT BREAKER ENABLED!!!No Response From Department Service at this moment. Service will be back shortly - " + new Date()));
        return list;
    }
}
