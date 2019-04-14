package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.UserDTO;
import project.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/rest/user/list/comment/{id}")
    public List<UserDTO> getListByCommentId(@PathVariable("id") long commentId) {
        return userService.getListByCommentId(commentId);
    }
}
