package main.java.com.epam.project4.model.entity.enums;

import java.util.Arrays;

/**
 * Enum represents types of user in hotel system
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum UserType {

    /**
     * Hotel System's manager
     */
    ADMIN(1),

    /**
     * Hotel System's registered user
     */
    REGISTERED_USER(2);

    /**
     * id in persistent storage
     */
    private int id;

    UserType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * returns {@link UserType} from it's id. If there's no user type
     * with target id - returns null.
     *
     * @param id id of target user type.
     * @return reservation status, that has target id or null
     */
    public static UserType fromID(int id) {
        return Arrays.asList(UserType.values()).stream().
                filter(t -> t.getId() == id).findFirst().orElse(REGISTERED_USER);
    }
}
