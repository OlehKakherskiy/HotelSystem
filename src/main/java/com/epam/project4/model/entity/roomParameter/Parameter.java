package main.java.com.epam.project4.model.entity.roomParameter;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class Parameter {

    private int id;

    private boolean optional;

    private Value defaultValue;

    private String paramName;

    public Parameter(int id, boolean optional, Value defaultValue, String paramName) {
        this.id = id;
        this.optional = optional;
        this.defaultValue = defaultValue;
        this.paramName = paramName;
    }

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

        return id == parameter.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
