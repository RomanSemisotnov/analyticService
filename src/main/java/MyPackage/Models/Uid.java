package MyPackage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "uids")
public class Uid {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", insertable = false, updatable = false)
    @JsonIgnore
    private Record record;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @OneToMany(mappedBy = "uid", fetch = FetchType.EAGER)
    private List<CorrectRequest> correctRequests;

    public List<CorrectRequest> getCorrectRequests() {
        return correctRequests;
    }

    public void setCorrectRequests(List<CorrectRequest> correctRequests) {
        this.correctRequests = correctRequests;
    }

    @Column(name = "value")
    private String value;

    @Column(name = "record_id")
    private int record_id;

    @Column(name = "patternlink_id")
    private int patternlink_id;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public int getPatternlink_id() {
        return patternlink_id;
    }

    public void setPatternlink_id(int patternlink_id) {
        this.patternlink_id = patternlink_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

}
