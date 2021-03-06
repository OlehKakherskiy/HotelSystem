package main.java.com.hotelSystem.model;

import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;

import java.io.Serializable;
import java.util.List;

/**
 * Class represents hotel room entity. It has id, name, activation status (active - can take part in business processes,
 * otherwise - cannot), can have reservations, assosiated with it and parameters. As each room has a list
 * o parameters with defined values {@link ParameterValueTuple}, and each tuple has it's own
 * {@link ParameterValueTuple#price price}, basic room's price can be calculated as sum of all tuple from
 * {@link #parameters}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see Reservation
 * @see ParameterValueTuple
 */
public class HotelRoom implements Serializable {

    /**
     * room's id
     */
    private int roomId;

    /**
     * room's name
     */
    private String roomName;

    /**
     * room's activation status (true - active, false - inactive)
     */
    private boolean activationStatus;

    /**
     * assosiated {@link Reservation}
     */
    private List<Reservation> reservationList;

    /**
     * assosiated {@link ParameterValueTuple}
     */
    private List<ParameterValueTuple> parameters;

    /**
     * list of {@link ParameterValueTuple#id}
     */
    private transient List<Integer> parameterListIds;

    /**
     * room's price
     */
    private int price;

    /**
     * Constructor, that inits some parameters
     *
     * @param roomId           room's id
     * @param roomName         room's name
     * @param activationStatus room's activation status (true - active, false - inactive)
     * @param parameters       assosiated ids of parameterValues
     * @param price            room's price
     */
    public HotelRoom(int roomId, String roomName, boolean activationStatus, List<Integer> parameters, int price) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.activationStatus = activationStatus;
        this.parameterListIds = parameters;
        this.price = price;
    }

    /**
     * constructor without params
     */
    public HotelRoom() {
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(boolean isActiveStatus) {
        this.activationStatus = isActiveStatus;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public List<ParameterValueTuple> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterValueTuple> parameters) {
        this.parameters = parameters;
    }

    public List<Integer> getParameterListIds() {
        return parameterListIds;
    }

    public void setParameterListIds(List<Integer> parameterListIds) {
        this.parameterListIds = parameterListIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelRoom)) return false;

        HotelRoom hotelRoom = (HotelRoom) o;

        if (activationStatus != hotelRoom.activationStatus) return false;
        if (price != hotelRoom.price) return false;
        if (!roomName.equals(hotelRoom.roomName)) return false;
        return parameterListIds.equals(hotelRoom.parameterListIds);

    }

    @Override
    public int hashCode() {
        int result = roomName.hashCode();
        result = 31 * result + (activationStatus ? 1 : 0);
        result = 31 * result + parameterListIds.hashCode();
        result = 31 * result + price;
        return result;
    }

    @Override
    public String toString() {
        return "HotelRoom{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", activationStatus=" + activationStatus +
                ", price=" + price +
                ", parameterListIds=" + parameterListIds +
                '}';
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
