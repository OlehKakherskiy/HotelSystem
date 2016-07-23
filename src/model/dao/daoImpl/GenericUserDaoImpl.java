package model.dao.daoImpl;

import model.dao.GenericUserDao;
import model.entity.User;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GenericUserDaoImpl extends GenericUserDao {

    @Override
    public int tryLogin(String login, String password) {
        return 0;
    }

    @Override
    public User read(Integer id) {
        return null;
    }

    @Override
    public User save(User object) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean update(User object) {
        return false;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
