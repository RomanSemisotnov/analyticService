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

@Service
@Transactional
public class AveragePricePerClickService {

    @Autowired
    private SessionFactory sessionFactory;

    public Object get(int record_id) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select new map( " +
                "case when count(request)=0 then 'No opening' else (request.uid.record.priceOneTag * request.uid.record.needLinks / count(request)) end as pricePerClick ) " +
                "from CorrectRequest request " +
                "where request.uid.record.id = :record_id and request.isConversion = 'yes' ");

        query.setParameter("record_id", record_id);

        return query.getSingleResult();
    }

    public Object get(int record_id, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select new map( " +
                "case when count(request)=0 then 'No opening' else (request.uid.record.priceOneTag * request.uid.record.needLinks / count(request)) end as pricePerClick ) " +
                "from CorrectRequest request " +
                "where request.uid.record.id = :record_id and request.isConversion = 'yes' " +
                "and request.created_at between :startDate and :endDate");

        query.setParameter("record_id", record_id);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = format.parse(startDate + " 00:00:00");
        Date end = format.parse(endDate + " 23:59:59");
        query.setTimestamp("startDate", start);
        query.setTimestamp("endDate", end);

        return query.getSingleResult();
    }

}
