package com.elasticsearch.employeedao;

import com.elasticsearch.dto.Employee;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmployeeDao {

    private static RestHighLevelClient restHighLevelClient;
    ObjectMapper objectMapper = new ObjectMapper();

    private static synchronized RestHighLevelClient getConnection() throws IOException {
        if(restHighLevelClient == null) {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream("D:\\ElasticSearch\\src\\main\\resources\\application.properties");
            properties.load(inputStream);
            String host = properties.getProperty("hostName");
            int port = Integer.parseInt(properties.getProperty("port"));
            String scheme = properties.getProperty("scheme");
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, scheme)));
        }

        return restHighLevelClient;
    }

    public void getEmployee() throws Exception{

        try {

            restHighLevelClient = getConnection();
            SearchRequest searchRequest = new SearchRequest("test");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
           // searchSourceBuilder.query(QueryBuilders.matchAllQuery());                   //to get all data
            //searchSourceBuilder.query(QueryBuilders.matchQuery("employeeName", "Dipshika"));     //to get specific data
            searchSourceBuilder.query(QueryBuilders.rangeQuery("employeeAge").from(20).to(25));
            searchRequest.source(searchSourceBuilder);
            //List<Employee> employeeList=new ArrayList<>();

            SearchResponse searchResponse;
            searchResponse =restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            String source="";
            Employee employee;

            for (SearchHit hit : hits.getHits())
            {
                source = hit.getSourceAsString();
                employee =objectMapper.readValue(source, Employee.class);
                System.out.println("Employee Name :" +employee.getEmployeeName()+
                        ",  Employee Age :" +employee.getEmployeeAge()+",  Experience :"+employee.getExperience());
            }


        } catch (IOException e) {
        }

    }

    public void insertEmployee() throws Exception {

        try  {
            restHighLevelClient = getConnection();
           Employee employee= new Employee();
            employee.setEmployeeName("Revathi");
            employee.setEmployeeAge(27);
            employee.setExperience(2);
            IndexRequest indexRequest = new IndexRequest("test");
            indexRequest.source(objectMapper.writeValueAsString(employee), XContentType.JSON);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("Response id: " + indexResponse.getId());
            System.out.println("Response name: " + indexResponse.getResult().name());
        }catch(Exception e){

        }
    }
}