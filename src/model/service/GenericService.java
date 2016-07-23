package model.service;

import model.dao.GenericDao;

import java.io.Serializable;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericService<T extends GenericDao, E extends Serializable, K extends Serializable> {

    protected T dao;

    public GenericService(T dao) {
        this.dao = dao;
    }
}
