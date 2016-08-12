package main.java.com.hotelSystem.model.enums;

import java.util.Arrays;

/**
 * Enum represents status of reservation
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum ReservationStatus {

    /**
     * Reservation is being processed by {@link UserType#ADMIN}
     */
    PROCESSING(1, "processing"),

    /**
     * Reservation has offered room and waiting for answer from {@link UserType#REGISTERED_USER}
     */
    WAITING_FOR_ANSWER(2, "waitingForAnswer"),

    /**
     * Reservation has been refused from processing by {@link UserType#ADMIN}, so it won't service it
     * until he won't change the status
     */
    REFUSED_FROM_PROCESSING(3, "refused"),

    /**
     * Hotel room's offer for reservation was submitted by user
     */
    SUBMITTED(4, "submitted"),

    ALL(-1, "all");

    /**
     * reservation's id in persistent storage
     */
    private int id;

    /**
     * reservation's name in persistent storage
     */
    private String name;

    /**
     * inits all fields
     *
     * @param id   id in persistent storage
     * @param name name in persistent storage
     */
    ReservationStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * returns {@link ReservationStatus} from it's id. If there's no reservation status
     * with target id - returns null.
     *
     * @param id id of target reservation status.
     * @return reservation status, that has target id or null
     */
    public static ReservationStatus fromId(int id) {
        return Arrays.asList(ReservationStatus.values()).stream().filter(status -> status.getId() == id).findFirst().orElse(ALL);
    }
}
