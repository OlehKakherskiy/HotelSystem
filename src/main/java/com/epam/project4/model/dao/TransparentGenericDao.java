package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.exception.DaoException;

import java.text.MessageFormat;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class TransparentGenericDao<T, K> implements GenericDao<T, K> {

    private static final String unsupportedOperationExceptionBase = "{0} operation is not supported by {1} type of dao";

    private static final String readOperation = "Read";

    private static final String updateOperation = "Update";

    private static final String saveOperation = "Save";

    private static final String deleteOperation = "Delete";

    @Override
    public T read(K id) throws DaoException {
        throw new DaoException(MessageFormat.format(unsupportedOperationExceptionBase, readOperation, this.getClass().getName()));
    }

    @Override
    public K save(T object) throws DaoException {
        throw new DaoException(MessageFormat.format(unsupportedOperationExceptionBase, saveOperation, this.getClass().getName()));
    }

    @Override
    public boolean delete(K id) throws DaoException {
        throw new DaoException(MessageFormat.format(unsupportedOperationExceptionBase, deleteOperation, this.getClass().getName()));
    }

    @Override
    public boolean update(T object) throws DaoException {
        throw new DaoException(MessageFormat.format(unsupportedOperationExceptionBase, updateOperation, this.getClass().getName()));
    }
}
