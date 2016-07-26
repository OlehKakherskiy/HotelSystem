package model.entity.enums;

import java.util.Arrays;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum UserType {

    ADMIN(1),

    REGISTERED_USER(2);

    private int ID;

    UserType(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }


    public static UserType fromID(int ID) {
        return Arrays.asList(UserType.values()).stream().
                filter(t -> t.getID() == ID).findFirst().orElse(REGISTERED_USER);
    }
}
