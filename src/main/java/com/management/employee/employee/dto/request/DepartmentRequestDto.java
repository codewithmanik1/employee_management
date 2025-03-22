package com.management.employee.employee.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDto {
    private String departmentName;
    private Boolean isActive = true;
    private Long createdBy;
    private Long lastModifiedBy;
}
