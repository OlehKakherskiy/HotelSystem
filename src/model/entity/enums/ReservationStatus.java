package model.entity.enums;

import java.util.Arrays;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum ReservationStatus {

    PROCESSING(1),

    CANCELLED(2),

    ANSWERED(3),

    REFUSED(4),

    SUBMITTED(5),

    ALL(-1);

    private int Id;

    ReservationStatus(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public static ReservationStatus fromId(int id) {
        return Arrays.asList(ReservationStatus.values()).stream().filter(status -> status.getId() == id).findFirst().get();
    }
}
