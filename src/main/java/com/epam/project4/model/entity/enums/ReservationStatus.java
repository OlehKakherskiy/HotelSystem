package main.java.com.epam.project4.model.entity.enums;

import java.util.Arrays;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum ReservationStatus {

    PROCESSING(1,"processing"),

    WAITING_FOR_ANSWER(2,"waitingForAnswer"),

    REFUSED(3,"refused"),

    SUBMITTED(4,"submitted"),

    ALL(-1,"all");

    private int id;

    private String name;

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

    public static ReservationStatus fromId(int id) {
        return Arrays.asList(ReservationStatus.values()).stream().filter(status -> status.getId() == id).findFirst().orElse(ALL);
    }
}
