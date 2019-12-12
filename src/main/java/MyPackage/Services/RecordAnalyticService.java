package MyPackage.Services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RecordAnalyticService {

    @Autowired
    private SessionFactory sessionFactory;

    public String androidQuery(int record_id, String from, String to) {
        return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUidIds(record_id) + ") AND " +
                "device_id IN (SELECT id FROM devices WHERE " +
                "name='Samsung phone' OR name='Sony phone' OR name='Asus phone' OR name='Xiomi phone' OR " +
                "name='Samsung tablet' OR name='Sony tablet' or name='Asus tablet' or " +
                "name = 'Xiomi tablet')" + getBetween(from, to);
    }

    public String iosQuery(int record_id, String from, String to) {
        return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUidIds(record_id) + ") AND device_id IN (SELECT id FROM devices WHERE name='Iphone' OR name='Ipad')" + getBetween(from, to);
    }

    public String unknownQuery(int record_id, String from, String to) {
        return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUidIds(record_id) + ") AND device_id IN " +
                " (SELECT id FROM devices WHERE name='Another phone' OR name='Another tablet' OR name = 'unknown')" + getBetween(from, to);
    }

    public String commonQuery(int record_id, String from, String to) {
        return "SELECT COUNT(*) as commonCount, " +
                "(" + androidQuery(record_id, from, to) + ") as androidCount, " +
                "(" + iosQuery(record_id, from, to) + ") as iosCount," +
                "(" + unknownQuery(record_id, from, to) + ") as unknownCount " +
                "FROM correct_requests WHERE uid_id IN (" + getUidIds(record_id) + ")" + getBetween(from, to);
    }

    public String getUidIds(int record_id) {
        return "Select id FROM uids WHERE record_id=" + record_id;
    }

    private String getBetween(String from, String to) {
        if (from == null)
            return "";

        return "AND created_at BETWEEN  STR_TO_DATE( '" + from + " 00:00:00' , '%Y-%m-%d %H:%i:%s') AND STR_TO_DATE( '" + to + " 23:59:59',   '%Y-%m-%d %H:%i:%s')";
    }

    public List<Map<String, Object>> all(Integer record_id, String from, String to) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery nQuery = session.createSQLQuery(commonQuery(record_id, from, to));
        nQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return nQuery.list();
    }

    public List<Map<String, Object>> get(Integer id, String from, String to) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery query = session.createSQLQuery("SELECT uids.value as 'uid_value', \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE correct_requests.uid_id=uids.id " + getBetween(from, to) + ") as request_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Samsung phone' OR name='Sony phone' OR name='Asus phone' OR name='Xiomi phone' OR name='Samsung tablet' OR name='Sony tablet' or name='Asus tablet' or name = 'Xiomi tablet')) " + getBetween(from, to) + ") as android_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Iphone' OR name='Ipad')) " + getBetween(from, to) + ") as ios_count, \n" +
                "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Another phone' OR name='Another tablet' OR name = 'unknown')) " + getBetween(from, to) + " ) as unknown_count \n" +
                "FROM records r INNER JOIN uids ON r.id=uids.record_id WHERE r.id=" + id);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.list();
    }

}
