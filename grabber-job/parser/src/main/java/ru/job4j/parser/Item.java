package ru.job4j.parser;

import java.net.URL;
import java.sql.Timestamp;

/**
 * Класс описывает структуру записи вакансии
 */
public class Item {
    private int id;
    private String subj;
    private URL link;
    private Timestamp created;

    public Item(final String subj, final URL link, final Timestamp creat) {
        this.subj = subj;
        this.link = link;
        this.created = creat;
        this.id = hashCode();
    }

    public int getId() {
        return id;
    }

    public String getSubj() {
        return subj;
    }

    public URL getLink() {
        return link;
    }

    public Timestamp getCreated() {
        return created;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Item item = (Item) o;

        if (subj != null ? !subj.equals(item.subj) : item.subj != null) {
            return false;
        }
        return link != null ? link.equals(item.link) : item.link == null;
    }

    @Override
    public int hashCode() {
        int result = subj != null ? subj.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
