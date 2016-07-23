package model.entity;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class MobilePhone {

    private int idMobilePhone;

    private String mobilePhone;

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
}
