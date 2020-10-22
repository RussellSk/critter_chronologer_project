package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.pet.Pet;
import com.udacity.jdnd.course3.critter.entity.schedule.Schedule;
import com.udacity.jdnd.course3.critter.entity.user.Customer;
import com.udacity.jdnd.course3.critter.entity.user.Employee;
import com.udacity.jdnd.course3.critter.mapper.ScheduleMapper;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeService employeeService;
    private final PetService petService;
    private final CustomerService customerService;

    public ScheduleService(ScheduleRepository scheduleRepository, EmployeeService employeeService, PetService petService, CustomerService customerService) {
        this.scheduleRepository = scheduleRepository;
        this.employeeService = employeeService;
        this.petService = petService;
        this.customerService = customerService;
    }

    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        Schedule schedule = ScheduleMapper.DtoToEntity(scheduleDTO);
        schedule.setEmployees(scheduleDTO
                .getEmployeeIds().stream()
                .map(employeeService::getEntityById)
                .collect(Collectors.toList())
        );
        schedule.setPets(scheduleDTO
                .getPetIds().stream()
                .map(petService::getEntityById)
                .collect(Collectors.toList())
        );

        return ScheduleMapper.EntityToDto(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOS.add(ScheduleMapper.EntityToDto(schedule));
        }
        return scheduleDTOS;
    }

    public List<ScheduleDTO> getSchedulesForEmployee(long employeeId) {
        Employee employee = employeeService.getEntityById(employeeId);
        List<Schedule> schedules = new ArrayList<>(scheduleRepository.findByEmployeesIn(Collections.singletonList(employee)));
        return schedules.stream().map(ScheduleMapper::EntityToDto).collect(Collectors.toList());
    }

    public List<ScheduleDTO> getSchedulesForCustomer(long customerId) {
        Customer customer = customerService.findById(customerId);
        List<Schedule> schedules = new ArrayList<>(scheduleRepository.findByPetsIn(customer.getPets()));
        return schedules.stream().map(ScheduleMapper::EntityToDto).collect(Collectors.toList());
    }

    public List<ScheduleDTO> getSchedulesForPet(long petId) {
        Pet pet = petService.getEntityById(petId);
        List<Schedule> schedules = new ArrayList<>(scheduleRepository.findByPetsIn(Collections.singletonList(pet)));
        return schedules.stream().map(ScheduleMapper::EntityToDto).collect(Collectors.toList());
    }
}
