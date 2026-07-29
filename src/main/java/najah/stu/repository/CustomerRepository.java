package najah.stu.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import najah.stu.domain.Customer;

public class CustomerRepository {

    private static final String FILE_PATH = "data/customers.txt";

    public Customer findByUsername(String username) {

        try (BufferedReader reader =
                new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if(line.isBlank())
                    continue;

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0].trim());
                String savedUsername = data[1].trim();
                String password = data[2].trim();
                String email = data[3].trim();

                if(savedUsername.equalsIgnoreCase(username)) {
                    return new Customer(id,savedUsername,password,email);
                }
            }

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}