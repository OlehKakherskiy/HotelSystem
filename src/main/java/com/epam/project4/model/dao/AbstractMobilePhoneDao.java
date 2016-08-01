package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.model.entity.MobilePhone;
import main.java.com.epam.project4.exception.DaoException;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class AbstractMobilePhoneDao extends TransparentGenericDao<MobilePhone, Integer> {

    public abstract List<MobilePhone> getAll(int UserID) throws DaoException;

}
