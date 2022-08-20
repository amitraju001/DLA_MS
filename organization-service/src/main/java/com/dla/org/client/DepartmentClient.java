package com.dla.org.client;

import com.dla.org.model.Department;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface DepartmentClient {
    public List<Department> findByOrganization(Long organizationId);

    public List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId);

}
