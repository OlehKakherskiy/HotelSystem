package main.java.com.hotelSystem.model.roomParameter;

import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.Reservation;

/**
 * Class represents mapping between set of {@link Parameter} and {@link Value}. Therefore,
 * this class is a dynamic implementation of enum language entity. As parameters, using which
 * {@link Reservation Reservation} and {@link HotelRoom HotelRoom}
 * objects can be identified, can change dynamically, there's no possibility to use simple enum constants and
 * it's difficult to maintain scalability in persistent storage (for example, table's structure in
 * relational databases can't be easily modified), but this solution deal with problem of dynamic modification
 * parameters of request and room in system.
 * <p>
 * This class represents many-to-many relationship between {@link Parameter} and {@link Value}.
 * </p>
 * <p>
 * Each tuple has it's own price. As each room {@link}
 * </p>
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ParameterValueTuple {

    /**
     * id of tuple
     */
    private int id;

    /**
     * key part of tuple
     */
    private Parameter parameter;

    /**
     * value part of tuple
     */
    private Value value;

    /**
     * price of tuple
     */
    private int price;

    /**
     * Creates new tuple, initializing all fields
     *
     * @param id        tuple's id
     * @param parameter key
     * @param value     value
     * @param price     tuple's price
     */
    public ParameterValueTuple(int id, Parameter parameter, Value value, int price) {
        this.id = id;
        this.parameter = parameter;
        this.value = value;
        this.price = price;
    }

    /**
     * Creates new tuple without initializing all fields
     */
    public ParameterValueTuple() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterValueTuple)) return false;

        ParameterValueTuple that = (ParameterValueTuple) o;

        if (id != that.id) return false;
        if (price != that.price) return false;
        if (!parameter.equals(that.parameter)) return false;
        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + parameter.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + price;
        return result;
    }
}
