package com.example.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String jsonFile = "data.json";
        List<Employee> list = parseCSV(columnMapping,fileName);
        String json = listToJson(list);
        writeFile(json,jsonFile);


    }
    public static List<Employee> parseCSV(String[] column, String name){
        List<Employee> staff = new ArrayList<>();
        try(CSVReader reader = new CSVReader(new FileReader(name))){
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(column);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
           staff = csv.parse();
            staff.forEach(System.out::println);
        }catch (IOException e){
           e.printStackTrace();
        }
        return staff;
    }

    public static String listToJson(List<Employee> list){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        System.out.println(gson.toJson(list));
        return json;
    }
    public static String writeFile(String json, String fileNameJson) {
        try (FileWriter file = new
                FileWriter(fileNameJson)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}