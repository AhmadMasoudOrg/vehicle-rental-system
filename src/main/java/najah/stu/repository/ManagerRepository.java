package najah.stu.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import najah.stu.domain.Manager;

public class ManagerRepository {

    private static final String FILE_PATH = "data/managers.txt";

    public Manager findByUsername(String username) {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0].trim());
                String savedUsername = data[1].trim();
                String password = data[2].trim();

                if (savedUsername.equalsIgnoreCase(username)) {
                    return new Manager(id, savedUsername, password);
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println(
                    "Error reading managers file: " + e.getMessage()
            );
        }

        return null;
    }
}