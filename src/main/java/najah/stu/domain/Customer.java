package najah.stu.domain;

public class Customer extends User {
    private String email;
    public Customer(int id, String username, String password, String email) {

        super(id, username, password, "Customer");
        this.email = email;
    }
public String getEmail() {
        return email;
    }

public void setEmail(String email) {
        this.email = email;
    }
}