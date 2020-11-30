package net.gincat.demo.jpx.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Gin
 * @since 2020/11/29 21:31
 */
@MappedSuperclass
public abstract class BaseEntity {
    @GeneratedValue
    @Id
    private long id;

    @Column(name = "is_deleted")
    private boolean deleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
