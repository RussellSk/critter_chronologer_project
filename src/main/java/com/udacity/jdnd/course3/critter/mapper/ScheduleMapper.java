package com.udacity.jdnd.course3.critter.mapper;

import com.udacity.jdnd.course3.critter.dto.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.pet.Pet;
import com.udacity.jdnd.course3.critter.entity.schedule.Schedule;
import com.udacity.jdnd.course3.critter.entity.user.Employee;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;


public class ScheduleMapper {
    public static ScheduleDTO EntityToDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        scheduleDTO.setEmployeeIds(schedule
                .getEmployees().stream()
                .map(Employee::getId)
                .collect(Collectors.toList())
        );

        scheduleDTO.setPetIds(schedule
                .getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList())
        );

        return scheduleDTO;
    }

    public static Schedule DtoToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        return schedule;
    }
}
