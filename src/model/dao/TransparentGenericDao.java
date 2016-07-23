package model.dao;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class TransparentGenericDao<T, K> implements GenericDao<T, K> {

    @Override
    public List<T> getAll() {
        unsupportedException();
        return null;
    }

    private void unsupportedException() {
        throw new UnsupportedOperationException(); //TODO: текст ошибки
    }
}
