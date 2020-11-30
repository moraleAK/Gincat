package net.gincat.demo.jpx.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Gin
 * @since 2020/11/29 21:34
 */
@Entity
@Table(name = "clazz")
public class Clazz extends BaseEntity {
    private String name;

    private String address;

    private String description;

    @ManyToOne
    private Teacher master;
}
