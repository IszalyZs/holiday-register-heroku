package com.oh.register.controller;

import com.oh.register.converter.EmployeeToEmployeeDTO;
import com.oh.register.model.dto.EmployeeDTO;
import com.oh.register.model.dto.ErrorDTO;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class EmployeeControllerITTest {

    @LocalServerPort
    private Integer port;

    private String BASE_URL;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EmployeeToEmployeeDTO employeeToEmployeeDTO;

    private List<EmployeeDTO> employeeDTOS = new ArrayList<>();

    @BeforeEach
    public void init() {
        BASE_URL = "http://localhost:" + port + "/employee";
        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setBirthDate(LocalDate.of(1995, 1, 1));
        employeeDTO1.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO1.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO1.setFirstName("Kovács");
        employeeDTO1.setLastName("Péter");
        employeeDTO1.setIdentityNumber("221111111");
        employeeDTO1.setPosition("manager");
        employeeDTO1.setWorkplace("IBM");
        employeeDTO1.setId(1L);

        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setBirthDate(LocalDate.of(1990, 1, 1));
        employeeDTO2.setBeginningOfEmployment(LocalDate.of(2021, 2, 5));
        employeeDTO2.setDateOfEntry(LocalDate.of(2021, 2, 2));
        employeeDTO2.setFirstName("Kiss");
        employeeDTO2.setLastName("Péter");
        employeeDTO2.setIdentityNumber("111111111");
        employeeDTO2.setPosition("manager");
        employeeDTO2.setWorkplace("IBM");
        employeeDTO2.setId(2L);

        employeeDTOS.clear();
        employeeDTOS.addAll(Arrays.asList(employeeDTO1, employeeDTO2));

        for (EmployeeDTO employeeDTO : employeeDTOS) {
            testRestTemplate.postForObject(BASE_URL + "/add", new HttpEntity<>(employeeDTO), Employee.class);
        }
    }

    @Test
    void findAll_shouldReturnAllEmployeeDTO() {
        ResponseEntity<Employee[]> responseEntity = testRestTemplate.getForEntity(BASE_URL + "/all", Employee[].class);
        List<Employee> employeeList = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        List<EmployeeDTO> actual = employeeList.stream().map(employeeToEmployeeDTO::getEmployeeDTO).collect(Collectors.toList());
        List<EmployeeDTO> expected = employeeDTOS;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expected, actual);
    }

    @Test
    void findById_inputValidId_shouldReturnRightEmployeeDTO() {
        long id = 2;
        ResponseEntity<Employee> response = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", Employee.class, id);
        EmployeeDTO expected = employeeDTOS.get(1);
        EmployeeDTO actual = employeeToEmployeeDTO.getEmployeeDTO(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, actual);
    }

    @Test
    void findById_inputBadId_shouldReturnBadRequest() {
        long badId = 3;
        ResponseEntity<ErrorDTO> response = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", ErrorDTO.class, badId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String expected = "The employee entity doesn't exist with id: 3!";
        String actual = response.getBody().getError();
        assertEquals(expected, actual);
    }

    @Test
    void deleteById_inputValidId_shouldReturnRightMessage() {
        long id = 2;
        ResponseEntity<Employee> responseBeforeDelete = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", Employee.class, id);
        assertEquals(HttpStatus.OK, responseBeforeDelete.getStatusCode());
        assertEquals(employeeDTOS.get(1), employeeToEmployeeDTO.getEmployeeDTO(responseBeforeDelete.getBody()));

        testRestTemplate.delete(BASE_URL + "/{id}/delete", id);
        ResponseEntity<ErrorDTO> responseAfterDelete = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, responseAfterDelete.getStatusCode());
        String expected = "The employee entity doesn't exist with id: 2!";
        String actual = responseAfterDelete.getBody().getError();
        assertEquals(expected, actual);
    }


    @Test
    void save_inputEmployeeDTO_shouldReturnRightEmployeeDTO() {
        EmployeeDTO expected = new EmployeeDTO();
        expected.setBirthDate(LocalDate.of(1993, 1, 1));
        expected.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        expected.setDateOfEntry(LocalDate.of(2021, 1, 2));
        expected.setFirstName("Kovács");
        expected.setLastName("Rozália");
        expected.setIdentityNumber("222111111");
        expected.setPosition("manager");
        expected.setWorkplace("IBM");
        expected.setId(3L);

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(BASE_URL + "/add", new HttpEntity<>(expected), Employee.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EmployeeDTO actual = employeeToEmployeeDTO.getEmployeeDTO(response.getBody());
        assertEquals(expected, actual);
    }

    @Test
    void save_inputEmployeeDTOWithoutFirstNameAndLastName_shouldReturnBadRequest() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1993, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO.setFirstName("Kiss");
        employeeDTO.setLastName("");
        employeeDTO.setIdentityNumber("222111111");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");
        employeeDTO.setId(3L);

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/add", new HttpEntity<>(employeeDTO), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String expected = "Object name:employeeDTO, error code:NotBlank, error message:The last name field can't be empty!\n";
        String actual = response.getBody().getError();
        assertEquals(expected, actual);
    }


    @Test
    void save_inputEmployeeDTOWithDuplicatedIdentityNumber_shouldReturnBadRequest() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1993, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO.setFirstName("Kovács");
        employeeDTO.setLastName("Péter");
        employeeDTO.setIdentityNumber("111111111");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");
        employeeDTO.setId(3L);

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/add", new HttpEntity<>(employeeDTO), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String expected = "Duplicate entry at employees identity number:111111111 is already exists!";
        String actual = response.getBody().getError();
        assertEquals(expected, actual);
    }

    @Test
    void save_inputEmployeeDTOWithShorterIdentityNumber_shouldReturnBadRequest() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1993, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO.setFirstName("Kovács");
        employeeDTO.setLastName("Péter");
        employeeDTO.setIdentityNumber("11111111");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");
        employeeDTO.setId(3L);

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/add", new HttpEntity<>(employeeDTO), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String expected = "Object name:employeeDTO, error code:Pattern, error message:The identity number length should be exactly 9 numbers!\n";
        String actual = response.getBody().getError();
        assertEquals(expected, actual);
    }

    @Test
    void save_inputEmployeeDTOWithBeginningDateIsBeforeThanDateOfEntry_shouldReturnBadRequest() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1993, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 7));
        employeeDTO.setFirstName("Kovács");
        employeeDTO.setLastName("Péter");
        employeeDTO.setIdentityNumber("111111211");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");
        employeeDTO.setId(3L);

        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/add", new HttpEntity<>(employeeDTO), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String expected = "The date of entry must be earlier than the beginning of employment!";
        String actual = response.getBody().getError();
        assertEquals(expected, actual);
    }

    @Test
    void save_inputEmployeeDTOWithBirthDay1990_shouldReturnCorrectBasicLeave() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO.setFirstName("Kovács");
        employeeDTO.setLastName("Tamás");
        employeeDTO.setIdentityNumber("221121111");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");
        employeeDTO.setId(3L);

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(BASE_URL + "/add", new HttpEntity<>(employeeDTO), Employee.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        int actual = Objects.requireNonNull(response.getBody()).getBasicLeave();
        assertTrue(actual > 22);
    }

    @Test
    void update_inputEmployeeDTO_shouldReturnUpdatedEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1995, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO.setFirstName("Kovács");
        employeeDTO.setLastName("Tamás");
        employeeDTO.setIdentityNumber("221111111");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");
        employeeDTO.setId(1L);

        long id = 1;

        ResponseEntity<Employee> responseBeforeUpdate = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", Employee.class, id);
        assertEquals(HttpStatus.OK, responseBeforeUpdate.getStatusCode());
        EmployeeDTO expected = employeeDTOS.get(0);
        EmployeeDTO actual = employeeToEmployeeDTO.getEmployeeDTO(responseBeforeUpdate.getBody());
        assertEquals(expected, actual);

        testRestTemplate.put(BASE_URL + "/{id}/update", new HttpEntity<>(employeeDTO), 1);
        assertEquals(HttpStatus.OK, responseBeforeUpdate.getStatusCode());
        ResponseEntity<Employee> responseAfterUpdate = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", Employee.class, id);
        assertEquals("Tamás", Objects.requireNonNull(responseAfterUpdate.getBody()).getLastName());
    }

    @Test
    void update_inputInvalidEmployeeDTO_shouldReturnDTOWithoutUpdate() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1995, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO.setFirstName("");
        employeeDTO.setLastName("");
        employeeDTO.setIdentityNumber("221111111");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");
        employeeDTO.setId(1L);

        long id = 1;

        ResponseEntity<Employee> responseBeforeUpdate = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", Employee.class, id);
        assertEquals(HttpStatus.OK, responseBeforeUpdate.getStatusCode());
        EmployeeDTO expected = employeeDTOS.get(0);
        EmployeeDTO actual = employeeToEmployeeDTO.getEmployeeDTO(responseBeforeUpdate.getBody());
        assertEquals(expected, actual);

        testRestTemplate.put(BASE_URL + "/{id}/update", new HttpEntity<>(employeeDTO), 1);
        assertEquals(HttpStatus.OK, responseBeforeUpdate.getStatusCode());
        ResponseEntity<Employee> responseAfterUpdate = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", Employee.class, id);
        assertEquals("Kovács Péter", Objects.requireNonNull(responseAfterUpdate.getBody()).getFirstName() + " " + responseAfterUpdate.getBody().getLastName());
    }

}