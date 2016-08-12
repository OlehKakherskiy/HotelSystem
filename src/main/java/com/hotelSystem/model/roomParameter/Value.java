package main.java.com.hotelSystem.model.roomParameter;

/**
 * This class represents possible value of {@link Parameter}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class Value {

    /**
     * value's id
     */
    private int id;

    /**
     * value name
     */
    private String value;

    /**
     * constructor initializes all parameters
     *
     * @param id    entity's id
     * @param value entity's name
     */
    public Value(int id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * constuctor without arguments
     */
    public Value() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;

        Value value1 = (Value) o;

        if (id != value1.id) return false;
        return value.equals(value1.value);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + value.hashCode();
        return result;
    }
}
