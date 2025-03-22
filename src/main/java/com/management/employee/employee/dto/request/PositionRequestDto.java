package com.management.employee.employee.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionRequestDto {

    private String position;
    private Boolean isActive = true;
    private Long createdBy;
    private Long lastModifiedBy;
}
