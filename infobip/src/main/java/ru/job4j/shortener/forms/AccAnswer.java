package ru.job4j.shortener.forms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class AccAnswer {
    @ApiModelProperty(value = "создан аккаунт или нет")
    private boolean result;

    @ApiModelProperty(value = "описание результата")
    private String description;

    @ApiModelProperty(value = "сгенерированный пароль у новому аккаунту")
    private String password;
}
