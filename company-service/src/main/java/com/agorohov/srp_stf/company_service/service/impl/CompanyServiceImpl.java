package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.*;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import com.agorohov.srp_stf.company_service.exception.CompanyAlreadyExistsException;
import com.agorohov.srp_stf.company_service.exception.CompanyNotFoundException;
import com.agorohov.srp_stf.company_service.exception.UserNotFoundException;
import com.agorohov.srp_stf.company_service.feign.UserServiceFeignClient;
import com.agorohov.srp_stf.company_service.mapper.CompanyMapper;
import com.agorohov.srp_stf.company_service.repository.CompanyRepository;
import com.agorohov.srp_stf.company_service.service.CompanyService;
import com.agorohov.srp_stf.company_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CompanyRepository companyRepository;
    private final EmployeeService employeeService;
    private final UserServiceFeignClient userServiceFeignClient;
    private final CompanyMapper mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, EmployeeService employeeService, UserServiceFeignClient userServiceFeignClient, CompanyMapper mapper) {
        this.companyRepository = companyRepository;
        this.employeeService = employeeService;
        this.userServiceFeignClient = userServiceFeignClient;
        this.mapper = mapper;
    }

    /**
     * Takes company ID and returns CompanyInfo object with extra field - number of employees of this company.
     * If there is no company with this ID, throws CompanyNotFoundException.
     *
     * @param id company ID
     * @return CompanyInfo object
     */
    @Override
    @Transactional(readOnly = true)
    public CompanyInfo getById(long id) {
        // получаем компанию по id или выбрасываем исключение
        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = String.format("Company with id %d not found", id);
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });

        // получаем количество сотрудников компании отдельным запросом через EmployeeRepository,
        // чтобы не выгружать весь список сотрудников ради из количества
        int numberOfEmployees = employeeService.getNumberOfEmployeesByCompanyId(id);

        // Преобразуем сущность компании в ДТО
        CompanyInfo result = mapper.mapCompanyEntityToCompanyInfo(companyEntity, numberOfEmployees);

        log.info("Company found by id: {}", result);
        return result;
    }

    /**
     * Takes company name and returns CompanyInfo object with extra field - number of employees of this company.
     * If there is no company with this name, throws CompanyNotFoundException.
     *
     * @param name name of company
     * @return CompanyInfo
     */
    @Override
    @Transactional(readOnly = true)
    public CompanyInfo getByName(String name) {
        // получаем компанию по имени или выбрасываем исключение
        CompanyEntity companyEntity = companyRepository.findByNameIgnoreCase(name.trim())
                .orElseThrow(() -> {
                    String msg = String.format("Company with name %s not found", name);
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });

        // получаем количество сотрудников компании отдельным запросом через EmployeeRepository,
        // чтобы не выгружать весь список сотрудников ради из количества
        int numberOfEmployees = employeeService.getNumberOfEmployeesByCompanyId(companyEntity.getId());

        // Преобразуем сущность компании в ДТО и возвращаем результат
        CompanyInfo result = mapper.mapCompanyEntityToCompanyInfo(companyEntity, numberOfEmployees);
        log.info("Company found by name: {}", result);
        return result;
    }

    /**
     * Takes a company id and a Pageable object that contains the size and page parameters,
     * returns a list of employees of this company as a Page object.
     * If there is no company with this ID, throws CompanyNotFoundException.
     *
     * @param companyId company id
     * @param pageable  a Pageable object that contains the size and page parameters
     * @return Page object contains UserDto objects
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByCompanyId(long companyId, Pageable pageable) {
        // Проверяем существование компании
        if (!companyRepository.existsById(companyId)) {
            String msg = String.format("No employees, because company with id %d not found", companyId);
            log.error(msg);
            throw new CompanyNotFoundException(msg);
        }

        List<Long> userIds = employeeService.findEmployeeIdsByCompanyId(companyId);
        List<UserDto> userDtos = userServiceFeignClient.getUsersByIds(userIds);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), userDtos.size());

        // Проверяем, что запрашиваемая страница не выходит за границы
        List<UserDto> pagedList = start < userDtos.size() ? userDtos.subList(start, end) : Collections.emptyList();

        Page<UserDto> result = new PageImpl<>(pagedList, pageable, userDtos.size());

        log.info("Returned page of employees by company id = {}, number of employees: {}",
                companyId, userDtos.size());
        return result;
    }

    /**
     * Takes a company name and a Pageable object that contains the size and page parameters,
     * returns a list of employees of this company as a Page object.
     * If there is no company with this name, throws CompanyNotFoundException.
     *
     * @param companyName company name
     * @param pageable    a Pageable object that contains the size and page parameters
     * @return Page object contains UserDto objects
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByCompanyName(String companyName, Pageable pageable) {
        // Проверяем существование компании
        long companyId = companyRepository.findByNameIgnoreCase(companyName.trim())
                .orElseThrow(() -> {
                    String msg = String.format("No employees, because company with name %s not found", companyName);
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                })
                .getId();

        List<Long> userIds = employeeService.findEmployeeIdsByCompanyId(companyId);
        List<UserDto> userDtos = userServiceFeignClient.getUsersByIds(userIds);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), userDtos.size());

        // Проверяем, что запрашиваемая страница не выходит за границы
        List<UserDto> pagedList = start < userDtos.size() ? userDtos.subList(start, end) : Collections.emptyList();

        Page<UserDto> result = new PageImpl<>(pagedList, pageable, userDtos.size());

        log.info("Returned page of employees by company name = {}, number of employees: {}",
                companyName, userDtos.size());
        return result;
    }

    /**
     * Takes CreateCompany object, saves company id DB and returns CompanyDto object with new user ID.
     * If there is already a company with the same name, throws CompanyAlreadyExistsException.
     *
     * @param company CreateCompany object
     * @return CompanyDto object
     */
    @Override
    @Transactional
    public CompanyDto create(CreateCompany company) {
        // Проверяем, нет ли уже компании с таким именем
        if (companyRepository.existsByNameIgnoreCase(company.getName().trim())) {
            throw new CompanyAlreadyExistsException("There is already a company with the name " + company.getName());
        }
        CompanyEntity entity = mapper.mapCreateCompanyToCompanyEntity(company);
        // Сохраняем компанию в БД и маппим в CompanyDto, чтобы вернуть созданную компанию уже с ID
        // (можно обойтись без этого и просто возвращать статус 201 Created вместо CompanyDto)
        CompanyDto result = mapper.mapCompanyEntityToCompanyDto(companyRepository.save(entity));
        log.info("Created company: {}", result);
        return result;
    }

    /**
     * Takes company ID and return found company in CompanyDto object.
     * If there is no company with the requested ID, throws CompanyNotFoundException.
     *
     * @param id company ID
     * @return CompanyDto object
     */
    @Override
    public CompanyDto get(long id) {
        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = String.format("No company with id %d", id);
                    log.error("Fail get company: {}", msg);
                    return new CompanyNotFoundException(msg);
                });
        CompanyDto result = mapper.mapCompanyEntityToCompanyDto(companyEntity);
        log.info("Got company: {}", result);
        return result;
    }

    /**
     * Takes UpdateCompany to update the company.
     * If there is no company with the same ID, throws CompanyNotFoundException.
     * If there is a company with the same ID as in UpdateCompany, and there are no other companies with the same name,
     * updates the company and returns CompanyDto with the updated data.
     *
     * @param company UpdateCompany object
     * @return CompanyDto object
     */
    @Override
    @Transactional
    public CompanyDto update(UpdateCompany company) {
        // Проверяем есть ли компания с таким ID, чтобы не создавать новую при попытке отредактировать несуществующую
        CompanyEntity existingCompany = companyRepository.findById(company.getId())
                .orElseThrow(() -> {
                    String msg = String.format("No company with id %d", company.getId());
                    log.error("Fail update company: {}", msg);
                    return new CompanyNotFoundException(msg);
                });
        // Проверяем, изменилось ли имя компании
        if (!existingCompany.getName().equalsIgnoreCase(company.getName().trim())) {
            // Если имя изменилось, проверяем нет ли уже компании с таким именем
            if (companyRepository.existsByNameIgnoreCase(company.getName().trim())) {
                throw new CompanyAlreadyExistsException("There is already a company with the name" + company.getName());
            }
        }
        CompanyEntity companyEntity = mapper.mapUpdateCompanyToCompanyEntity(company);
        companyRepository.save(companyEntity);
        // Маппим обновленного юзера в CompanyDto и возвращаем результат
        // (можно было вернуть 200 OK или тот же UpdateCompany, но хочется побыть дотошным)
        CompanyDto result = mapper.mapCompanyEntityToCompanyDto(companyEntity);
        log.info("Updated company: {}", result);
        return result;
    }

    /**
     * Deletes company with taking ID, returns no data.
     * @param id company ID
     */
    @Override
    public void delete(long id) {
        companyRepository.deleteById(id);
        log.info("Company with id {} deleted", id);
    }

    /**
     * Returns all existing companies is Pages with CompanyDto objects.
     *
     * @param pageable Pageable object with default size and page
     * @return Page object with all existing CompanyDto objects
     */
    @Override
    public Page<CompanyDto> getAll(Pageable pageable) {
        Page<CompanyEntity> companyPage = companyRepository.findAll(pageable);

        List<CompanyDto> companyDtos = companyPage.getContent().stream()
                .map(mapper::mapCompanyEntityToCompanyDto)
                .toList();

        PageImpl<CompanyDto> result = new PageImpl<>(
                companyDtos, pageable, companyPage.getTotalElements());
        log.info("Page with companies returned: {}", result);
        return result;
    }

    /**
     * Takes the company ID and the employee ID. Check if there is a company with such an ID,
     * if not, throws CompanyNotFoundException.
     * Checks if there is a user with such an ID in the user-service microservice,
     * if not, throws UserNotFoundException. Add the employee to the DB and return the EmployeeDto object.
     *
     * @param companyId  company ID where is adding employee
     * @param employeeId employee ID corresponding user ID from user-service microservice
     * @return EmployeeDto object
     */
    @Override
    @Transactional
    public EmployeeDto addEmployee(long companyId, long employeeId) {
        // Если компании с companyId нет, возвращаем сообщение об ошибке
        CompanyEntity companyEntity = companyRepository.findById(companyId)
                .orElseThrow(() -> {
                    String msg = String.format("No company with id %d", companyId);
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });

        // Идём в микросервис user-service и проверяем, есть ли пользователь с таким ID
        if (!userServiceFeignClient.existsById(employeeId)) {
            String msg = String.format("User with id %d not found", employeeId);
            log.error(msg);
            throw new UserNotFoundException(msg);
        }

        return employeeService.addEmployee(mapper.mapCompanyEntityToCompanyDto(companyEntity), employeeId);
    }

    /**
     * ID of the company from which the employee needs to be removed.
     * If there is no company with such an ID, it is throws CompanyNotFoundException.
     *
     * @param companyId  company ID.
     * @param employeeId employee ID.
     */
    @Override
    @Transactional
    public void deleteEmployee(long companyId, long employeeId) {
        // Если компании с companyId нет, возвращаем сообщение об ошибке
        CompanyEntity companyEntity = companyRepository.findById(companyId)
                .orElseThrow(() -> {
                    String msg = String.format("No company with id %d", companyId);
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });
        employeeService.deleteEmployee(employeeId);
    }
}
