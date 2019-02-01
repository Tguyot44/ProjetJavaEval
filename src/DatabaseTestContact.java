import fr.epsi.book.dal.ContactDAO;
import fr.epsi.book.domain.Contact;

import java.util.List;

public class DatabaseTestContact {

    public static void main(String[] args) {
        ContactDAO cd = new ContactDAO();
        Contact c = new Contact(
                "2",
                "test",
                "mail",
                "1234567",
                Contact.Type.PERSO
        );
        cd.create(c);
        System.out.println(cd.findById(2l));
        c.setName("bob");
        cd.update(c);
        System.out.println(cd.findAll());
        cd.remove(c);
        System.out.println(cd.findAll());
    }

}
