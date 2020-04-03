package ru.job4j.cars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.job4j.cars.forms.ActionListForm;
import ru.job4j.cars.models.CarEntity;
import ru.job4j.cars.models.FotoEntity;
import ru.job4j.cars.services.*;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class CarsControllerTest {
    @Autowired
    private UserService userds;
    @Autowired
    private MarkService ms;
    @Autowired
    private ModelService mds;
    @Autowired
    private CarService acs;
    @Autowired
    private CityService citys;
    @Autowired
    private TransmissionService trs;
    @Autowired
    private BodytypeService bds;
    @Autowired
    private EnginestypeService es;
    @Autowired
    private DriveunitService drs;
    @Autowired
    private WheelService ws;
    @Autowired
    private FotoService fs;

    @Autowired
    private MockMvc mockMvc;

    @After
    public void clearCars() {
        acs.findAll().forEach((x) -> acs.delete(x.getId()));
    }

    @Test
    public void getLoginPage() throws Exception {
        this.mockMvc.perform(get("/cars/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void whenLoginSuccessThenShowListAds() throws Exception {
        this.mockMvc.perform(post("/cars/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"));
    }

    @Test
    public void getShowListAds() throws Exception {
        this.mockMvc.perform(get("/cars/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("carlist"))
                .andExpect(model().attributeExists("MarkEntityList"))
                .andExpect(view().name("list"));
    }

    @Test
    public void whenPostFilterThenGetListAdsWithFilters() throws Exception {
        ActionListForm form = new ActionListForm();
        form.setAction("actual");
        form.setMark("VAZ");
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/cars/list")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(form))
                ).andExpect(status().isOk())
                .andExpect(model().attributeExists("carlist"))
                .andExpect(view().name("table"));
     }

    @Test
    @WithUserDetails(value = "admin")
    public void getAdFormWithAuth() throws Exception {
        this.mockMvc.perform(get("/cars/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("CityEntityList"))
                .andExpect(model().attributeExists("MarkEntityList"))
                .andExpect(model().attributeExists("TransmissionEntityList"))
                .andExpect(model().attributeExists("BodytypeEntityList"))
                .andExpect(model().attributeExists("EnginestypeEntityList"))
                .andExpect(model().attributeExists("DriveunitEntityList"))
                .andExpect(model().attributeExists("WheelEntityList"))
                .andExpect(view().name("add"));
    }

    @Test
    public void whenGetAdFormWithAnonymThenRedirToLogin() throws Exception {
        this.mockMvc.perform(get("/cars/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(value = "admin")
    public void whenPostAdFormWithAuth() throws Exception {
        MockMultipartFile file = new MockMultipartFile("upfile", "originalFileName", "text/plain", "testingstring".getBytes());
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/cars/add")
                        .file(file)
                        .param("note", "test test")
                        .param("city", "London")
                        .param("mark", "VAZ")
                        .param("model", "Model25")
                        .param("trans", "automat")
                        .param("body", "limuzin")
                        .param("engine", "hybrid")
                        .param("drive", "zadnij")
                        .param("wheel", "right");
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(view().name("list"));
    }

    @Test
    public void whenPostMarkThenGetListModelsSameMark() throws Exception {
        String mark = "VAZ";
        MvcResult res = this.mockMvc.perform(post("/cars/models")
                            .param("mark", mark)
                            ).andExpect(status().isOk())
                            .andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Model25"));
    }

    @Test
    @WithUserDetails(value = "admin")
    public void getAdWithAuth() throws Exception {
        CarEntity car = new CarEntity();
        car.setNote("test");
        car.setCity(citys.findByName("London"));
        car.setMark(ms.findByName("VAZ"));
        car.setModel(mds.findByName("Model25"));
        car.setTrans(trs.findByName("automat"));
        car.setBtype(bds.findByName("hetchbag"));
        car.setEtype(es.findByName("hybrid"));
        car.setDunit(drs.findByName("zadnij"));
        car.setWheel(ws.findByName("right"));
        car.setUser(userds.findByName("admin"));
        int i = acs.add(car);
        this.mockMvc.perform(get("/cars/ad")
                .param("id", Integer.toString(i))
                ).andExpect(status().isOk())
                .andExpect(model().attributeExists("loginUser"))
                .andExpect(model().attributeExists("curcar"))
                .andExpect(view().name("car"));
    }
    @Test
    public void getAdWithAnon() throws Exception {
        this.mockMvc.perform(get("/cars/ad"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void whenSendIdThenRecievePicture() throws Exception {
        FotoEntity foto = new FotoEntity();
        foto.setName("test");
        foto.setFoto("testingstring".getBytes());
        int i = fs.add(foto);
        MvcResult res = this.mockMvc.perform(get("/cars/image")
                .param("cid", Integer.toString(i))
                ).andExpect(status().isOk())
                .andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("testingstring"));
    }

}