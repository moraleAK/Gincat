package net.gincat.demo.jpx.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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

    @OneToMany(mappedBy = "clazz")
    private List<Student> students;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Teacher getMaster() {
        return master;
    }

    public void setMaster(Teacher master) {
        this.master = master;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
