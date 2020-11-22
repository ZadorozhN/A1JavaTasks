package org.zadorozhn.task.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zadorozhn.task.database.DataProvider;
import org.zadorozhn.task.model.Posting;

import java.util.List;

@RestController
@Slf4j
public class PostingController {

    @Autowired
    DataProvider dataProvider;

    @GetMapping("/posting/{id}")
    public Posting getPosting(@PathVariable long id) {
        return dataProvider.getPostingById(id).orElseThrow();
    }

    @GetMapping("/allpostings")
    public List<Posting> getAllPosting() {
        return dataProvider.getAllPostings();
    }

    @GetMapping("/postings")
    public List<Posting> getPostingByPeriod(@RequestParam(name = "from", required = true) String from,
                                            @RequestParam(name = "to", required = true) String to,
                                            @RequestParam(name = "isActive", required = true) String isActive) {

        List<Posting> postings = dataProvider.getPostingsByPeriod(from, to, Boolean.parseBoolean(isActive));
        return postings;
    }
}
