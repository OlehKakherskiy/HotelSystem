package main.java.com.epam.project4.model.entity;

import main.java.com.epam.project4.model.entity.enums.UserType;

import java.io.Serializable;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class User implements Serializable {

    private int idUser;

    private String name;

    private String lastName;

    private UserType userType;

    private List<MobilePhone> mobilePhoneList;

    public User(int idUser, String name, String lastName, UserType userType, List<MobilePhone> mobilePhoneList) {
        this.idUser = idUser;
        this.name = name;
        this.lastName = lastName;
        this.userType = userType;
        this.mobilePhoneList = mobilePhoneList;
    }

    public User() {
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<MobilePhone> getMobilePhoneList() {
        return mobilePhoneList;
    }

    public void setMobilePhoneList(List<MobilePhone> mobilePhoneList) {
        this.mobilePhoneList = mobilePhoneList;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (idUser != user.idUser) return false;
        if (!name.equals(user.name)) return false;
        if (!lastName.equals(user.lastName)) return false;
        if (userType != user.userType) return false;
        return mobilePhoneList.equals(user.mobilePhoneList);

    }

    @Override
    public int hashCode() {
        return idUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userType=" + userType +
                ", mobilePhoneList=" + mobilePhoneList +
                '}';
    }
}
