package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.NewsDTO;
import com.googe.ssadm.sc.entity.News;
import com.googe.ssadm.sc.entity.User;
import com.googe.ssadm.sc.mappers.NewsMapper;
import com.googe.ssadm.sc.service.NewsService;
import com.googe.ssadm.sc.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;
    private final NewsMapper newsMapper;
    private final UserService userService;

    public NewsController(NewsService newsService , NewsMapper newsMapper , UserService userService) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
        this.userService = userService;
    }

    @Value("${page.news.size}")
    private int pageSize;
    @Value("${page.startpage}")
    private int startPage;

    @GetMapping(path = "")
    public String allNews(HttpServletRequest request , Model model) {
        Page<News> newsPage = newsPage(request);
        model.addAttribute("newsPage" , newsPage.map(newsMapper::toNewsDTO));
        return "news";
    }

    @GetMapping(path = "/new")
    public String newNews(Model model) {
        model.addAttribute("news" , new NewsDTO());
        return "newsform";
    }

    @GetMapping(path = "/edit/{id}")
    public String editNews(@PathVariable("id") Long id , Model model) {
        News editnews = newsService.findById(id).orElse(new News());
        model.addAttribute("news" , newsMapper.toNewsDTO(editnews));
        return "newsform";
    }

    @PostMapping(path = "/new")
    public String addNewsForm(NewsDTO news ,
                              Authentication authentication) {
        News addNews = newsMapper.toNews(news);
        User user = userService.findByEmail(authentication.getName()).orElse(new User());
        addNews.setAuthor(user.getName() + " " + user.getSurname());
        newsService.save(addNews);
        return "redirect:/news";
    }

    @GetMapping(path = "/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsService.deleteById(id);
        return "redirect:/news";
    }

    private Page<News> newsPage(HttpServletRequest request) {
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            startPage = Integer.parseInt(request.getParameter("page")) - 1;
        }
        return newsService.findAllPaginated(PageRequest.of(
                startPage , pageSize , Sort.by("id").descending()));
    }
}
