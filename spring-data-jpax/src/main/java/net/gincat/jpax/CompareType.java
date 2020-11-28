package net.gincat.jpax;

/**
 * @author Gin
 * @since 2019/3/8 15:20
 */
public enum CompareType {

    GRATER_THAN(" > "),

    GRATER_THAN_OR_EQUAL(" >= "),

    LESS_THAN(" < "),

    LESS_THAN_OR_EQUAL(" <= "),

    EQUAL(" = "),

    NOT_EQUAL(" != "),

    LIKE(" LIKE '%%' "),

    LEFT_LIKE(" LIKE "),

    NOT_LIKE(" NOT LIKE "),

    RIGHT_LIKE(" LIKE "),

    NOT_IN(" NOT IN() "),

    IN(" IN()"),

    NULL(" IS NULL"),

    NOT_NULL(" NOT NULL ")

    ;
    String operator;

    public String getOperator() {
        return operator;
    }

    CompareType(String operator) {
        this.operator = operator;
    }

    public static CompareType getByName(String name) {
        for (CompareType type : CompareType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return null;
    }
}