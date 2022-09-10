package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.getOne(id);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findSchedulesByPetId(long petId) {
        return scheduleRepository.findAllByPetsId(petId);
    }

    public List<Schedule> findSchedulesByEmployeeId(long employeeId) {
        return scheduleRepository.findAllByEmployeesId(employeeId);
    }

    public List<Schedule> getSchedulesForPets(List<Pet> pets) {
        List<Schedule> schedules = new ArrayList<>();
        for (Pet pet : pets) {
            schedules.addAll(findSchedulesByPetId(pet.getId()));
        }
        return schedules;
    }
}
