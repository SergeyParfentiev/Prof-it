package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.CommentDTO;
import project.dto.UserDTO;
import project.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    @Transactional
    public void save(List<CommentDTO> commentList) {
        commentRepository.save(commentList);
    }

    public List<CommentDTO> getList(String username) {
        UserDTO currentUser;
        if (username != null && (currentUser = userService.getByUserName(username)) != null) {
            return commentRepository.getListByEmail(currentUser.getEmail());
        }
        return new ArrayList<>();
    }

    public CommentDTO getById(long id) {
        return commentRepository.findOne(id);
    }

    public long getWordCount(String bodyWord) {
        Long count = commentRepository.getWordCountByBodyWord(bodyWord);
        return count != null ? count : 0;
    }

    @Modifying
    @Transactional
    public CommentDTO createOwnComment(String username, Long postId, String commentName, String commentBody) {
        UserDTO currentUser;
        if (username != null && postId != null && commentName != null && commentBody != null &&
                (currentUser = userService.getByUserName(username)) != null && postService.findById(postId) != null) {
            return commentRepository.saveAndFlush(new CommentDTO(postId, commentName, currentUser.getEmail(), commentBody));
        }
        return null;
    }

    @Modifying
    @Transactional
    public CommentDTO editOwnComment(String username, Long commentId, String commentName, String commentBody) {
        UserDTO currentUser;
        CommentDTO editedComment;
        if (username != null && commentId != null && commentName != null && commentBody != null &&
                (currentUser = userService.getByUserName(username)) != null &&
                (editedComment = commentRepository.getByIdAndEmail(commentId, currentUser.getEmail())) != null) {
            editedComment.setName(commentName);
            editedComment.setBody(commentBody);
            return commentRepository.saveAndFlush(editedComment);
        }

        return null;
    }

    @Modifying
    @Transactional
    public boolean deleteOwnComment(String username, Long commentId) {
        UserDTO currentUser;
        if (username != null && commentId != null && (currentUser = userService.getByUserName(username)) != null &&
                commentRepository.getByIdAndEmail(commentId, currentUser.getEmail()) != null) {
            commentRepository.delete(commentId);
            return commentRepository.findOne(commentId) == null;
        }

        return false;
    }
}

