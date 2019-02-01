package fr.epsi.book.dal;

import fr.epsi.book.domain.Contact;
import fr.epsi.book.domain.Contact.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO implements IDAO<Contact, Long> {

    private static final String INSERT_QUERY = "INSERT INTO contact (id, name, email, phone, type_var, type_num) values (?,?,?,?,?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM `contact` WHERE `id` = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM `contact`";
    private static final String UPDATE_QUERY = "UPDATE `contact` SET `ID`=?,`name`=?,`email`=?,`phone`=?,`type_var`=?,`type_num`=? WHERE `ID`= ?";
    private static final String REMOVE_QUERY = "DELETE FROM `contact` WHERE `ID`=?";

    @Override
    public void create(Contact c) {

        try (Connection connection = PersistenceManager.getConnection()) {
            PreparedStatement st = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, c.getId());
            st.setString(2, c.getName());
            st.setString(3, c.getEmail());
            st.setString(4, c.getPhone());
            st.setString(5, c.getType().getValue());
            st.setInt(6, c.getType().ordinal());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();

            if (rs.next()) {
                c.setId(rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println(e+" create err");;
        }
    }

    @Override
    public Contact findById(Long aLong) {
        try (Connection connection = PersistenceManager.getConnection()) {

            PreparedStatement st = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, aLong);
            st.executeQuery();
            ResultSet rs = st.executeQuery();
            rs.next();
            return toContact(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(6)
            );

        } catch (SQLException e) {
            System.err.println(e+" findId err");
            return null;
        }
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> lc = new ArrayList<>();
        try (Connection connection = PersistenceManager.getConnection()) {
            PreparedStatement st = connection.prepareStatement(FIND_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
            st.executeQuery();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lc.add(toContact(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(6))
                );
            }
            return lc;
        } catch (SQLException e) {
            System.err.println(e+" findAll err");;
            return null;
        }
    }

    @Override
    public Contact update(Contact c) {
        try (Connection connection = PersistenceManager.getConnection()) {
            PreparedStatement st = connection.prepareStatement(UPDATE_QUERY, Statement.NO_GENERATED_KEYS);
            st.setString(1, c.getId());
            st.setString(2, c.getName());
            st.setString(3, c.getEmail());
            st.setString(4, c.getPhone());
            st.setString(5, c.getType().toString());
            st.setInt(6, c.getType().ordinal());
            st.setString(7, c.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e+" update err");;
        }
        return null;
    }

    @Override
    public void remove(Contact c) {
        try (Connection connection = PersistenceManager.getConnection()) {
            PreparedStatement st = connection.prepareStatement(REMOVE_QUERY, Statement.NO_GENERATED_KEYS);
            st.setString(1, c.getId());
            st.executeUpdate();
        }catch (SQLException e){
            System.err.println(e+" remove err");
        }
    }

    private Contact toContact(String id, String name, String email, String phone, String type_num) {
        Type t;
        if (Integer.parseInt(type_num) == Type.PRO.ordinal()) {
            t = Type.PRO;
        } else if (Integer.parseInt(type_num) == Type.PERSO.ordinal()) {
            t = Type.PERSO;
        } else {
            return null;
        }
        return new Contact(id, name, email, phone, t);
    }
}
