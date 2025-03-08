package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.*;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import com.agorohov.srp_stf.company_service.exception.CompanyAlreadyExistsException;
import com.agorohov.srp_stf.company_service.exception.CompanyNotFoundException;
import com.agorohov.srp_stf.company_service.exception.UserNotFoundException;
import com.agorohov.srp_stf.company_service.feign.UserServiceFeignClient;
import com.agorohov.srp_stf.company_service.mapper.CompanyMapper;
import com.agorohov.srp_stf.company_service.repository.CompanyRepository;
import com.agorohov.srp_stf.company_service.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private UserServiceFeignClient userServiceFeignClient;

    @Mock
    private CompanyMapper mapper;

    @InjectMocks
    private CompanyServiceImpl out;

    private static final long COMPANY_ID = 1L;
    private static final String COMPANY_NAME = "Quraga Technologies";
    private static final BigDecimal COMPANY_BUDGET = BigDecimal.valueOf(12345.54321);
    private static final long EMPLOYEE_ID = 35L;
    private static final String CREATE_COMPANY_NAME = "Persique Corporation";
    private static final int NUM_OF_EMPLOYEES = 25;

    private CompanyEntity companyEntity;
    private CompanyDto companyDto;
    private CompanyInfo companyInfo;
    private CreateCompany createCompany;
    private UpdateCompany updateCompany;

    @BeforeEach
    void setUp() {
        companyEntity = new CompanyEntity(COMPANY_ID, COMPANY_NAME, COMPANY_BUDGET, null);
        companyDto = new CompanyDto(COMPANY_ID, COMPANY_NAME, COMPANY_BUDGET);
        companyInfo = new CompanyInfo(COMPANY_ID, COMPANY_NAME, COMPANY_BUDGET, NUM_OF_EMPLOYEES);
        createCompany = new CreateCompany(CREATE_COMPANY_NAME, COMPANY_BUDGET);
        updateCompany = new UpdateCompany(COMPANY_ID, COMPANY_NAME, COMPANY_BUDGET);
    }

    @Test
    void getById_test_success() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(companyEntity));
        when(employeeService.getNumberOfEmployeesByCompanyId(COMPANY_ID)).thenReturn(NUM_OF_EMPLOYEES);
        when(mapper.mapCompanyEntityToCompanyInfo(companyEntity, NUM_OF_EMPLOYEES))
                .thenReturn(companyInfo);

        CompanyInfo actual = out.getById(COMPANY_ID);

        assertEquals(companyInfo, actual);
    }

    @Test
    void getById_test_notFound() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> out.getById(COMPANY_ID));
    }

    @Test
    void getByName_test_success() {
        when(companyRepository.findByNameIgnoreCase(COMPANY_NAME)).thenReturn(Optional.of(companyEntity));
        when(employeeService.getNumberOfEmployeesByCompanyId(COMPANY_ID)).thenReturn(NUM_OF_EMPLOYEES);
        when(mapper.mapCompanyEntityToCompanyInfo(companyEntity, NUM_OF_EMPLOYEES)).thenReturn(companyInfo);

        CompanyInfo actual = out.getByName(COMPANY_NAME);

        assertEquals(companyInfo, actual);
    }

    @Test
    void getByName_test_notFound() {
        when(companyRepository.findByNameIgnoreCase(COMPANY_NAME)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> out.getByName(COMPANY_NAME));
    }

    @Test
    void getUsersByCompanyId_test_success() {
        UserDto userDto = new UserDto(EMPLOYEE_ID, "Zahar", "Evgeniev", "+79005333297");
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(true);
        when(employeeService.findEmployeeIdsByCompanyId(COMPANY_ID)).thenReturn(List.of(EMPLOYEE_ID));
        when(userServiceFeignClient.getUsersByIds(List.of(EMPLOYEE_ID))).thenReturn(List.of(userDto));

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserDto> actual = out.getUsersByCompanyId(COMPANY_ID, pageable);

        assertEquals(1, actual.getContent().size());
        assertEquals(userDto.getId(), actual.getContent().get(0).getId());
        assertEquals(userDto.getFirstName(), actual.getContent().get(0).getFirstName());
        assertEquals(userDto.getLastName(), actual.getContent().get(0).getLastName());
        assertEquals(userDto.getPhoneNumber(), actual.getContent().get(0).getPhoneNumber());
    }

    @Test
    void getUsersByCompanyId_test_companyNotFound() {
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(false);

        assertThrows(CompanyNotFoundException.class, () -> out.getUsersByCompanyId(COMPANY_ID, Pageable.unpaged()));
    }

    @Test
    void getUsersByCompanyName_test_success() {
        when(companyRepository.findByNameIgnoreCase(COMPANY_NAME)).thenReturn(Optional.of(companyEntity));
        when(employeeService.findEmployeeIdsByCompanyId(COMPANY_ID)).thenReturn(List.of(EMPLOYEE_ID));
        when(userServiceFeignClient.getUsersByIds(List.of(EMPLOYEE_ID))).thenReturn(List.of(new UserDto()));

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserDto> actual = out.getUsersByCompanyName(COMPANY_NAME, pageable);

        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
    }

    @Test
    void getUsersByCompanyName_test_companyNotFound() {
        when(companyRepository.findByNameIgnoreCase(COMPANY_NAME)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> out.getUsersByCompanyName(COMPANY_NAME, Pageable.unpaged()));
    }

    @Test
    void create_test_success() {
        when(companyRepository.existsByNameIgnoreCase(CREATE_COMPANY_NAME)).thenReturn(false);
        when(mapper.mapCreateCompanyToCompanyEntity(createCompany)).thenReturn(companyEntity);
        when(companyRepository.save(companyEntity)).thenReturn(companyEntity);
        when(mapper.mapCompanyEntityToCompanyDto(companyEntity)).thenReturn(companyDto);

        CompanyDto actual = out.create(createCompany);

        assertEquals(companyDto, actual);
        assertEquals(companyDto.getId(), actual.getId());
        assertEquals(companyDto.getName(), actual.getName());
        assertEquals(companyDto.getBudget(), actual.getBudget());
    }

    @Test
    void create_test_alreadyExists() {
        when(companyRepository.existsByNameIgnoreCase(CREATE_COMPANY_NAME)).thenReturn(true);

        assertThrows(CompanyAlreadyExistsException.class, () -> out.create(createCompany));
    }

    @Test
    void get_test_success() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(companyEntity));
        when(mapper.mapCompanyEntityToCompanyDto(companyEntity)).thenReturn(companyDto);

        CompanyDto actual = out.get(COMPANY_ID);

        assertEquals(companyDto, actual);
    }

    @Test
    void get_test_notFound() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> out.get(COMPANY_ID));
    }

    @Test
    void update_test_success() {
        CompanyEntity existingCompany = new CompanyEntity(COMPANY_ID, "Old Name", COMPANY_BUDGET, null);
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.existsByNameIgnoreCase(COMPANY_NAME)).thenReturn(false);
        when(mapper.mapUpdateCompanyToCompanyEntity(updateCompany)).thenReturn(companyEntity);
        when(companyRepository.save(companyEntity)).thenReturn(companyEntity);
        when(mapper.mapCompanyEntityToCompanyDto(companyEntity)).thenReturn(companyDto);

        CompanyDto actual = out.update(updateCompany);

        assertEquals(companyDto, actual);
        assertEquals(companyDto.getId(), actual.getId());
        assertEquals(companyDto.getName(), actual.getName());
        assertEquals(companyDto.getBudget(), actual.getBudget());
    }

    @Test
    void update_test_notFound() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> out.update(updateCompany));
    }

    @Test
    void update_test_alreadyExists() {
        CompanyEntity existingCompany = new CompanyEntity(COMPANY_ID, "Old Name", COMPANY_BUDGET, null);
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.existsByNameIgnoreCase(COMPANY_NAME)).thenReturn(true);

        assertThrows(CompanyAlreadyExistsException.class, () -> out.update(updateCompany));
    }

    @Test
    void delete_test_success() {
        out.delete(COMPANY_ID);
        verify(companyRepository, times(1)).deleteById(COMPANY_ID);
    }

    @Test
    void getAll_test_success() {
        Page<CompanyEntity> companyPage = new PageImpl<>(List.of(companyEntity), Pageable.unpaged(), 1);
        when(companyRepository.findAll(any(Pageable.class))).thenReturn(companyPage);
        when(mapper.mapCompanyEntityToCompanyDto(companyEntity)).thenReturn(companyDto);

        Page<CompanyDto> actual = out.getAll(Pageable.unpaged());

        assertEquals(1, actual.getContent().size());
        assertEquals(companyDto, actual.getContent().get(0));
    }

    @Test
    void addEmployee_test_success() {
        EmployeeDto newEmployeeDto = new EmployeeDto(COMPANY_ID, EMPLOYEE_ID);
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(companyEntity));
        when(userServiceFeignClient.existsById(EMPLOYEE_ID)).thenReturn(true);
        when(mapper.mapCompanyEntityToCompanyDto(companyEntity)).thenReturn(companyDto);
        when(employeeService.addEmployee(companyDto, EMPLOYEE_ID)).thenReturn(newEmployeeDto);

        EmployeeDto actual = out.addEmployee(COMPANY_ID, EMPLOYEE_ID);

        verify(employeeService, times(1)).addEmployee(companyDto, EMPLOYEE_ID);
        assertEquals(newEmployeeDto, actual);
        assertEquals(newEmployeeDto.getUserId(), actual.getUserId());
        assertEquals(newEmployeeDto.getCompanyId(), actual.getCompanyId());
    }

    @Test
    void addEmployee_test_companyNotFound() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> out.addEmployee(COMPANY_ID, EMPLOYEE_ID));
    }

    @Test
    void addEmployee_test_userNotFound() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(companyEntity));
        when(userServiceFeignClient.existsById(EMPLOYEE_ID)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> out.addEmployee(COMPANY_ID, EMPLOYEE_ID));
    }

    @Test
    void deleteEmployee_test_success() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(companyEntity));

        out.deleteEmployee(COMPANY_ID, EMPLOYEE_ID);

        verify(employeeService).deleteEmployee(EMPLOYEE_ID);
    }

    @Test
    void deleteEmployee_test_companyNotFound() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> out.deleteEmployee(COMPANY_ID, EMPLOYEE_ID));
    }
}