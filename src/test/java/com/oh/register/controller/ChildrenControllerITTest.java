package com.oh.register.controller;

import com.oh.register.model.dto.ChildrenDTO;
import com.oh.register.model.dto.EmployeeDTO;
import com.oh.register.model.dto.ErrorDTO;
import com.oh.register.model.entity.Children;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ChildrenControllerITTest {

    @LocalServerPort
    private Integer port;

    private String BASE_URL;

    private List<ChildrenDTO> childrenDTOS = new ArrayList<>();
    private EmployeeDTO employeeDTO;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void init() {
        BASE_URL = "http://localhost:" + port + "/children";
        employeeDTO = new EmployeeDTO();
        employeeDTO.setBirthDate(LocalDate.of(1995, 1, 1));
        employeeDTO.setBeginningOfEmployment(LocalDate.of(2021, 1, 5));
        employeeDTO.setDateOfEntry(LocalDate.of(2021, 1, 2));
        employeeDTO.setFirstName("Kovács");
        employeeDTO.setLastName("Péter");
        employeeDTO.setIdentityNumber("111111111");
        employeeDTO.setPosition("manager");
        employeeDTO.setWorkplace("IBM");


        ChildrenDTO children1 = new ChildrenDTO();
        children1.setId(1L);
        children1.setFirstName("Kiss");
        children1.setLastName("Péter");
        children1.setBirthDay(LocalDate.of(2020, 1, 1));
        ChildrenDTO children2 = new ChildrenDTO();
        children2.setId(2L);
        children2.setFirstName("Kiss");
        children2.setLastName("Ilona");
        children2.setBirthDay(LocalDate.of(2018, 1, 1));
        ChildrenDTO children3 = new ChildrenDTO();
        children3.setId(3L);
        children3.setFirstName("Molnár");
        children3.setLastName("Tamás");
        children3.setBirthDay(LocalDate.of(2019, 1, 1));

        testRestTemplate.postForObject("http://localhost:" + port + "/employee/add", new HttpEntity<>(employeeDTO), Employee.class);

        childrenDTOS.clear();
        childrenDTOS.addAll(Arrays.asList(children1, children2, children3));
        long id = 1;
        for (ChildrenDTO children : childrenDTOS) {
            testRestTemplate.postForObject(BASE_URL + "/employee/{id}/add", new HttpEntity<>(children), ChildrenDTO.class, id);
        }
    }


    @Test
    void findAll_shouldReturnAllChildrenDTO() {
        ResponseEntity<ChildrenDTO[]> response = testRestTemplate.getForEntity(BASE_URL + "/all", ChildrenDTO[].class);
        List<ChildrenDTO> actual = Arrays.asList(Objects.requireNonNull(response.getBody()));
        List<ChildrenDTO> expected = childrenDTOS;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, actual);
    }

    @Test
    void findById_inputValidId_shouldReturnRightChlidrenDTO() {
        long id = 3;
        ResponseEntity<ChildrenDTO> response = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", ChildrenDTO.class, id);
        ChildrenDTO expected = childrenDTOS.get(2);
        ChildrenDTO actual = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, actual);
    }

    @Test
    void findById_inputBadId_shouldReturnBadRequest() {
        long badId = 4;
        ResponseEntity<ErrorDTO> response = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", ErrorDTO.class, badId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String expected = "The children entity doesn't exist with id: 4!";
        String actual = response.getBody().getError();
        assertEquals(expected, actual);
    }

    @Test
    void deleteById_inputValidId_shouldReturnRightMessage() {
        long id = 3;
        ResponseEntity<ChildrenDTO> responseBeforeDelete = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", ChildrenDTO.class, id);
        assertEquals(HttpStatus.OK, responseBeforeDelete.getStatusCode());
        assertEquals(childrenDTOS.get(2), responseBeforeDelete.getBody());

        testRestTemplate.delete(BASE_URL + "/{id}/delete", id);
        ResponseEntity<ErrorDTO> responseAfterDelete = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, responseAfterDelete.getStatusCode());
        String expected = "The children entity doesn't exist with id: 3!";
        String actual = responseAfterDelete.getBody().getError();
        assertEquals(expected, actual);
    }

    @Test
    void save_inputChildrenDTO_shouldReturnRightChlidrenDTO() {
        long id = 1;
        ChildrenDTO expected = new ChildrenDTO();
        expected.setId(4L);
        expected.setFirstName("Kiss");
        expected.setLastName("Péter");
        expected.setBirthDay(LocalDate.of(2020, 1, 1));
        ResponseEntity<ChildrenDTO> response = testRestTemplate.postForEntity(BASE_URL + "/employee/{id}/add", new HttpEntity<>(expected), ChildrenDTO.class, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ChildrenDTO actual = response.getBody();
        assertEquals(expected, actual);
    }

    @Test
    void save_inputChildrenDTOWithInvalidBirthDayDate_shouldReturnBadRequest() {
        long id = 1;
        ChildrenDTO children = new ChildrenDTO();
        children.setId(4L);
        children.setFirstName("Kiss");
        children.setLastName("Péter");
        children.setBirthDay(LocalDate.of(2022, 1, 1));
        ResponseEntity<ErrorDTO> response = testRestTemplate.postForEntity(BASE_URL + "/employee/{id}/add", new HttpEntity<>(children), ErrorDTO.class, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String expected = "Object name:childrenDTO, error code:Past, error message:Date of birth must be less than today!\n";
        String actual = response.getBody().getError();
        assertEquals(expected, actual);
    }


    @Test
    void update_inputChildrenDTO_shouldReturnUpdatedChildrenDTO() {
        long employeeId = 1;
        ChildrenDTO children = new ChildrenDTO();
        children.setId(4L);
        children.setFirstName("Kiss");
        children.setLastName("Péter");
        children.setBirthDay(LocalDate.of(2020, 1, 1));
        ResponseEntity<ChildrenDTO> response = testRestTemplate.postForEntity(BASE_URL + "/employee/{id}/add", new HttpEntity<>(children), ChildrenDTO.class, employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String expected = "Kiss";
        String actual = Objects.requireNonNull(response.getBody()).getFirstName();
        assertEquals(expected, actual);
        children.setFirstName("Kovács");
        long id = 4;
        testRestTemplate.put(BASE_URL + "/{id}/update", new HttpEntity<>(children), id);
        ResponseEntity<ChildrenDTO> updatedChildrenDTO = testRestTemplate.getForEntity(BASE_URL + "/{id}/get", ChildrenDTO.class, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        expected = "Kovács";
        actual = Objects.requireNonNull(updatedChildrenDTO.getBody()).getFirstName();
        assertEquals(expected, actual);
    }
}