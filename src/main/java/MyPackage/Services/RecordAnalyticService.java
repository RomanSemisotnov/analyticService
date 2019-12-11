package MyPackage.Services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecordAnalyticService {

    @Autowired
    private SessionFactory sessionFactory;

    public String androidQuery(int record_id) {
        return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUidIds(record_id) + ") AND " +
                "device_id IN (SELECT id FROM devices WHERE " +
                "name='Samsung phone' OR name='Sony phone' OR name='Asus phone' OR name='Xiomi phone' OR " +
                "name='Samsung tablet' OR name='Sony tablet' or name='Asus tablet' or " +
                "name = 'Xiomi tablet')";
    }

    public String iosQuery(int record_id) {
        return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUidIds(record_id) + ") AND device_id IN (SELECT id FROM devices WHERE name='Iphone' OR name='Ipad')";
    }

    public String unknownQuery(int record_id) {
        return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUidIds(record_id) + ") AND device_id IN " +
                " (SELECT id FROM devices WHERE name='Another phone' OR name='Another tablet' OR name = 'unknown')";
    }

    public String commonQuery(int record_id) {
        return "SELECT COUNT(*) as commonCount, " +
                "(" + androidQuery(record_id) + ") as androidCount, " +
                "(" + iosQuery(record_id) + ") as iosCount," +
                "(" + unknownQuery(record_id) + ") as unknownCount " +
                "FROM correct_requests WHERE uid_id IN (" + getUidIds(record_id) + ")";
    }

    public String getUidIds(int record_id) {
        return "Select id FROM Uids WHERE record_id=" + record_id;
    }

    public String all(Integer record_id) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery nQuery = session.createSQLQuery(commonQuery(record_id));
        nQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return commonQuery(record_id);
    }

    public String get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery query = session.createSQLQuery("SELECT uids.value as 'uid_value', \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE correct_requests.uid_id=uids.id) as request_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Samsung phone' OR name='Sony phone' OR name='Asus phone' OR name='Xiomi phone' OR name='Samsung tablet' OR name='Sony tablet' or name='Asus tablet' or name = 'Xiomi tablet'))) as android_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Iphone' OR name='Ipad'))) as ios_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Another phone' OR name='Another tablet' OR name = 'unknown'))) as unknown_count \n" +
                "FROM records r INNER JOIN uids ON r.id=uids.record_id WHERE r.id=" + id);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return "SELECT uids.value as 'uid_value', \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE correct_requests.uid_id=uids.id) as request_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Samsung phone' OR name='Sony phone' OR name='Asus phone' OR name='Xiomi phone' OR name='Samsung tablet' OR name='Sony tablet' or name='Asus tablet' or name = 'Xiomi tablet'))) as android_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Iphone' OR name='Ipad'))) as ios_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Another phone' OR name='Another tablet' OR name = 'unknown'))) as unknown_count \n" +
                "FROM records r INNER JOIN uids ON r.id=uids.record_id WHERE r.id=" + id;
    }

}
