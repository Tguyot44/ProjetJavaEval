package fr.epsi.book.dal;

import fr.epsi.book.domain.Book;
import fr.epsi.book.domain.Contact;

import java.sql.*;
import java.util.*;

public class BookDAO implements IDAO<Book, Long> {

    private static final String INSERT_QUERY = "INSERT INTO `book`(`id`, `code`, `contact`) VALUES ";
    private static final String FIND_BY_ID_QUERY = "SELECT `id`, `code`, `contact` FROM `book` WHERE `id` = ?";
    private static final String FIND_ALL_QUERY = "SELECT id,code,GROUP_CONCAT(contact) FROM `book` GROUP BY id ";
    private static final String UPDATE_QUERY = "";
    private static final String REMOVE_QUERY = "DELETE FROM `book` WHERE `id` = ?";

    @Override
    public void create(Book b) {
        boolean looping = false;
        String content = INSERT_QUERY;
        try (Connection connection = PersistenceManager.getConnection()) {
            for (Map.Entry<String, Contact> entry : b.getContacts().entrySet()) {
                if (looping) content += ",";
                content += "(" + b.getId() + ",'" + b.getCode() + "'," + entry.getValue().getId() + ")";
                looping = true;
            }
            PreparedStatement st = connection.prepareStatement(content, Statement.RETURN_GENERATED_KEYS);
            System.out.println(content);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();

            if (rs.next()) {
                b.setId(rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    @Override
    public Book findById(Long aLong) {
        List<Long> l = new ArrayList<>();
        String id = "";
        String code = "";

        try (Connection connection = PersistenceManager.getConnection()) {

            PreparedStatement st = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, aLong);
            st.executeQuery();
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                id = rs.getString(1);
                code = rs.getString(2);
                l.add(rs.getLong(3));
            }
            return bookMaker(id, code, l);

        } catch (SQLException e) {
            System.err.println(e + " findId err");
            return null;
        }
    }

    @Override
    public List<Book> findAll() {
        ContactDAO cdao = new ContactDAO();
        List<Book> lb = new ArrayList<>();
        List<Book> flb = new ArrayList<>();
        List<List<String>> arrl = new ArrayList<>();

        try (Connection connection = PersistenceManager.getConnection()) {

            PreparedStatement st = connection.prepareStatement(FIND_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
            st.executeQuery();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Book b = new Book();
                b.setId(rs.getString(1));
                b.setCode(rs.getString(2));
                lb.add(b);
                arrl.add(Arrays.asList(rs.getString(3).split("\\s*,\\s*")));
            }
            for (int i = 0 ; i<arrl.size(); i++) {
                List<Long> idl = new ArrayList<>();
                for (String s : arrl.get(i)) {
                    idl.add(Long.parseLong(s));
                }
                flb.add(bookMaker(lb.get(i).getId(),lb.get(i).getCode(),idl));
            }
            return flb;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    @Override
    public Book update(Book o) {
        //TODO
        return null;
    }

    @Override
    public void remove(Book o) {
        try (Connection connection = PersistenceManager.getConnection()) {
            PreparedStatement st = connection.prepareStatement(REMOVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, o.getId());
            st.executeUpdate();
        }catch (SQLException e){
            System.err.println(e);
        }
    }

    private Book bookMaker(String id, String code, List<Long> l) {
        ContactDAO cd = new ContactDAO();
        HashMap<String, Contact> mp = new HashMap<>();
        for (Long cid : l) {
            Contact c = cd.findById(cid);
            mp.put(c.getName(), c);
        }

        if (id != "" && code != "") {
            return new Book(id, code, mp);
        } else {
            return null;
        }
    }
}
