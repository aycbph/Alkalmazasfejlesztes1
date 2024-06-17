package hu.gde.aycbph.entity;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class Blogger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;
    private String lastName;
    private String addressId;
    private int telephoneNumber;
    private String computerId;
    private String telephoneId;
    private String monitorId;
    private String printerId;
    private String notification;

    @OneToMany(mappedBy = "blogger")
    private ArrayList<Story> stories;

    private Blogger(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(int telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getComputerId() {
        return computerId;
    }

    public void setComputerId(String computerId) {
        this.computerId = computerId;
    }

    public String getTelephoneId() {
        return telephoneId;
    }

    public void setTelephoneId(String telephoneId) {
        this.telephoneId = telephoneId;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
    public ArrayList<Story> getStories(){
        return stories;
    }
    public void setStories(ArrayList<Story> stories){
        this.stories = stories;
    }
}
