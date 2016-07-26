package model.entity.roomParameter;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ParameterValue {

    private int id;

    private Parameter parameter;

    private Value value;

    private int price;

    public ParameterValue(int id, Parameter parameter, Value value, int price) {
        this.id = id;
        this.parameter = parameter;
        this.value = value;
        this.price = price;
    }

    public ParameterValue() {
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
        if (!(o instanceof ParameterValue)) return false;

        ParameterValue that = (ParameterValue) o;

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
