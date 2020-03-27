package ru.job4j.cars.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.forms.ActionListForm;
import ru.job4j.cars.forms.AddAdsForm;
import ru.job4j.cars.models.*;
import ru.job4j.cars.services.*;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/cars")
public class CarsController {

    @Autowired
    private UserService userdb;
    @Autowired
    private MarkService mdb;
    @Autowired
    private ModelService mddb;
    @Autowired
    private CarService acdb;
    @Autowired
    private CityService citydb;
    @Autowired
    private TransmissionService trdb;
    @Autowired
    private BodytypeService bddb;
    @Autowired
    private EnginestypeService edb;
    @Autowired
    private DriveunitService drdb;
    @Autowired
    private WheelService wdb;
    @Autowired
    private FotoService fdb;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String loginWithUser() {
        return "list";
    }

    @GetMapping(value = "/list")
    public String getListAds(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        List<CarEntity> list = acdb.findAll();
        if (list != null) {
            list.forEach((x) -> x.setDate(sdf.format(x.getCreated())));
        }
        model.addAttribute("carlist", list);
        model.addAttribute("MarkEntityList", mdb.findAll());
        return "list";
    }


    @PostMapping(value = "/list", consumes = "application/json")
    public String getListAdsAction(@RequestBody ActionListForm actlist, Model model) {
        List<String> params = new ArrayList<>();
        params.add(actlist.getAction());
        params.add(actlist.getMark());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        List<CarEntity> result = acdb.findAllCarsWithFilter(params);
        if (result != null) {
            result.forEach((x) -> x.setDate(sdf.format(x.getCreated())));
        }
        model.addAttribute("carlist", result);
        return "table";
    }

    @GetMapping(value = "/add")
    public String getNewAdForm(Model model) {
        model.addAttribute("CityEntityList", citydb.findAll());
        model.addAttribute("MarkEntityList", mdb.findAll());
        model.addAttribute("TransmissionEntityList", trdb.findAll());
        model.addAttribute("BodytypeEntityList", bddb.findAll());
        model.addAttribute("EnginestypeEntityList", edb.findAll());
        model.addAttribute("DriveunitEntityList", drdb.findAll());
        model.addAttribute("WheelEntityList", wdb.findAll());
        return "add";
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public String postNewAdForm(@ModelAttribute AddAdsForm form, Principal princ) throws IOException {
        CarEntity car = new CarEntity(form.getNote());
        car.setUser(userdb.findByName(princ.getName()));
        car.setCity(citydb.findById(citydb.findIdByModel(new CityEntity(form.getCity()))));
        car.setMark(mdb.findById(mdb.findIdByModel(new MarkEntity(form.getMark()))));
        car.setModel(mddb.findById(mddb.findIdByModel(new ModelEntity(form.getModel(), car.getMark()))));
        car.setPrice(form.getPrice());
        car.setIssue(form.getIssue());
        car.setDist(form.getDist());
        car.setTrans(trdb.findById(trdb.findIdByModel(new TransmissionEntity(form.getTrans()))));
        car.setBtype(bddb.findById(bddb.findIdByModel(new BodytypeEntity(form.getBody()))));
        car.setEtype(edb.findById(edb.findIdByModel(new EnginestypeEntity(form.getEngine()))));
        car.setEnginecapacity(form.getEnginecapacity());
        car.setPower(form.getPower());
        car.setDunit(drdb.findById(drdb.findIdByModel(new DriveunitEntity(form.getDrive()))));
        car.setWheel(wdb.findById(wdb.findIdByModel(new WheelEntity(form.getWheel()))));
        car.setOld(false);
        Set<FotoEntity> fotos = new HashSet<>();
        FotoEntity ft = null;
        for (var i = 0; i < form.getUpfile().length && form.getUpfile()[i].getSize() != 0; i++) {
            ft = new FotoEntity();
            ft.setName(form.getUpfile()[i].getOriginalFilename());
            ft.setFoto(form.getUpfile()[i].getBytes());
            ft.setCar(car);
            fotos.add(ft);
        }
        car.setFotos(fotos);
        acdb.add(car);
        return "list";
    }

    @PostMapping(value = "/models")
    @ResponseBody
    public List<String> getAllModels(@RequestParam(value = "mark") String mark) {
        List<ModelEntity> mlist = mddb.findAll();
        return mlist.stream().filter((x) -> mark.equals(x.getMark().getName())).map(AbsProjectEntity::getName).collect(Collectors.toList());
    }

    @GetMapping(value = "/ad")
    public String showAd(@RequestParam(value = "id") int id, Model model, Principal princ) {
        if (princ != null) {
            model.addAttribute("loginUser", princ.getName());
        }
        model.addAttribute("curcar", acdb.findById(id));
        return "car";
    }

    @PostMapping(value = "/ad")
    public String setAttrAd(@RequestParam(value = "id") int id) {
        CarEntity car = acdb.findById(id);
        car.setOld(!car.isOld());
        acdb.update(car);
        return "redirect:/cars/list";
    }

    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> getImageAsResponseEntity(@RequestParam(value = "cid") int cid) {
        HttpHeaders headers = new HttpHeaders();
        byte[] media = fdb.findById(cid).getFoto();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }

}
