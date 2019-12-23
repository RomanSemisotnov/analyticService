package MyPackage.Models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "records")
public class Record {

    public Record(){}

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "record", fetch = FetchType.EAGER)
    private List<Uid> uids;

    public List<Uid> getUids() {
        return uids;
    }

    public void setUids(List<Uid> uids) {
        this.uids = uids;
    }

    @Column(name = "patternlink_id")
    private int patternlink_id;

    @Column(name = "client_id")
    private int client_id;

    @Column(name = "needLinks")
    private int needLinks;

    @Column(name = "priceOneTag")
    private int priceOneTag;

    @Column(name = "isActive")
    private String isActive;

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

    public int getPatternlink_id() {
        return patternlink_id;
    }

    public void setPatternlink_id(int patternlink_id) {
        this.patternlink_id = patternlink_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getNeedLinks() {
        return needLinks;
    }

    public void setNeedLinks(int needLinks) {
        this.needLinks = needLinks;
    }

    public int getPriceOneTag() {
        return priceOneTag;
    }

    public void setPriceOneTag(int priceOneTag) {
        this.priceOneTag = priceOneTag;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
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
