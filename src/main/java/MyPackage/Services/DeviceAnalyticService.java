package MyPackage.Services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DeviceAnalyticService {

    @Autowired
    private SessionFactory sessionFactory;

    @Resource(name = "getAndroids")
    private List<String> androids;

    @Resource(name = "getIos")
    private List<String> ios;

    public Object getRating(Integer record_id, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        String betweenClause = "";
        if (startDate != null && endDate != null) {
            betweenClause = " and request.created_at between :startDate and :endDate ";
        }

        Query query = session.createQuery("select  new map(" +
                "count(uid) as onlyAndroidCount, " +
                "(select count(uid) from Uid uid where uid.record_id=:record_id " +
                "and exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:ios) " + betweenClause + " ) " +
                "and not exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:androids) " + betweenClause + " )) " +
                "as onlyIosCount, " +
                "(select count(uid) from Uid uid where uid.record_id=:record_id " +
                "and exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:androids) " + betweenClause + " ) " +
                "and exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:ios) " + betweenClause + " )) " +
                "as androidAndIosCount) " +
                "from Uid uid " +
                "where uid.record_id=:record_id " +
                "and exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:androids) " + betweenClause + " )" +
                "and not exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:ios) " + betweenClause + " )");


        query.setParameterList("androids", androids);
        query.setParameterList("ios", ios);
        query.setParameter("record_id", record_id);

        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(startDate + " 00:00:00");
            Date end = format.parse(endDate + " 23:59:59");

            query.setTimestamp("startDate", start);
            query.setTimestamp("endDate", end);
        }

        return query.getSingleResult();
    }

    public Object all(Integer record_id, String from, String to) {
        Session session = sessionFactory.getCurrentSession();

        SqlQuery sqlQuery = new SqlQuery(record_id, from, to);

        NativeQuery nQuery = session.createSQLQuery(sqlQuery.getAllAnalyticQuery());
        nQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return nQuery.getSingleResult();
    }

    public List get(Integer record_id, String from, String to) {
        Session session = sessionFactory.getCurrentSession();

        SqlQuery sqlQuery = new SqlQuery(record_id, from, to);

        NativeQuery query = session.createSQLQuery(sqlQuery.getUidAnalyticQuery());
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.list();
    }

    class SqlQuery {

        private int record_id;
        private String from;
        private String to;

        public SqlQuery(Integer record_id, String from, String to) {
            this.record_id = record_id;
            this.from = from;
            this.to = to;
        }

        public String getAndroid() {
            return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUid_idSet() + ") AND " +
                    "device_id IN (SELECT id FROM devices WHERE " +
                    "name='Samsung phone' OR name='Sony phone' OR name='Asus phone' OR name='Xiomi phone' OR " +
                    "name='Samsung tablet' OR name='Sony tablet' or name='Asus tablet' or " +
                    "name = 'Xiomi tablet')" + getDateRange();
        }

        public String getIos() {
            return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUid_idSet() + ") AND device_id IN (SELECT id FROM devices WHERE name='Iphone' OR name='Ipad')" + getDateRange();
        }

        public String getUnknown() {
            return "SELECT COUNT(*) FROM correct_requests WHERE uid_id IN (" + getUid_idSet() + ") AND device_id IN " +
                    " (SELECT id FROM devices WHERE name='Another phone' OR name='Another tablet' OR name = 'unknown')" + getDateRange();
        }

        public String getUid_idSet() {
            return "Select id FROM uids WHERE record_id=" + record_id;
        }

        private String getDateRange() {
            if (from == null)
                return "";

            return "AND created_at BETWEEN  STR_TO_DATE( '" + from + " 00:00:00' , '%Y-%m-%d %H:%i:%s') AND STR_TO_DATE( '" + to + " 23:59:59',   '%Y-%m-%d %H:%i:%s')";
        }

        public String getAllAnalyticQuery() {
            return "SELECT COUNT(*) as commonCount, " +
                    "(" + getAndroid() + ") as androidCount, " +
                    "(" + getIos() + ") as iosCount," +
                    "(" + getUnknown() + ") as unknownCount " +
                    "FROM correct_requests WHERE uid_id IN (" + getUid_idSet() + ")" + getDateRange();
        }

        public String getUidAnalyticQuery() {
            return "SELECT uids.id as 'uid_id', uids.value as 'uid_value', \n" +
                    "(SELECT COUNT(*) FROM correct_requests WHERE correct_requests.uid_id=uids.id " + getDateRange() + ") as request_count, \n" +
                    "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Samsung phone' OR name='Sony phone' OR name='Asus phone' OR name='Xiomi phone' OR name='Samsung tablet' OR name='Sony tablet' or name='Asus tablet' or name = 'Xiomi tablet')) " + getDateRange() + ") as android_count, \n" +
                    "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Iphone' OR name='Ipad')) " + getDateRange() + ") as ios_count, \n" +
                    "(SELECT COUNT(*) FROM correct_requests WHERE uid_id=uids.id AND device_id IN (SELECT id FROM devices WHERE (name='Another phone' OR name='Another tablet' OR name = 'unknown')) " + getDateRange() + " ) as unknown_count \n" +
                    "FROM records r INNER JOIN uids ON r.id=uids.record_id WHERE r.id=" + record_id;
        }

    }

}
