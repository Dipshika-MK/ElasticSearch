package com.elasticsearch.main;


import com.elasticsearch.dto.Employee;
import com.elasticsearch.employeedao.EmployeeDao;

import java.io.IOException;



public class ElasticSearchMain {

    public static void main(String[] args) {


        EmployeeDao employeeDao = new EmployeeDao();

        try{
            employeeDao.getEmployee();
        //    employeeDao.insertEmployee();
        }catch(Exception e){
            System.out.println(e);
        }

    }

}