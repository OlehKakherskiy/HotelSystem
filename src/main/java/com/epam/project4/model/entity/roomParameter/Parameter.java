package main.java.com.epam.project4.model.entity.roomParameter;

/**
 * Class represents possible parameters, which can describe {@link main.java.com.epam.project4.model.entity.HotelRoom}
 * and {@linkplain main.java.com.epam.project4.model.entity.Reservation}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class Parameter {

    /**
     * entity's id
     */
    private int id;

    /**
     * determines whether should be defined in each reservation and hotel room's parameters
     */
    private boolean optional;

    /**
     * default value of parameter
     */
    private Value defaultValue;

    /**
     * name of parameter
     */
    private String paramName;

    /**
     * Creates new entity, initializing all parameters
     *
     * @param id           entity's id
     * @param optional     whether can be absent in reservation or hotelRoom entities
     * @param defaultValue default value
     * @param paramName    name
     */
    public Parameter(int id, boolean optional, Value defaultValue, String paramName) {
        this.id = id;
        this.optional = optional;
        this.defaultValue = defaultValue;
        this.paramName = paramName;
    }

    /**
     * constuctor without parameters
     */
    public Parameter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public Value getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Value defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;

        Parameter parameter = (Parameter) o;

        if (optional != parameter.optional) return false;
        if (!defaultValue.equals(parameter.defaultValue)) return false;
        return paramName.equals(parameter.paramName);

    }

    @Override
    public int hashCode() {
        int result = (optional ? 1 : 0);
        result = 31 * result + defaultValue.hashCode();
        result = 31 * result + paramName.hashCode();
        return result;
    }
}
