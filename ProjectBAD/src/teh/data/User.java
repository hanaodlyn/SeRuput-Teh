package teh.data;

public class User {

    private String userId;
    private String username;
    private String password;
    private String role;
    private String address;
    private String phonenumber;
    private String gender;


    public boolean isAdmin() {
        return role.equals("Admin");
    }

    public User(String userId, String username, String password, String role, String address, String phonenumber, String gender) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.phonenumber = phonenumber;
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
