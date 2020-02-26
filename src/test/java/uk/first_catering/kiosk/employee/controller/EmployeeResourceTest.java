package uk.first_catering.kiosk.employee.controller;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.card.repository.CardRepository;
import uk.first_catering.kiosk.employee.model.Employee;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = {EmployeeResourceTest.Initializer.class})
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeResourceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CardRepository cardRepository;

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=" + "none",
                    "spring.liquibase.change-log=classpath:/db/changelog/liquibase-changelog.xml",
                    "spring.liquibase.enabled=" + true
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Before
    public void setUp() {
        Card card = new Card();
        Employee employee = new Employee();
        card.setTotal(100);
        card.setCardId("1");
        card.setEmployee(employee);

        employee.setName("Nim");
        employee.setEmail("email");
        employee.setEmployeeId("123");
        employee.setPhone("phone");
        employee.setPin("1234");
        employee.setCard(card);
        cardRepository.save(card);

        Card unregisteredCard = new Card();
        unregisteredCard.setCardId("2");
        unregisteredCard.setTotal(100);
        cardRepository.save(unregisteredCard);
    }

    @Test
    public void timedOutWhileRegistering() throws Exception {
        String newEmployee = "{"
                + "\"employee_id\":\"1\""
                + ",\"name\":\"Nim\""
                + ",\"email\":\"email\""
                + ",\"phone\":\"phone\""
                + ",\"pin\":\"pin\""
                + ",\"card_id\":\"1234\""
                + "}";

        mockMvc.perform(
                post("/employee/")
                .content(newEmployee)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(408))
                .andExpect(jsonPath("$.message", is("You were timed out. Please tap in again.")));
    }

    @Test
    public void registerCard() throws Exception {
        Cookie cookie = new Cookie("card_id", "2");
        cookie.setMaxAge(240);
        cookie.setPath("/");

        String newEmployee = "{"
                + "\"employee_id\":\"1\""
                + ",\"name\":\"Nim\""
                + ",\"email\":\"email\""
                + ",\"phone\":\"phone\""
                + ",\"pin\":\"pin\""
                + "}";

        mockMvc.perform(
                post("/employee/")
                        .cookie(cookie)
                        .content(newEmployee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Your card has been registered.")))
                .andExpect(jsonPath("$.employee.name", is("Nim")))
                .andExpect(jsonPath("$.employee.phone", is("phone")))
                .andExpect(jsonPath("$.employee.employee_id", is("1")))
                .andExpect(jsonPath("$.employee.email", is("email")));
    }

    @Test
    public void updateEmployee() throws Exception {
        Cookie cookie = new Cookie("card_id", "1");
        cookie.setMaxAge(240);
        cookie.setPath("/");

        String newEmployee = "{"
                + "\"employee_id\":\"123\""
                + ",\"name\":\"Nim\""
                + ",\"email\":\"email\""
                + ",\"phone\":\"phone\""
                + ",\"pin\":\"pin\""
                + "}";

        mockMvc.perform(
                patch("/employee/")
                        .cookie(cookie)
                        .content(newEmployee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Your details have been updated")))
                .andExpect(jsonPath("$.employee.name", is("Nim")))
                .andExpect(jsonPath("$.employee.phone", is("phone")))
                .andExpect(jsonPath("$.employee.employee_id", is("123")))
                .andExpect(jsonPath("$.employee.email", is("email")));
    }
}