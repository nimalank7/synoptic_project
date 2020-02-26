package uk.first_catering.kiosk.card.controller;

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
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.card.repository.CardRepository;
import uk.first_catering.kiosk.employee.model.Employee;

import javax.servlet.http.Cookie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = {CardResourceTest.Initializer.class})
@SpringBootTest
@AutoConfigureMockMvc
public class CardResourceTest {

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
    public void createsNewCard() throws Exception {
        String newCard = "{"
                + "\"card_id\":\"3\""
                + ",\"total\":0"
                + "}";

        mockMvc.perform(
                post("/card/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCard))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Your card has been created!")))
                .andExpect(jsonPath("$.card.total", is(0)))
                .andExpect(jsonPath("$.card.card_id", is("3")));
    }

    @Test
    public void tapInSuccessfully() throws Exception {
        mockMvc.perform(get("/card/1"))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = mockMvc.
                perform(get("/card/1"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message", is("Welcome: Nim")))
                .andExpect(jsonPath("$.card.total", is(100)))
                .andExpect(jsonPath("$.card.card_id", is("1")))
                .andReturn();
        Cookie setCookie = result.getResponse().getCookie("card_id");
        assertThat(setCookie.getValue(), is("1"));
        assertThat(setCookie.getMaxAge(), is(240));
    }

    @Test
    public void doesNotRecognizeCard() throws Exception {
        mockMvc.perform(get("/card/0"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("This card isn't recognized")));
    }

    @Test
    public void cardRequiresRegistration() throws Exception {
        MvcResult result = mockMvc.perform(get("/card/2"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("This card isn't registered")))
                .andExpect(jsonPath("$.card.total", is(100)))
                .andExpect(jsonPath("$.card.card_id", is("2")))
                .andReturn();
        Cookie cookies = result.getResponse().getCookie("card_id");
        assertThat(cookies.getValue(), is("2"));
        assertThat(cookies.getMaxAge(), is(240));
    }

    @Test
    public void topUpCard() throws Exception {
        Cookie cookie = new Cookie("card_id", "1");
        cookie.setMaxAge(240);
        cookie.setPath("/");

        String topUpRequest = "{"
                + "\"topUp\":100"
                + "}";

        mockMvc.perform(
                patch("/card/")
                        .cookie(cookie)
                        .content(topUpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("You have topped up by 100")))
                .andExpect(jsonPath("$.card.total", is(200)))
                .andExpect(jsonPath("$.card.card_id", is("1")));
    }

    @Test
    public void paySuccessfully() throws Exception {
        Cookie cookie = new Cookie("card_id", "1");
        cookie.setMaxAge(240);
        cookie.setPath("/");

        String topUpRequest = "{"
                + "\"pay\":50"
                + "}";

        mockMvc.perform(
                patch("/card/")
                        .cookie(cookie)
                        .content(topUpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("You have paid: 50. Your remaining total is: 50")))
                .andExpect(jsonPath("$.card.total", is(50)))
                .andExpect(jsonPath("$.card.card_id", is("1")));
    }

    @Test
    public void payUnsuccessfully() throws Exception {
        Cookie cookie = new Cookie("card_id", "1");
        cookie.setMaxAge(240);
        cookie.setPath("/");

        String topUpRequest = "{"
                + "\"pay\":500"
                + "}";

        mockMvc.perform(
                patch("/card/")
                        .cookie(cookie)
                        .content(topUpRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Insufficent funds for this payment.")));
    }

    @Test
    public void tapOutSuccessfully() throws Exception {
        Cookie cookie = new Cookie("card_id", "1");
        cookie.setMaxAge(240);
        cookie.setPath("/");
        MvcResult result = mockMvc.
                perform(get("/card/1").cookie(cookie))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message", is("You have successfully logged out")))
                .andReturn();
        Cookie deletedCookie = result.getResponse().getCookie("card_id");
        assertThat(deletedCookie.getValue(), is("1"));
        assertThat(deletedCookie.getMaxAge(), is(0));
    }
}