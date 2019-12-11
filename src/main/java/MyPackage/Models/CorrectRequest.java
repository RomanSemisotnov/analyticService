package MyPackage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "correct_requests")
public class CorrectRequest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid_id", insertable = false, updatable = false)
    @JsonIgnore
    private Uid uid;

    public Uid getUid() {
        return uid;
    }

    public void setUid(Uid uid) {
        this.uid = uid;
    }

    @Column(name = "uid_id")
    private int uid_id;

    @Column(name = "client_id")
    private int client_id;

    @Column(name = "device_id")
    private int device_id;

    @Column(name = "ip")
    private String ip;

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

    public int getUid_id() {
        return uid_id;
    }

    public void setUid_id(int uid_id) {
        this.uid_id = uid_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
