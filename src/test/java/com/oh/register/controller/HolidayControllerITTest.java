package com.oh.register.controller;

import com.oh.register.exception.RegisterException;
import com.oh.register.model.dto.*;
import com.oh.register.model.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class HolidayControllerITTest {

    @LocalServerPort
    private Integer port;

    private String BASE_URL;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private HolidayController holidayController;

    private List<ChildrenDTO> childrenDTOS = new ArrayList<>();

    private List<HolidayDTO> holidayDTOS = new ArrayList<>();

    private List<HolidayDayDTO> holidayDayDTOS = new ArrayList<>();

    private EmployeeDTO employeeDTO;

    @BeforeEach
    public void init() {
        BASE_URL = "http://localhost:" + port;

        HolidayDayDTO holidayDayDTO1 = new HolidayDayDTO();
        holidayDayDTO1.setId(1L);
        holidayDayDTO1.setYear("2021");
        holidayDayDTO1.setLocalDate(Arrays.asList(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 3, 15), LocalDate.of(2021, 4, 2)));

        HolidayDayDTO holidayDayDTO2 = new HolidayDayDTO();
        holidayDayDTO2.setId(2L);
        holidayDayDTO2.setYear("2022");
        holidayDayDTO2.setLocalDate(Arrays.asList(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 3, 15), LocalDate.of(2022, 4, 2)));

        holidayDayDTOS.clear();
        holidayDayDTOS.addAll(Arrays.asList(holidayDayDTO1, holidayDayDTO2));

        for (HolidayDayDTO holidayDayDTO : holidayDayDTOS) {
            testRestTemplate.postForEntity(BASE_URL + "/holidayday/add", new HttpEntity<>(holidayDayDTO), HolidayDayDTO.class);
        }


        employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1995, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO.setFirstName("Kovács");
        employeeDTO.setLastName("Péter");
        employeeDTO.setIdentityNumber("111111111");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");
        employeeDTO.setId(1L);

        testRestTemplate.postForObject(BASE_URL + "/employee/add", new HttpEntity<>(employeeDTO), Employee.class);

        ChildrenDTO children1 = new ChildrenDTO();
        children1.setId(1L);
        children1.setFirstName("Kovács");
        children1.setLastName("Péter");
        children1.setBirthDay(LocalDate.of(2020, 1, 1));

        ChildrenDTO children2 = new ChildrenDTO();
        children2.setId(2L);
        children2.setFirstName("Kovács");
        children2.setLastName("Ilona");
        children2.setBirthDay(LocalDate.of(2018, 1, 1));

        childrenDTOS.clear();
        childrenDTOS.addAll(Arrays.asList(children1, children2));
        long id = 1;
        for (ChildrenDTO children : childrenDTOS) {
            testRestTemplate.postForObject(BASE_URL + "/children/employee/{id}/add", new HttpEntity<>(children), ChildrenDTO.class, id);
        }

        HolidayDTO holidayDTO1 = new HolidayDTO();
        holidayDTO1.setId(1L);
        holidayDTO1.setEmployeeId(1L);
        holidayDTO1.setStartDate(LocalDate.of(2021, 2, 5));
        holidayDTO1.setFinishDate(LocalDate.of(2021, 2, 15));

        HolidayDTO holidayDTO2 = new HolidayDTO();
        holidayDTO2.setId(2L);
        holidayDTO2.setEmployeeId(1L);
        holidayDTO2.setStartDate(LocalDate.of(2021, 2, 25));
        holidayDTO2.setFinishDate(LocalDate.of(2021, 3, 5));

        HolidayDTO holidayDTO3 = new HolidayDTO();
        holidayDTO3.setId(3L);
        holidayDTO3.setEmployeeId(1L);
        holidayDTO3.setStartDate(LocalDate.of(2021, 12, 25));
        holidayDTO3.setFinishDate(LocalDate.of(2022, 1, 5));

        HolidayDTO holidayDTO4 = new HolidayDTO();
        holidayDTO4.setId(4L);
        holidayDTO4.setEmployeeId(1L);
        holidayDTO4.setStartDate(LocalDate.of(2021, 4, 30));
        holidayDTO4.setFinishDate(LocalDate.of(2021, 5, 5));

        holidayDTOS.clear();
        holidayDTOS.addAll(Arrays.asList(holidayDTO1, holidayDTO2, holidayDTO3, holidayDTO4));
        for (HolidayDTO holidayDTO : holidayDTOS) {
            testRestTemplate.postForEntity(BASE_URL + "/holiday/employee/{id}/add", new HttpEntity<>(holidayDTO), HolidayDTO.class, id);
        }
    }

    @Test
    void save_inputHolidayDTO_shouldReturnRightHolidayDTO() {
        long id = 1;
        HolidayDTO expected = new HolidayDTO();
        expected.setId(5L);
        expected.setStartDate(LocalDate.of(2021, 6, 25));
        expected.setFinishDate(LocalDate.of(2021, 6, 28));

        ResponseEntity<HolidayDTO> response = testRestTemplate.postForEntity(BASE_URL + "/holiday/employee/{id}/add", new HttpEntity<>(expected), HolidayDTO.class, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        HolidayDTO actual = response.getBody();
        assertEquals(expected, actual);
    }

    @Test
    void save_inputHolidayDTOWithBiggerHolidayInterval_shouldReturnBadRequest() {
        long id = 1;
        HolidayDTO expected = new HolidayDTO();
        expected.setId(5L);
        expected.setStartDate(LocalDate.of(2021, 6, 25));
        expected.setFinishDate(LocalDate.of(2021, 7, 28));

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/holiday/employee/{id}/add", new HttpEntity<>(expected), ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ResponseEntity<Employee> responseEmployee = testRestTemplate.getForEntity(BASE_URL + "/employee/{id}/get", Employee.class, id);
        String actual = response.getBody().getError();
        String expectedResponse = "The number of holidays available is less than the requested leave! You have only " + (Objects.requireNonNull(responseEmployee.getBody()).getBasicLeave() + responseEmployee.getBody().getExtraLeave() - responseEmployee.getBody().getSumHoliday()) + " days!";
        assertEquals(expectedResponse, actual);
    }

    @Test
    void save_inputHolidayDTOWithNextYearDateInterval_shouldReturnBadRequest() {
        long id = 1;
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(5L);
        holidayDTO.setStartDate(LocalDate.of(2022, 1, 25));
        holidayDTO.setFinishDate(LocalDate.of(2022, 1, 28));

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/holiday/employee/{id}/add", new HttpEntity<>(holidayDTO), ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String actual = response.getBody().getError();
        String expected = "Invalid date interval! Next year's leave is not allowed";
        assertEquals(expected, actual);
    }

    @Test
    void save_inputHolidayDTOWithOverLapDateInterval_shouldReturnBadRequest() {
        long id = 1;
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(5L);
        holidayDTO.setStartDate(LocalDate.of(2021, 12, 30));
        holidayDTO.setFinishDate(LocalDate.of(2022, 1, 12));

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/holiday/employee/{id}/add", new HttpEntity<>(holidayDTO), ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String actual = response.getBody().getError();
        String expected = "The interval shouldn't be overlap with an existing interval!";
        assertEquals(expected, actual);
    }

    @Test
    void save_inputHolidayDTOWithDuplicatedDateInterval_shouldReturnBadRequest() {
        long id = 1;
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(5L);
        holidayDTO.setStartDate(LocalDate.of(2021, 2, 5));
        holidayDTO.setFinishDate(LocalDate.of(2021, 2, 15));

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/holiday/employee/{id}/add", new HttpEntity<>(holidayDTO), ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String actual = response.getBody().getError();
        String expected = String.format("This date interval from %s to %s already exists for the worker!", holidayDTO.getStartDate(), holidayDTO.getFinishDate());
        assertEquals(expected, actual);
    }

    @Test
    void save_inputHolidayDTOWithOverlapDateInterval_shouldReturnBadRequest() {
        long id = 1;
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(5L);
        holidayDTO.setStartDate(LocalDate.of(2021, 2, 15));
        holidayDTO.setFinishDate(LocalDate.of(2021, 2, 18));

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/holiday/employee/{id}/add", new HttpEntity<>(holidayDTO), ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String actual = response.getBody().getError();
        String expected = "The start date or the finish date already exists for the worker!";
        assertEquals(expected, actual);
    }

    @Test
    void save_inputHolidayDTOWithBiggerHolidayIntervalNextYear_shouldReturnBadRequest() {
        long id = 1;
        HolidayDTO expected = new HolidayDTO();
        expected.setId(5L);
        expected.setStartDate(LocalDate.of(2021, 12, 25));
        expected.setFinishDate(LocalDate.of(2022, 2, 28));
        holidayController.delete(holidayDTOS.get(2), 1L);
        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/holiday/employee/{id}/add", new HttpEntity<>(expected), ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ResponseEntity<Employee> responseEmployee = testRestTemplate.getForEntity(BASE_URL + "/employee/{id}/get", Employee.class, id);
        String actual = response.getBody().getError();
        String expectedResponse = "The number of holidays available is less than the requested leave! You have only " + (Objects.requireNonNull(responseEmployee.getBody()).getNextYearLeave() - responseEmployee.getBody().getSumHolidayNextYear()) + " days in 2022!";
        assertEquals(expectedResponse, actual);
    }

    @Test
    void delete_inputHolidayDTOWithValidDateInterval_shouldReturnRightMessageAndLeaveRestored() {
        long id = 1;
        ResponseEntity<Employee> response;
        response = testRestTemplate.getForEntity(BASE_URL + "/employee/{id}/get", Employee.class, id);
        long sumHoliday = Objects.requireNonNull(response.getBody()).getSumHoliday();
        long expectedHoliday = 23;
        assertEquals(expectedHoliday, sumHoliday);
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(2L);
        holidayDTO.setEmployeeId(1L);
        holidayDTO.setStartDate(LocalDate.of(2021, 2, 25));
        holidayDTO.setFinishDate(LocalDate.of(2021, 3, 5));
        ResponseEntity<MessageDTO> responseAfterDelete = holidayController.delete(holidayDTO, 1L);

        String expected = "The holiday was deleted from " + holidayDTO.getStartDate().toString() + " to " + holidayDTO.getFinishDate().toString() + "!";
        String actual = responseAfterDelete.getBody().getMessage();
        assertEquals(HttpStatus.OK, responseAfterDelete.getStatusCode());
        assertEquals(expected, actual);

        long expectedHolidayAfterDelete = 16;
        response = testRestTemplate.getForEntity(BASE_URL + "/employee/{id}/get", Employee.class, id);
        assertEquals(expectedHolidayAfterDelete, Objects.requireNonNull(response.getBody()).getSumHoliday());
    }

    @Test
    void delete_inputHolidayDTOWithBadDateInterval_shouldReturnRegisterException() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(2L);
        holidayDTO.setEmployeeId(1L);
        holidayDTO.setStartDate(LocalDate.of(2021, 2, 25));
        holidayDTO.setFinishDate(LocalDate.of(2021, 3, 3));
        String actual = "";
        try {
            holidayController.delete(holidayDTO, 1L);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        String expected = "The specified date interval doesn't exist for the employee with id:1!";
        assertEquals(expected, actual);
    }


    @Test
    void delete_inputHolidayDTOWithFinishDateIsEarlierThanStartDate_shouldReturnRegisterException() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(2L);
        holidayDTO.setEmployeeId(1L);
        holidayDTO.setStartDate(LocalDate.of(2021, 3, 6));
        holidayDTO.setFinishDate(LocalDate.of(2021, 3, 3));
        String actual = "";
        try {
            holidayController.delete(holidayDTO, 1L);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        String expected = "The start date must be earlier than the finish date!";
        assertEquals(expected, actual);
    }


    @Test
    void delete_inputHolidayDTOWithStartDateIsEarlierThanBeginningDateOfEmployeeDTO_shouldReturnRegisterException() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(2L);
        holidayDTO.setEmployeeId(1L);
        holidayDTO.setStartDate(LocalDate.of(2021, 1, 2));
        holidayDTO.setFinishDate(LocalDate.of(2021, 2, 15));
        String actual = "";
        try {
            holidayController.delete(holidayDTO, 1L);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        String expected = "The beginning of employment must be earlier than the start date!";
        assertEquals(expected, actual);
    }

    @Test
    void delete_inputHolidayDTOAndDeleteBeginningDateOfEmployeeDTO_shouldReturnRegisterException() {
        employeeDTO.setBeginningOfEmployment(null);
        testRestTemplate.put(BASE_URL + "/employee/{id}/update", new HttpEntity<>(employeeDTO), 1);
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setId(2L);
        holidayDTO.setEmployeeId(1L);
        holidayDTO.setStartDate(LocalDate.of(2021, 4, 30));
        holidayDTO.setFinishDate(LocalDate.of(2021, 5, 5));
        String actual = "";
        try {
            holidayController.delete(holidayDTO, 1L);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        String expected = "The employee doesn't have beginning date!";
        assertEquals(expected, actual);
    }

    @Test
    void getAllBusinessDayByDateInterval_inputStartDateAndEndDate_shouldReturnRightMessage() {
        long id = 1;
        String startDate = "2021-01-01";
        String endDate = "2021-02-25";
        ResponseEntity<MessageDTO> allBusinessDayByDateInterval = holidayController.getAllBusinessDayByDateInterval(startDate, endDate, id);
        long days = 30;
        String expected = String.format("The employee with id:%d worked %d days from %s to %s!", id, days, startDate, endDate);
        String actual = allBusinessDayByDateInterval.getBody().getMessage();
        assertEquals(expected, actual);
    }

    @Test
    void getAllBusinessDayByDateInterval_inputInvalidStartDateAndEndDate_shouldReturnRegisterException() {
        long id = 1;
        String startDate = "";
        String endDate = "2021-02-25";
        String expected = "Invalid date format!";
        String actual = "";
        try {
            holidayController.getAllBusinessDayByDateInterval(startDate, endDate, id);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        assertEquals(expected, actual);
    }


    @Test
    void getAllBusinessDayByDateInterval_inputStartDateAndEndDateAndInvalidEmployeeDTO_shouldReturnRegisterException() {
        long id = 1;
        employeeDTO.setBeginningOfEmployment(null);
        testRestTemplate.put(BASE_URL + "/employee/{id}/update", new HttpEntity<>(employeeDTO), 1);
        String startDate = "2021-01-05";
        String endDate = "2021-02-25";
        String expected = "The beginning of employment date is null!";
        String actual = "";
        try {
            holidayController.getAllBusinessDayByDateInterval(startDate, endDate, id);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        assertEquals(expected, actual);
    }


    @Test
    void getAllBusinessDayByDateInterval_inputStartDateAndEndDateAndValidEmployeeDTO_shouldReturnRegisterException() {
        long id = 1;
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 26));
        testRestTemplate.put(BASE_URL + "/employee/{id}/update", new HttpEntity<>(employeeDTO), 1);
        String startDate = "2021-01-15";
        String endDate = "2021-01-25";
        String expected = String.format("The worker with id: %d has no beginning of employment date during that interval!", id);
        String actual = "";
        try {
            holidayController.getAllBusinessDayByDateInterval(startDate, endDate, id);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        assertEquals(expected, actual);
    }

    @Test
    void getAllBusinessDayByDateInterval_inputStartDateAndEndDateDoNotHaveHolidayDayDatabase_shouldReturnRegisterException() {
        long id = 1;
        testRestTemplate.delete(BASE_URL + "/holidayday/{id}/delete", id);
        String startDate = "2021-06-15";
        String endDate = "2021-06-25";
        String expected = "You don't have holiday day database for 2021!";
        String actual = "";
        try {
            holidayController.getAllBusinessDayByDateInterval(startDate, endDate, id);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        assertEquals(expected, actual);
    }


    @Test
    void getAllBusinessDayByYearAndMonth_inputYearAndMonth_shouldReturnRightMessage() {
        String year = "2021";
        String month = "4";
        long employeeId = 1;
        ResponseEntity<MessageDTO> allBusinessDayByYearAndMonth = holidayController.getAllBusinessDayByYearAndMonth(year, month, employeeId);
        long days = 20;
        String expected = String.format("The employee with id:%d worked %d days in a given month %s of a given year %s!", employeeId, days, month, year);
        assertEquals(expected, allBusinessDayByYearAndMonth.getBody().getMessage());
    }

    @Test
    void getAllBusinessDayByYearAndMonth_inputYearAndMonth_shouldReturnRegisterException() {
        String year = "";
        String month = "4";
        long employeeId = 1;
        String expected = "Invalid arguments!";
        String actual = "";
        try {
            holidayController.getAllBusinessDayByYearAndMonth(year, month, employeeId);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        assertEquals(expected, actual);
    }

    @Test
    void getHolidayByDateInterval_inputStartDateAndEndDate_shouldReturnRegisterException() {
        String startDate = "2021-06-15";
        String endDate = "";
        long employeeId = 1;
        String expected = "Invalid date format!";
        String actual = "";
        try {
            holidayController.getHolidayByDateInterval(startDate, endDate, employeeId);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        assertEquals(expected, actual);
    }

    @Test
    void getHolidayByDateInterval_inputStartDateAndEndDate_shouldReturnLocaldateSet() {
        long employeeId = 1;
        String startDate = "2021-02-01";
        String endDate = "2021-02-28";
        ResponseEntity<Set<LocalDate>> holidayByDateInterval = holidayController.getHolidayByDateInterval(startDate, endDate, employeeId);
        Set<LocalDate> expected = new TreeSet<>();
        expected.addAll(Set.of(LocalDate.of(2021, 2, 5), LocalDate.of(2021, 2, 8), LocalDate.of(2021, 2, 9), LocalDate.of(2021, 2, 10), LocalDate.of(2021, 2, 11), LocalDate.of(2021, 2, 12), LocalDate.of(2021, 2, 15), LocalDate.of(2021, 2, 25), LocalDate.of(2021, 2, 26)));
        assertEquals(expected, holidayByDateInterval.getBody());
    }

    @Test
    void getHolidayByDateInterval_inputStartDateAndEndDateWithoutEmployee_shouldReturnBadRequest() {
        long employeeId = 2;
        String startDate = "2021-02-01";
        String endDate = "2021-02-28";
        String actual = "";
        String expected = String.format("The employee entity doesn't exist with id: %d!", employeeId);
        try {
            holidayController.getHolidayByDateInterval(startDate, endDate, employeeId);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        assertEquals(expected, actual);
    }

    @Test
    void getAmountOfLeaveByDateInterval_inputStartDateAndEndDate_shouldReturnRightMessage() {
        long employeeId = 1;
        String startDate = "2021-02-01";
        String endDate = "2021-02-28";
        ResponseEntity<MessageDTO> amountOfLeaveByDateInterval = holidayController.getAmountOfLeaveByDateInterval(startDate, endDate, employeeId);
        String actual = amountOfLeaveByDateInterval.getBody().getMessage();
        long days = 9;
        String expected = String.format("The employee with id:%d number of leave %d days from %s to %s!", employeeId, days, startDate, endDate);
        assertEquals(expected, actual);
    }

    @Test
    void getAmountOfLeaveByDateInterval_inputStartDateAndEndDate_shouldReturnBadRequest() {
        long employeeId = 1;
        String startDate = "";
        String endDate = "2021-02-28";
        String actual = "";
        String expected = "Invalid date format!";
        try {
            holidayController.getAmountOfLeaveByDateInterval(startDate, endDate, employeeId);
        } catch (RegisterException ex) {
            actual = ex.getMessage();
        }
        assertEquals(expected, actual);
    }
}