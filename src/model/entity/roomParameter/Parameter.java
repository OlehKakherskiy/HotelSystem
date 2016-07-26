package model.entity.roomParameter;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class Parameter {

    private int id;

    private boolean optional;

    private Value defaultValue;

    private String paramName;

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

        if (id != parameter.id) return false;
        if (optional != parameter.optional) return false;
        if (!defaultValue.equals(parameter.defaultValue)) return false;
        return paramName.equals(parameter.paramName);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (optional ? 1 : 0);
        result = 31 * result + defaultValue.hashCode();
        result = 31 * result + paramName.hashCode();
        return result;
    }
}
