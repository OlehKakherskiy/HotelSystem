package main.java.com.epam.project4.model.dao;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class TransparentGenericDao<T, K> implements GenericDao<T, K> {

    @Override
    public T read(K id) {
        unsupportedException();
        return null;
    }

    @Override
    public K save(T object) {
        unsupportedException();
        return null;
    }

    @Override
    public boolean delete(K id) {
        unsupportedException();
        return false;
    }

    @Override
    public boolean update(T object) {
        unsupportedException();
        return false;
    }

    private void unsupportedException() {
        throw new UnsupportedOperationException(); //TODO: текст ошибки
    }
}
