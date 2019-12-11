package MyPackage.Services;

import MyPackage.Models.Record;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecordService {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Record get(Integer id) {
        Session session = sessionFactory.getCurrentSession();

        Record record = session.get(Record.class, id);

        return record;
    }

}
