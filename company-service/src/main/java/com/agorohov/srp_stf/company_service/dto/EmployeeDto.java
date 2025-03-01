package com.agorohov.srp_stf.company_service.dto;

public class EmployeeDto {

    private long userId;
    private long companyId;

    public EmployeeDto() {
    }

    public EmployeeDto(long userId, long companyId) {
        this.userId = userId;
        this.companyId = companyId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
}
