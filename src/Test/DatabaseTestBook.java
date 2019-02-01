package Test;

import fr.epsi.book.dal.BookDAO;
import fr.epsi.book.dal.ContactDAO;
import fr.epsi.book.domain.Book;
import fr.epsi.book.domain.Contact;

import java.util.HashMap;

public class DatabaseTestBook {

    public static void main(String[] args) {
        BookDAO bd = new BookDAO();
        ContactDAO cd = new ContactDAO();

        Contact c1 = new Contact(
                "2",
                "test2",
                "mail",
                "1234567",
                Contact.Type.PERSO
        );
        Contact c2 = new Contact(
                "3",
                "test3",
                "mail",
                "1234567",
                Contact.Type.PERSO
        );
        Contact c3 = new Contact(
                "1",
                "test1",
                "mail",
                "1234567",
                Contact.Type.PERSO);

        cd.create(c1);
        cd.create(c2);
        cd.create(c3);

        HashMap<String, Contact> hb1 = new HashMap<>();
        hb1.put(c1.getName(), c1);
        Book b1 = new Book(
                "1",
                "testString",
                hb1
        );

        HashMap<String, Contact> hb2 = new HashMap<>();
        hb2.put(c2.getName(), c2);
        hb2.put(c3.getName(), c3);
        Book b2 = new Book(
                "2",
                "testString",
                hb2
        );


        bd.create(b1);
        bd.create(b2);

        System.out.println(bd.findById(1l).toString());
        System.out.println(bd.findById(2l).toString());

        System.out.println(bd.findAll());

        cd.remove(c1);
        cd.remove(c2);
        cd.remove(c3);

        bd.remove(b1);
        bd.remove(b2);
    }

}
