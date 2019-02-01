import fr.epsi.book.dal.BookDAO;
import fr.epsi.book.dal.ContactDAO;
import fr.epsi.book.domain.Book;
import fr.epsi.book.domain.Contact;

import java.util.HashMap;

public class DatabaseTestBook {

    public static void main(String[] args) {
        BookDAO bd = new BookDAO();
        Contact c = new Contact(
                "2",
                "test",
                "mail",
                "1234567",
                Contact.Type.PERSO
        );
        HashMap<String, Contact> hb = new HashMap<>();
        hb.put("mapname", c);
        Book b = new Book(
                "2",
                "testString",
                hb
        );
        System.out.println(bd.findById(2l));
    }

}
