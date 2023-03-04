import org.example.Employee;
import org.example.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.example.Main.parseCSV;
import static org.example.Main.parseXML;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JsonParserTest {
    @Test
    public void parseCsvFile() {
        Employee employee1 = new Employee(1, "John", "Smith", "USA", 25);
        Employee employee2 = new Employee(2, "Inav", "Petrov", "RU", 23);
        List<Employee> expectedEmployees = Arrays.asList(employee1, employee2);
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> employees = parseCSV(columnMapping, fileName);
        assertThat(employees, is(expectedEmployees));
    }

    @Test
    public void parseXmlFile() {
        Employee employee1 = new Employee(1, "John", "Smith", "USA", 25);
        Employee employee2 = new Employee(2, "Inav", "Petrov", "RU", 23);
        List<Employee> expectedEmployees = Arrays.asList(employee1, employee2);
        String fileName = "data.xml";
        List<Employee> employees = parseXML(fileName);
        assertThat(employees, is(expectedEmployees));
    }

    @Test
    public void listToJson() {
        Employee employee1 = new Employee(1, "John", "Smith", "USA", 25);
        Employee employee2 = new Employee(2, "Inav", "Petrov", "RU", 23);
        List<Employee> employees = Arrays.asList(employee1, employee2);
        String expectedJson = "[" +
                "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}," +
                "{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}" +
                "]";
        String json = Main.listToJson(employees);
        Assertions.assertEquals(json, expectedJson);
    }
}
