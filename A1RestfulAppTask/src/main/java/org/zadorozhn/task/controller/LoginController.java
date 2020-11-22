package org.zadorozhn.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zadorozhn.task.database.DataProvider;
import org.zadorozhn.task.model.Login;

@RestController
public class LoginController {

    @Autowired
    DataProvider dataProvider;

    @GetMapping("/login/{id}")
    public Login getLogin(@PathVariable String id) {
        return dataProvider.getLoginById(id).orElseThrow();
    }
}
