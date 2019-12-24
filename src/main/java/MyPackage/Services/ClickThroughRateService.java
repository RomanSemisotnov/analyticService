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
public class ClickThroughRateService {

    @Autowired
    private SessionFactory sessionFactory;

    public List get(int record_id) {
        Session session = sessionFactory.getCurrentSession();

        Query query=session.createQuery("select new map" +
                "(count(request) as withConversion, " +
                "(select count(request) from CorrectRequest request " +
                "where request.uid.record_id = :record_id and request.isConversion ='no') " +
                "as withoutConversion)  " +
                "from CorrectRequest request " +
                "where request.uid.record_id = :record_id and request.isConversion ='yes'");

        query.setParameter("record_id",record_id);

        return query.list();
    }

    public List get(int record_id, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        Query query=session.createQuery("select new map" +
                "(count(request) as withConversion, " +
                "(select count(request) from CorrectRequest request " +
                "where request.uid.record_id = :record_id and request.isConversion ='no' and request.created_at between :startDate and :endDate) " +
                "as withoutConversion)  " +
                "from CorrectRequest request " +
                "where request.uid.record_id = :record_id and request.isConversion ='yes' and request.created_at between :startDate and :endDate");

        query.setParameter("record_id",record_id);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = format.parse(startDate+" 00:00:00");
        Date end = format.parse(endDate + " 23:59:59");
        query.setTimestamp("startDate", start);
        query.setTimestamp("endDate", end);

        return query.list();
    }

}
