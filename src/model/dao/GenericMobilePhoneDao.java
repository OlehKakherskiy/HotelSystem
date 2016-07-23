package model.dao;

import model.dao.TransparentGenericDao;
import model.entity.MobilePhone;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericMobilePhoneDao extends TransparentGenericDao<MobilePhone, Integer> {

    public abstract List<MobilePhone> getAll(int UserID);

}
