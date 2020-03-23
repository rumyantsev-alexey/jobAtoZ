package ru.job4j.shortener.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.job4j.shortener.repositories.UsersRepository;
import ru.job4j.shortener.services.LinksService;
import ru.job4j.shortener.services.UsersService;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(scripts = {"/addTestUser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ShortenerControllerTest {
    @Autowired
    private UsersService ussr;
    @Autowired
    private LinksService lisv;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addNewAccount() throws Exception {
        MvcResult res = this.mockMvc.perform(post("/account")
                .param("AccountId", "test555")
                ).andExpect(status().isOk())
                .andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Your account is opened"));
    }

    @Test
    void addExistAccount() throws Exception {
        MvcResult res = this.mockMvc.perform(post("/account")
                .param("AccountId", "testuser")
                ).andExpect(status().isOk())
                .andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("This account already uses"));
    }

    @Test
    void addAccountWithoutAccount() throws Exception {
        this.mockMvc.perform(post("/account"))
        .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "testuser")
    void createShotUrl() throws Exception {
        MvcResult res = this.mockMvc.perform(post("/register")
                .param("url", "http://ya.ru")
                ).andExpect(status().isOk())
                .andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("url"));
    }

    @Test
    @WithUserDetails(value = "testuser")
    void createShotUrlWithoutUrl() throws Exception {
        this.mockMvc.perform(post("/register"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createShotUrlWithoutAuth() throws Exception {
        this.mockMvc.perform(post("/register")
                .param("url", "http://ya.ru")
                ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "testuser")
    void createShotUrlWithNoValidUrl() throws Exception {
        this.mockMvc.perform(post("/register")
                .param("url", "http://ya.rur")
        ).andExpect(status().isNotFound());
   }

    @Test
    @WithUserDetails(value = "testuser")
    void getStatisticAccountWithSameAuth() throws Exception {
        createShotUrl();
        MvcResult res = this.mockMvc.perform(get("/statistic/testuser"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("http"));
    }

    @Test
    @WithUserDetails(value = "testuser")
    void getStatisticWithoutAccount() throws Exception {
        this.mockMvc.perform(get("/statistic"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "testuser")
    void getStatisticWithNonExistAccount() throws Exception {
        this.mockMvc.perform(get("/statistic/fhfhf"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStatisticAccountWithoutAuth() throws Exception {
        this.mockMvc.perform(get("/statistic/testuser"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "testuser")
    void getStatisticAccountWithForbAuth() throws Exception {
        ussr.createUserByUsername("test5");
        this.mockMvc.perform(get("/statistic/test5"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "testuser")
    void goToShotLinkWith301() throws Exception {
        String sl = lisv.getShotLink("http://ya.ru", ussr.findUserByName("testuser"), HttpStatus.MOVED_PERMANENTLY);
        this.mockMvc.perform(get("/slink/" + sl))
                .andExpect(status().isMovedPermanently());
    }

    @Test
    @WithUserDetails(value = "testuser")
    void goToShotLinkWith302() throws Exception {
        String sl = lisv.getShotLink("http://ya.ru", ussr.findUserByName("testuser"), HttpStatus.FOUND);
        this.mockMvc.perform(get("/slink/" + sl))
                .andExpect(status().isFound());
    }

    @Test
    void goToShotLinkWithoutAuth() throws Exception {
        String sl = lisv.getShotLink("http://ya.ru", ussr.findUserByName("testuser"), HttpStatus.FOUND);
        this.mockMvc.perform(get("/slink/" + sl))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "testuser")
    void goToShotLinkWithoutShotLink() throws Exception {
        this.mockMvc.perform(get("/slink/"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "testuser")
    void goToShotLinkWithForbAuth() throws Exception {
        ussr.createUserByUsername("test5");
        String sl = lisv.getShotLink("http://ya.ru", ussr.findUserByName("test5"), HttpStatus.FOUND);
        this.mockMvc.perform(get("/slink/" + sl))
                .andExpect(status().isForbidden());
    }

}