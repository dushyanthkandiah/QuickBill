package Models;

/**
 * Created by Dushyanth on 2018-12-04.
 */

public class ClassUsers {

    private int id, phone;
    private String name, email, nic, gender, dob, address, password, dateJoined, designation;

    public ClassUsers(int id, int phone, String name, String email, String nic, String gender, String dob, String address, String password, String dateJoined, String designation) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.nic = nic;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.password = password;
        this.dateJoined = dateJoined;
        this.designation = designation;
    }

    public ClassUsers() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
