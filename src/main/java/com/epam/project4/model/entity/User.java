package main.java.com.epam.project4.model.entity;

import main.java.com.epam.project4.model.entity.enums.UserType;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Class represents User entity in Hotel System. Each user has name, surname, assosiated mobile phones,
 * and user type. As user has user type - it has some access rights in system. So that, for example,
 * one user's type has access to creating new reservation, others are not.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see UserType
 * @see MobilePhone
 */
public class User implements Serializable {

    /**
     * user's id
     */
    private int idUser;

    /**
     * user's name
     */
    private String name;

    /**
     * user's surname
     */
    private String lastName;

    /**
     * user's type
     */
    private UserType userType;

    /**
     * assosiated mobile phones
     */
    private List<MobilePhone> mobilePhoneList;

    /**
     * consturctor, that inits all fields
     *
     * @param idUser          user's id
     * @param name            user's name
     * @param lastName        user's surname
     * @param userType        user's type
     * @param mobilePhoneList assosiated mobile phones
     */
    public User(int idUser, String name, String lastName, UserType userType, List<MobilePhone> mobilePhoneList) {
        this.idUser = idUser;
        this.name = name;
        this.lastName = lastName;
        this.userType = userType;
        this.mobilePhoneList = (mobilePhoneList == null) ? Collections.emptyList() : mobilePhoneList;
    }

    /**
     * constructor without parameters
     */
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
