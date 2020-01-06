package MyPackage.Services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeviceAnalyticService {

    @Autowired
    private SessionFactory sessionFactory;

    @Resource(name = "getAndroids")
    private List<String> androids;

    @Resource(name = "getIos")
    private List<String> ios;

    @Resource(name = "getUnknown")
    private List<String> unknown;

    public Map<String, Long> getRating(int record_id, String startDate, String endDate) throws ParseException {
        List<Integer> list = new ArrayList<>();
        list.add(record_id);
        return getRating(list, startDate, endDate);
    }

    public Map<String, Long> all(int record_id, String startDate, String endDate) throws ParseException {
        List<Integer> list = new ArrayList<>();
        list.add(record_id);
        return all(list, startDate, endDate);
    }

    public List<Map<String, Object>> get(int record_id, String startDate, String endDate) throws ParseException {
        List<Integer> list = new ArrayList<>();
        list.add(record_id);
        return get(list, startDate, endDate);
    }

    public Map<String, Long> getRating(List<Integer> record_ids, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select new map(" +
                "count(uid) as onlyAndroidCount, " +
                "(select count(uid) from Uid uid where uid.record_id in (:record_ids) " +
                "and exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:ios) " + getBetweenClause(startDate, endDate) + " ) " +
                "and not exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:androids) " + getBetweenClause(startDate, endDate) + " )) " +
                "as onlyIosCount, " +
                "(select count(uid) from Uid uid where uid.record_id in (:record_ids) " +
                "and exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:androids) " + getBetweenClause(startDate, endDate) + " ) " +
                "and exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:ios) " + getBetweenClause(startDate, endDate) + " )) " +
                "as androidAndIosCount) " +
                "from Uid uid " +
                "where uid.record_id in (:record_ids) " +
                "and exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:androids) " + getBetweenClause(startDate, endDate) + " )" +
                "and not exists (from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:ios) " + getBetweenClause(startDate, endDate) + " )");


        query.setParameterList("androids", androids);
        query.setParameterList("ios", ios);
        query.setParameterList("record_ids", record_ids);

        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(startDate + " 00:00:00");
            Date end = format.parse(endDate + " 23:59:59");

            query.setTimestamp("startDate", start);
            query.setTimestamp("endDate", end);
        }

        return (Map<String, Long>) query.getSingleResult();
    }

    public Map<String, Long> all(List<Integer> record_ids, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select new map(count(request) as commonCount, " +
                "(select count(request) from CorrectRequest request where request.uid.record_id in (:record_ids) " +
                "and request.device.name in (:android_names) " + getBetweenClause(startDate, endDate) + " ) as androidCount, " +
                "(select count(request) from CorrectRequest request where request.uid.record_id in (:record_ids) " +
                "and request.device.name in (:ios_names) " + getBetweenClause(startDate, endDate) + " ) as iosCount, " +
                "(select count(request) from CorrectRequest request where request.uid.record_id in (:record_ids) " +
                "and request.device.name in (:unknown_names) " + getBetweenClause(startDate, endDate) + " ) as unknownCount) " +
                "from CorrectRequest request where request.uid.record_id in (:record_ids) " + getBetweenClause(startDate, endDate));

        query.setParameterList("record_ids", record_ids);
        query.setParameterList("android_names", androids);
        query.setParameterList("ios_names", ios);
        query.setParameterList("unknown_names", unknown);

        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(startDate + " 00:00:00");
            Date end = format.parse(endDate + " 23:59:59");

            query.setTimestamp("startDate", start);
            query.setTimestamp("endDate", end);
        }

        return (Map<String, Long>) query.getSingleResult();
    }

    public List<Map<String, Object>> get(List<Integer> record_ids, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select new map (uid.id as uid_id, uid.value as uid_value, " +
                "(select count(request) from CorrectRequest request where request.uid_id=uid.id " + getBetweenClause(startDate, endDate) + " ) as request_count, " +
                "(select count(request) from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:android_names) " + getBetweenClause(startDate, endDate) + " ) as android_count, " +
                "(select count(request) from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:ios_names) " + getBetweenClause(startDate, endDate) + " ) as ios_count, " +
                "(select count(request) from CorrectRequest request where request.uid_id=uid.id and request.device.name in (:unknown_names) " + getBetweenClause(startDate, endDate) + " ) as unknown_count) " +
                "from Uid uid where uid.record_id in (:record_ids) " + getUidBetweenClause(startDate));

        query.setParameterList("record_ids", record_ids);
        query.setParameterList("android_names", androids);
        query.setParameterList("ios_names", ios);
        query.setParameterList("unknown_names", unknown);

        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(startDate + " 00:00:00");
            Date end = format.parse(endDate + " 23:59:59");

            query.setTimestamp("startDate", start);
            query.setTimestamp("endDate", end);
        }

        return (List<Map<String, Object>>) query.list();
    }

    private String getUidBetweenClause(String startDate) {
        if (startDate == null)
            return "";
        return " and uid.created_at between :startDate and :endDate ";
    }

    private String getBetweenClause(String startDate, String endDate) {
        String betweenClause = "";
        if (startDate != null && endDate != null) {
            betweenClause = " and request.created_at between :startDate and :endDate ";
        }
        return betweenClause;
    }

}
