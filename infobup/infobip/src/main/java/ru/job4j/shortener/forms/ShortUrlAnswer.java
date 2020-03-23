package ru.job4j.shortener.forms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@ApiModel
public class ShortUrlAnswer {
    @ApiModelProperty(value = "укороченный 'url'")
    @Getter
    private String url;
}
