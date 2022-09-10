package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.customer.Customer;
import com.udacity.jdnd.course3.critter.user.customer.CustomerService;
import com.udacity.jdnd.course3.critter.user.employee.Employee;
import com.udacity.jdnd.course3.critter.user.employee.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.employee.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PetService petService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleToScheduleDTO(scheduleService.saveSchedule(convertScheduleDTOToSchedule(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return convertScheduleListToScheduleDTOList(scheduleService.getAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return convertScheduleListToScheduleDTOList(scheduleService.findSchedulesByPetId(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return convertScheduleListToScheduleDTOList(scheduleService.findSchedulesByEmployeeId(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        List<Pet> pets = customer.getPets();
        if (pets == null) return new ArrayList<>();
        return convertScheduleListToScheduleDTOList(scheduleService.getSchedulesForPets(pets));
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        // set same name properties
        BeanUtils.copyProperties(schedule, scheduleDTO);
        // set petIds
        List<Long> petIds = new ArrayList<>();
        List<Pet> petsInSchedule = schedule.getPets();
        if (petsInSchedule != null) {
            for (Pet pet : petsInSchedule) {
                petIds.add(pet.getId());
            }
        }
        scheduleDTO.setPetIds(petIds);
        // set employeeIds
        List<Long> employeeIds = new ArrayList<>();
        List<Employee> employeesInSchedule = schedule.getEmployees();
        if (employeesInSchedule != null) {
            for (Employee employee : employeesInSchedule) {
                employeeIds.add(employee.getId());
            }
        }
        scheduleDTO.setEmployeeIds(employeeIds);
        return scheduleDTO;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        // set Pets
        List<Pet> pets = new ArrayList<>();
        List<Long> petIdsInSchedule = scheduleDTO.getPetIds();
        if (petIdsInSchedule != null) {
            for (Long petId : petIdsInSchedule) {
                pets.add(petService.getPetById(petId));
            }
        }
        schedule.setPets(pets);
        // set Employees
        List<Employee> employees = new ArrayList<>();
        List<Long> employeeIdsInSchedule = scheduleDTO.getEmployeeIds();
        if (employeeIdsInSchedule != null) {
            for (Long employeeId : employeeIdsInSchedule) {
                employees.add(employeeService.getEmployeeById(employeeId));
            }
        }
        schedule.setEmployees(employees);
        return schedule;
    }

    private List<ScheduleDTO> convertScheduleListToScheduleDTOList(List<Schedule> schedules) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOList;
    }
}
