package ru.job4j.todo;

import java.sql.Timestamp;

/**
 * Класс определяет сущность ToDo
 */
public class ToDo {
    private int id;
    private String descr;
    private Timestamp created;
    private Boolean done;

    public ToDo() {

    }

    public ToDo(String desc) {
        this.descr = desc;
        this.done = false;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String desc) {
        this.descr = desc;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ToDo toDo = (ToDo) o;

        return descr.equals(toDo.descr);
    }

    @Override
    public int hashCode() {
        return descr.hashCode();
    }
}
