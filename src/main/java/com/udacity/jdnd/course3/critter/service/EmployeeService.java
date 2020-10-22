package com.udacity.jdnd.course3.critter.service;

import com.google.common.collect.Sets;
import com.udacity.jdnd.course3.critter.dto.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.dto.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.user.Employee;
import com.udacity.jdnd.course3.critter.mapper.EmployeeMapper;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.save(EmployeeMapper.DtoToEntity(employeeDTO));
        return EmployeeMapper.EntityToDto(employee);
    }

    public EmployeeDTO getById(Long employeeId) {
        return EmployeeMapper.EntityToDto(employeeRepository.findById(employeeId).orElse(new Employee()));
    }

    public Employee getEntityById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(new Employee());
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(new Employee());
        employee.setDaysAvailable(daysAvailable);
    }

    public List<EmployeeDTO> findEmployeesByServiceAndTime(EmployeeRequestDTO employeeDTO) {
        Set<DayOfWeek> days = Sets.newHashSet(employeeDTO.getDate().getDayOfWeek());
        Set<Employee> employees = employeeRepository.findBySkillsInAndDaysAvailableIn(employeeDTO.getSkills(), days);
        List<EmployeeDTO> employeesByService = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getSkills().containsAll(employeeDTO.getSkills()) && employee.getDaysAvailable().containsAll(days)) {
                employeesByService.add(EmployeeMapper.EntityToDto(employee));
            }
        }
        return employeesByService;
    }
}
