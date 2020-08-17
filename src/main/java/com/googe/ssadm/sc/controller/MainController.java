package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.entity.News;
import com.googe.ssadm.sc.mappers.NewsMapper;
import com.googe.ssadm.sc.service.NewsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class MainController {
    private final NewsService newsService;
    private final NewsMapper newsMapper;

    public MainController(NewsService newsService , NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }

    @Value("${page.startpage}")
    private int startPage;

    @Value("${page.news.size}")
    private int pageSize;

    @GetMapping("/")
    public String homePage(HttpServletRequest request , Model model) {
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            startPage = Integer.parseInt(request.getParameter("page")) - 1;
        }
        Page<News> newsPage = newsService.findAllPaginated(PageRequest.of(
                startPage , pageSize , Sort.by("id").descending()));
        model.addAttribute("newsPage" , newsPage.map(newsMapper::toNewsDTO));
        return "home";
    }
}
