package main.java.com.epam.project4.model.entity;

import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;

import java.io.Serializable;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class HotelRoom implements Serializable {

    private int roomID;

    private String roomName;

    private boolean activationStatus;

    private List<Reservation> reservationList;

    private List<ParameterValue> parameters;

    private List<Integer> parametersIds;

    private int price;

    public HotelRoom(int roomID, String roomName, boolean activationStatus, List<ParameterValue> parameters, int price) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.activationStatus = activationStatus;
        this.parameters = parameters;
        this.price = price;
    }

    public HotelRoom() {
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
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

    public List<ParameterValue> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterValue> parameters) {
        this.parameters = parameters;
    }

    public List<Integer> getParametersIds() {
        return parametersIds;
    }

    public void setParametersIds(List<Integer> parametersIds) {
        this.parametersIds = parametersIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelRoom)) return false;

        HotelRoom hotelRoom = (HotelRoom) o;

        if (roomID != hotelRoom.roomID) return false;
        if (activationStatus != hotelRoom.activationStatus) return false;
        if (price != hotelRoom.price) return false;
        return roomName.equals(hotelRoom.roomName);

    }

    @Override
    public int hashCode() {
        int result = roomID;
        result = 31 * result + roomName.hashCode();
        result = 31 * result + (activationStatus ? 1 : 0);
        result = 31 * result + price;
        return result;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
