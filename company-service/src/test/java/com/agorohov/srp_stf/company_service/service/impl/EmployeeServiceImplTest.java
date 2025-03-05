package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.dto.EmployeeDto;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import com.agorohov.srp_stf.company_service.entity.EmployeeEntity;
import com.agorohov.srp_stf.company_service.exception.EmployeeAlreadyExistsException;
import com.agorohov.srp_stf.company_service.mapper.CompanyMapper;
import com.agorohov.srp_stf.company_service.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyMapper mapper;

    @InjectMocks
    private EmployeeServiceImpl out;

    private static final long EMPLOYEE_ID = 1L;
    private static final long COMPANY_ID = 37L;
    private static final String COMPANY_NAME = "Dessert Forests Inc.";
    private static final BigDecimal COMPANY_BUDGET = BigDecimal.valueOf(1230005.09);

    private CompanyEntity companyEntity;
    private CompanyEntity companyEntityWithoutEmployeesList;
    private CompanyDto companyDto;
    private EmployeeEntity employeeEntity;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setup() {
        employeeEntity = new EmployeeEntity(EMPLOYEE_ID, companyEntity);
        employeeDto = new EmployeeDto(EMPLOYEE_ID, COMPANY_ID);
        companyEntity = new CompanyEntity(COMPANY_ID, COMPANY_NAME, COMPANY_BUDGET, null);
        List<EmployeeEntity> employees = List.of(employeeEntity);
        companyEntity.setEmployees(employees);
        companyEntityWithoutEmployeesList = new CompanyEntity(COMPANY_ID, COMPANY_NAME, COMPANY_BUDGET, null);
        companyDto = new CompanyDto(COMPANY_ID, COMPANY_NAME, COMPANY_BUDGET);
    }

    @Test
    void getNumberOfEmployeesByCompanyId_test() {
        when(employeeRepository.getNumberOfEmployeesByCompanyId(COMPANY_ID))
                .thenReturn(companyEntity.getEmployees().size());

        int actual = out.getNumberOfEmployeesByCompanyId(COMPANY_ID);

        assertEquals(1, actual);
    }

    @Test
    void findEmployeeIdsByCompanyId_test() {
        when(employeeRepository.findEmployeeIdsByCompanyId(COMPANY_ID)).thenReturn(List.of(EMPLOYEE_ID));

        List<Long> actual = out.findEmployeeIdsByCompanyId(COMPANY_ID);

        assertEquals(EMPLOYEE_ID, actual.get(0));
        assertEquals(1, actual.size());
    }

    @Test
    void existsById_test_true() {
        when(employeeRepository.existsById(EMPLOYEE_ID)).thenReturn(Boolean.TRUE);

        boolean actual = out.existsById(EMPLOYEE_ID);

        assertTrue(actual);
    }

    @Test
    void existsById_test_false() {
        when(employeeRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);

        boolean actual = out.existsById(EMPLOYEE_ID);

        assertFalse(actual);
    }

    @Test
    void addEmployee_test_success() {
        when(employeeRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
        when(mapper.mapCompanyDtoToCompanyEntityWithoutEmployees(any(CompanyDto.class)))
                .thenReturn(companyEntityWithoutEmployeesList);
        when(mapper.mapEmployeeEntityToEmployeeDto(any(EmployeeEntity.class))).thenReturn(employeeDto);

        EmployeeDto actual = out.addEmployee(companyDto, EMPLOYEE_ID);

        assertEquals(employeeDto, actual);
    }

    @Test
    void addEmployee_test_fail() {
        when(employeeRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);

        assertThrows(EmployeeAlreadyExistsException.class, () -> out.addEmployee(companyDto, EMPLOYEE_ID));
    }

    @Test
    void deleteEmployee_test() {
        out.deleteEmployee(EMPLOYEE_ID);

        verify(employeeRepository).deleteById(EMPLOYEE_ID);
    }
}