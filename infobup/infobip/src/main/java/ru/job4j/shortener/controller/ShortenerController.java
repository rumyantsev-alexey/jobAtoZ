package ru.job4j.shortener.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortener.forms.AccAnswer;
import ru.job4j.shortener.forms.ShortUrlAnswer;
import ru.job4j.shortener.models.Link;
import ru.job4j.shortener.models.User;
import ru.job4j.shortener.services.LinksService;
import ru.job4j.shortener.services.UsersService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "API для укорачивания ссылок", description = "API для укорачивания ссылок")
public class ShortenerController {

    @Autowired
    private UsersService usserv;
    @Autowired
    private LinksService linkserv;

    @ApiOperation(value = "создание нового аккаунта для работы с api", response = AccAnswer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "все хорошо"),
            @ApiResponse(code = 400, message = "не указан аккаунт")})
    @PostMapping(value = "/account")
    public ResponseEntity<AccAnswer> addAccount(@ApiParam(value = "имя нового аккаунта", required = true) @RequestParam(value = "AccountId") String name) {
        AccAnswer answer = new AccAnswer();
        if (usserv.findUserByName(name) != null) {
            answer.setResult(false);
            answer.setDescription("This account already uses");
        } else {
            answer.setResult(true);
            answer.setDescription("Your account is opened");
            answer.setPassword(usserv.createUserByUsername(name));
        }
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @ApiOperation(value = "регистрация короткой ссылки", authorizations = {@Authorization(value = "basicAuth")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "все хорошо"),
            @ApiResponse(code = 400, message = "не указана ссылка"),
            @ApiResponse(code = 401, message = "пользователь не прошел аутентификацию"),
            @ApiResponse(code = 404, message = "ссылка не рабочая")})
    @PostMapping(value = "/register")
    public ResponseEntity<ShortUrlAnswer> shotUrl(@ApiParam(value = "ссылка для укорачивания", required = true) @RequestParam(value = "url") String url, @ApiParam(value = "тип редиректа") @RequestParam(value = "redirectType", required = false) String redir, Principal princ) {
        User user = usserv.findUserByName(princ.getName());
        ResponseEntity<ShortUrlAnswer> res = null;
        try {
            URL urltest = new URL(url);
            URLConnection conn = urltest.openConnection();
            conn.connect();
            HttpStatus status = (redir != null && redir.equals("301")) ? HttpStatus.MOVED_PERMANENTLY : HttpStatus.FOUND;
            res = new ResponseEntity<>(new ShortUrlAnswer(linkserv.getShotLink(url, user, status)), HttpStatus.OK);
        } catch (MalformedURLException e) {
            res = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            res = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return res;
    }

    @ApiOperation(value = "просмотр статистики по существующему аккаунту", authorizations = {@Authorization(value = "basicAuth")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "все хорошо"),
            @ApiResponse(code = 401, message = "пользователь не прошел аутентификацию"),
            @ApiResponse(code = 403, message = "текущий пользователь не авторизован для получения этой информации"),
            @ApiResponse(code = 404, message = "нет такого аккаунта")})
    @GetMapping(value = "/statistic/{AccountId}")
    public ResponseEntity<Map<String, Integer>> getStatistic(@ApiParam(value = "имя аккаунта", required = true) @PathVariable(value = "AccountId") String account, Principal princ) {
        Map<String, Integer> result = new HashMap<>();
        ResponseEntity<Map<String, Integer>> res = null;
        User user = usserv.findUserByName(account);
        if (user == null) {
            res = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!account.equals(princ.getName())) {
            res = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            List<Link> list = user.getLinks();
            list.forEach((x) -> result.put(x.getSource(), x.getCount()));
            res = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return res;
    }

    @ApiOperation(value = "вызов ссылки по короткому адресу (кнопка Try it out не отображает правильно успех операции, т к в этом случаи происходит переход по ссылку а не возврат информации)", authorizations = {@Authorization(value = "basicAuth")})
    @ApiResponses(value = {
            @ApiResponse(code = 301, message = "переход по ссылке"),
            @ApiResponse(code = 302, message = "переход по ссылке"),
            @ApiResponse(code = 401, message = "пользователь не прошел аутентификацию"),
            @ApiResponse(code = 403, message = "текущий пользователь не авторизован для получения этой информации"),
            @ApiResponse(code = 404, message = "нет такой ссылки")})
    @ResponseStatus(code = HttpStatus.FOUND)
    @GetMapping(value = "/slink/{ShortUrl}")
    public ResponseEntity<Object> goToLink(@ApiParam(value = "короткая ссылка", required = true) @PathVariable(value = "ShortUrl") String shotUrl, Principal princ) throws IOException {
        ResponseEntity<Object> res = null;
        Link link = linkserv.findByResult(shotUrl);
        if (link == null) {
            res = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!link.getUser().getName().equals(princ.getName())) {
            res = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", link.getSource());
            res = new ResponseEntity<>(headers, link.getStatus());
            link.setCount(link.getCount() + 1);
            linkserv.save(link);
        }
        return res;
    }
}
