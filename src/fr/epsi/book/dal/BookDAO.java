package fr.epsi.book.dal;

import fr.epsi.book.domain.Book;
import fr.epsi.book.domain.Contact;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDAO implements IDAO<Book, Long> {

    private static final String INSERT_QUERY = "INSERT INTO `book`(`id`, `code`, `contact`) VALUES ";
    private static final String FIND_BY_ID_QUERY = "SELECT `id`, `code`, `contact` FROM `book` WHERE `id` = ?";
    private static final String FIND_ALL_QUERY = "";
    private static final String UPDATE_QUERY = "";
    private static final String REMOVE_QUERY = "";

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
        HashMap<String,Contact> mp = new HashMap<>();
        String id ="";
        String  code="";
        try (Connection connection = PersistenceManager.getConnection()) {

            PreparedStatement st = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, aLong);
            st.executeQuery();
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                id = rs.getString(1);
                code = rs.getString(2);
                Long l = rs.getLong(3);
                ContactDAO cd = new ContactDAO();
                Contact c = cd.findById(l);
                mp.put(c.getName(),c);
            }
            rs.previous();
            if (id != "" && code != ""){
            return new Book(id,code,mp);
            }else{
                return null;
            }


        } catch (SQLException e) {
            System.err.println(e + " findId err");
            return null;
        }
    }


    @Override
    public List<Book> findAll() {
        //TODO
        return null;
    }

    @Override
    public Book update(Book o) {
        //TODO
        return null;
    }

    @Override
    public void remove(Book o) {
        //TODO
    }

    private HashMap<String, Contact> to_map() {
        return null;
    }
}
