package main.java.com.epam.project4.model.entity.enums;

import java.util.Arrays;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum UserType {

    ADMIN(1),

    REGISTERED_USER(2);

    private int id;

    UserType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UserType fromID(int id) {
        return Arrays.asList(UserType.values()).stream().
                filter(t -> t.getId() == id).findFirst().orElse(REGISTERED_USER);
    }
}
