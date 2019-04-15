package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.dto.CommentDTO;
import project.service.CommentService;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping("/rest/comment/list/user/{username}")
    public List<CommentDTO> getListByUserId(@PathVariable("username") String username) {
        return commentService.getList(username);
    }

    @RequestMapping("/rest/comment/body-word-count")
    public Long getWordCountByBodyWord(@RequestParam(value = "word", required = false) String bodyWord) {
        return commentService.getWordCount(bodyWord);
    }

    @RequestMapping(value = "/rest/own/comment/create", method = RequestMethod.POST)
    public CommentDTO createOwn(@RequestParam(required = false) String username, @RequestParam(required = false) Long postId,
                                @RequestParam(required = false) String commentName, @RequestParam(required = false) String commentBody) {
        return commentService.createOwnComment(username, postId, commentName, commentBody);
    }

    @RequestMapping(value = "/rest/own/comment/edit", method = RequestMethod.PUT)
    public CommentDTO editOwn(@RequestParam(required = false) String username, @RequestParam(required = false) Long commentId,
                              @RequestParam(required = false) String commentName, @RequestParam(required = false) String commentBody) {
        return commentService.editOwnComment(username, commentId, commentName, commentBody);
    }

    @RequestMapping(value = "/rest/own/comment/delete", method = RequestMethod.DELETE)
    public boolean deleteOwn(@RequestParam(required = false) String username, @RequestParam(required = false) Long commentId) {
        return commentService.deleteOwnComment(username, commentId);
    }
}
