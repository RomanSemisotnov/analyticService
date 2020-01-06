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
public class AveragePricePerClickService {

    @Autowired
    private SessionFactory sessionFactory;

    public String get(List<Integer> record_ids, String startDate, String endDate) throws ParseException {
        Session session = sessionFactory.getCurrentSession();

        String betweenDateClause = "";
        if (startDate != null && endDate != null) {
            betweenDateClause = " and request.created_at between :startDate and :endDate ";
        }

        Query query = session.createQuery("select " +
                "case when count(request)=0 then 'No opening' else round(request.uid.record.priceOneTag * request.uid.record.needLinks / count(request),2) end " +
                "from CorrectRequest request " +
                "where request.uid.record.id in (:record_ids) and request.isConversion = 'yes' " + betweenDateClause);

        query.setParameterList("record_ids", record_ids);

        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(startDate + " 00:00:00");
            Date end = format.parse(endDate + " 23:59:59");
            query.setTimestamp("startDate", start);
            query.setTimestamp("endDate", end);
        }

        return (String) query.getSingleResult();
    }

}
