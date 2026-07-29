package najah.stu.service;

import najah.stu.domain.Customer;
import najah.stu.repository.CustomerRepository;

public class CustomerService {

    private CustomerRepository repository;
    private Customer loggedCustomer;


    public CustomerService() {
        repository = new CustomerRepository();
    }


    public boolean login(String username,String password){

        Customer customer =
                repository.findByUsername(username);


        if(customer != null &&
           customer.getPassword().equals(password)){

            loggedCustomer = customer;
            return true;
        }

        return false;
    }

    public void requireLogin() {
    if (!isLoggedIn()) {
        throw new IllegalStateException(
                "Customer must login first."
        );
    }
}
    public void logout(){
        loggedCustomer = null;
    }


    public boolean isLoggedIn(){
        return loggedCustomer != null;
    }


    public Customer getLoggedCustomer(){
        return loggedCustomer;
    }
}