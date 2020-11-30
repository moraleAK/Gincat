package net.gincat.demo.jpx.query;

import net.gincat.jpax.CompareType;
import net.gincat.jpax.annotation.Query;

/**
 * @author Gin
 * @since 2020/11/30 11:00
 */
public class SchoolListTO {
    private String idNo;

    @Query(compare = CompareType.LIKE)
    private String name;

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
