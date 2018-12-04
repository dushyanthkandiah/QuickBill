package Models;

/**
 * Created by Dushyanth on 2018-12-04.
 */

public class ClassRequest {

    private int reqId, status;
    private ClassUsers classUsers;
    private String reqDate;
    private Double total;

    public ClassRequest(int reqId, int status, String reqDate, Double total) {
        classUsers = new ClassUsers();
        this.reqId = reqId;
        this.status = status;
        this.reqDate = reqDate;
        this.total = total;
    }

    public ClassRequest() {

    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ClassUsers getClassUsers() {
        return classUsers;
    }

    public void setClassUsers(ClassUsers classUsers) {
        this.classUsers = classUsers;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
