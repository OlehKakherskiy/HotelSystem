package main.java.com.hotelSystem.model;

/**
 * Class represents mobile phone's entity, that can be assosiated with {@link User}.
 * Each one contains id and string representation of mobile phone
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see User
 */
public class MobilePhone {

    /**
     * id
     */
    private int idMobilePhone;

    /**
     * string representation of id
     */
    private String mobilePhone;

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
     */
    public MobilePhone(int idMobilePhone, String mobilePhone) {
        this.idMobilePhone = idMobilePhone;
        this.mobilePhone = mobilePhone;
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

        return getIdMobilePhone() == that.getIdMobilePhone();

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
                '}';
    }
}