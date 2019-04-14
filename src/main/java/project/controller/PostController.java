package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dto.PostDTO;
import project.service.PostService;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping("/rest/post/list")
    public List<PostDTO> getListByTitle(@RequestParam(value = "title", required = false) String title) {
        return postService.getListByTitle(title);
    }

    @RequestMapping("/rest/post/list/like")
    public List<PostDTO> getListLikeTitle(@RequestParam(value = "title", required = false) String title) {
        return postService.getListLikeTitle(title);
    }

    @RequestMapping(value = "/rest/post/delete", method = RequestMethod.DELETE)
    public boolean deleteById(@RequestParam(value = "id", required = false) Long postId) {
        return postService.deleteById(postId);
    }

    @RequestMapping(value = "/rest/own/post/delete", method = RequestMethod.DELETE)
    public boolean delete(@RequestParam(required = false) String username, @RequestParam(required = false) Long postId) {
        return postService.deleteOwnById(username, postId);
    }
}
