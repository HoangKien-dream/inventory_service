package com.example.productservice.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {
    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate convertStringToLocalDate(String date){
        return LocalDate.parse(date,formatter);
    }
    public static  String convertLocalDateToString(LocalDate date){
        return date.format(formatter);
    }
}
