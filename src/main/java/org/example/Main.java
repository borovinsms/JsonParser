package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try {
            try (FileReader fileReader = new FileReader(fileName)) {
                CSVReader csvReader = new CSVReader(fileReader);
                ColumnPositionMappingStrategy<Employee> columnPositionMappingStrategy = new ColumnPositionMappingStrategy<>();
                columnPositionMappingStrategy.setType(Employee.class);
                columnPositionMappingStrategy.setColumnMapping(columnMapping);
                CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader).withMappingStrategy(columnPositionMappingStrategy).build();
                return csvToBean.parse();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Employee> parseXML(String fileName) {
        try {
            List<Employee> resultList = new ArrayList<>();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
            Node node = document.getDocumentElement();
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodeList.item(i);
                    Employee employee = new Employee(
                            Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent()),
                            element.getElementsByTagName("firstName").item(0).getTextContent(),
                            element.getElementsByTagName("lastName").item(0).getTextContent(),
                            element.getElementsByTagName("country").item(0).getTextContent(),
                            Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent())
                    );
                    resultList.add(employee);
                }
            }
            return resultList;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String fileName) {
        try {
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(json);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");
        List<Employee> listXml = parseXML("data.xml");
        String jsonXml = listToJson(listXml);
        writeString(jsonXml, "data2.json");
    }
}