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

    private boolean isActiveStatus;

    private List<Reservation> reservationList;

    private List<ParameterValue> parameters;

    private List<Integer> parametersIds;

    public HotelRoom(int roomID, String roomName, boolean isActiveStatus, List<ParameterValue> parameters) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.isActiveStatus = isActiveStatus;
        this.parameters = parameters;
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

    public boolean isActiveStatus() {
        return isActiveStatus;
    }

    public void setIsActiveStatus(boolean isActiveStatus) {
        this.isActiveStatus = isActiveStatus;
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
        if (isActiveStatus != hotelRoom.isActiveStatus) return false;
        return roomName.equals(hotelRoom.roomName);

    }

    @Override
    public int hashCode() {
        int result = roomID;
        result = 31 * result + roomName.hashCode();
        result = 31 * result + (isActiveStatus ? 1 : 0);
        return result;
    }
}
