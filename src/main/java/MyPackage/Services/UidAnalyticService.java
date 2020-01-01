package MyPackage.Services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UidAnalyticService {

    @Autowired
    private SessionFactory sessionFactory;

    public Object getOpenCount(List<Integer> record_ids, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select new map (count(uid) as count)" +
                "from Uid uid where uid.record_id in (:record_ids) and exists " +
                "(from CorrectRequest request where request.uid_id = uid.id " +
                getBetweenClause(startDate) + ") " + getUidBetweenClause(startDate));

        query.setParameterList("record_ids", record_ids);

        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(startDate + " 00:00:00");
            Date end = format.parse(endDate + " 23:59:59");

            query.setTimestamp("startDate", start);
            query.setTimestamp("endDate", end);
        }

        return query.getSingleResult();
    }

    public Object getNotOpenCount(List<Integer> record_ids, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select new map (count(uid) as count)" +
                "from Uid uid where uid.record_id in (:record_ids) and not exists " +
                "(from CorrectRequest request where request.uid_id = uid.id " +
                getBetweenClause(startDate) + ") " + getUidBetweenClause(startDate));

        query.setParameter("record_ids", record_ids);

        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(startDate + " 00:00:00");
            Date end = format.parse(endDate + " 23:59:59");

            query.setTimestamp("startDate", start);
            query.setTimestamp("endDate", end);
        }

        return query.getSingleResult();
    }

    private String getUidBetweenClause(String startDate) {
        if (startDate == null)
            return "";
        return " and uid.created_at between :startDate and :endDate ";
    }

    private String getBetweenClause(String startDate) {
        if (startDate == null)
            return "";
        return " and request.created_at between :startDate and :endDate ";
    }

}
