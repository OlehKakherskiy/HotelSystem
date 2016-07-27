package main.java.com.epam.project4.model.entity.enums;

import java.util.Arrays;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum ReservationStatus {

    PROCESSING(1),

    ANSWERED(2),

    REFUSED(3),

    SUBMITTED(4),

    ALL(-1);

    private int Id;

    ReservationStatus(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public static ReservationStatus fromId(int id) {
        return Arrays.asList(ReservationStatus.values()).stream().filter(status -> status.getId() == id).findFirst().orElse(ALL);//TODO: если нету id, то возвращаем все
    }
}
