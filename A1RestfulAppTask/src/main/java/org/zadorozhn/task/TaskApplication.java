package org.zadorozhn.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.zadorozhn.task.database.AuthorizedDeliveryParser;
import org.zadorozhn.task.database.DataProvider;
import org.zadorozhn.task.model.Login;
import org.zadorozhn.task.model.Posting;

import javax.xml.crypto.Data;
import java.util.Map;

@SpringBootApplication
public class TaskApplication {

    public static void main(String[] args) {
        Map<String, Login> authorizedUsers = AuthorizedDeliveryParser.readLogins("D:/Downloads/logins.csv");
        Map<Long, Posting> postings = AuthorizedDeliveryParser.readPostings("D:/Downloads/postings.csv", authorizedUsers);

        DataProvider dataProvider = new DataProvider();
        dataProvider.dropLoginTable();
        dataProvider.dropPostingTable();
        dataProvider.createLoginTable();
        dataProvider.createPostingTable();
        authorizedUsers.values().forEach(dataProvider::saveLogin);
        postings.values().forEach(dataProvider::savePosting);

        SpringApplication.run(TaskApplication.class, args);
    }

}
