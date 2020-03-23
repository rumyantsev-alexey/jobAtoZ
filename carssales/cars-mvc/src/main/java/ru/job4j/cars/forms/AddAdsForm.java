package ru.job4j.cars.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AddAdsForm {
    private String note;
    private String city;
    private String mark;
    private String model;
    private int price;
    private int issue;
    private int dist;
    private String trans;
    private String body;
    private String engine;
    private int enginecapacity;
    private int power;
    private String drive;
    private String wheel;
    private MultipartFile[] upfile;
}
