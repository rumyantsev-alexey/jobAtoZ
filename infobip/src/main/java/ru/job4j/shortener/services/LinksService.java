package ru.job4j.shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortener.RandomString;
import ru.job4j.shortener.models.Link;
import ru.job4j.shortener.models.User;
import ru.job4j.shortener.repositories.LinksRepository;

@Service
public class LinksService {
    private static final int SURL_LEN = 6;

    @Autowired
    private LinksRepository linkr;

    @Transactional
    public String getShotLink(String dest, User user, HttpStatus status) {
        String surl = RandomString.genString(SURL_LEN);
        Link newlink = new Link();
        newlink.setSource(dest);
        newlink.setResult(surl);
        newlink.setStatus(status);
        newlink.setUser(user);
        linkr.save(newlink);
        return surl;
    }

    public Link findByResult(String result) {
        return linkr.findByResult(result);
    }

    public Link save(Link link) {
        return linkr.save(link);
    }
}
