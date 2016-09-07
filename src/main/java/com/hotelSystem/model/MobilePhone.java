package main.java.com.hotelSystem.model;

import java.io.Serializable;

/**
 * Class represents mobile phone's entity, that can be assosiated with {@link User}.
 * Each one contains id and string representation of mobile phone
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see User
 */
public class MobilePhone implements Serializable, Cloneable {

    /**
     * id
     */
    private int idMobilePhone;

    /**
     * string representation of id
     */
    private String mobilePhone;

    private int userId;

    /**
     * constructor without parameters
     */
    public MobilePhone() {
    }

    /**
     * constructor that inits all fields
     *
     * @param idMobilePhone id
     * @param mobilePhone   string representation of mobile phone
     * @param userId        user's id, who is owning this number
     */
    public MobilePhone(int idMobilePhone, String mobilePhone, int userId) {
        this.idMobilePhone = idMobilePhone;
        this.mobilePhone = mobilePhone;
        this.userId = userId;
    }

    public int getIdMobilePhone() {
        return idMobilePhone;
    }

    public void setIdMobilePhone(int idMobilePhone) {
        this.idMobilePhone = idMobilePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobilePhone that = (MobilePhone) o;

        if (idMobilePhone != that.idMobilePhone) return false;
        if (userId != that.userId) return false;
        return mobilePhone.equals(that.mobilePhone);

    }

    @Override
    public MobilePhone clone() throws CloneNotSupportedException {
        MobilePhone mobilePhone = (MobilePhone) super.clone();
        mobilePhone.setIdMobilePhone(idMobilePhone);
        mobilePhone.setMobilePhone(this.mobilePhone);
        return mobilePhone;
    }

    @Override
    public int hashCode() {
        return getIdMobilePhone();
    }

    @Override
    public String toString() {
        return "MobilePhone{" +
                "idMobilePhone=" + idMobilePhone +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", userId=" + userId +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}