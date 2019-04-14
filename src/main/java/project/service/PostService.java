package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.PostDTO;
import project.dto.UserDTO;
import project.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    public void save(List<PostDTO> postList) {
        postRepository.save(postList);
    }

    public List<PostDTO> getListByTitle(String title) {
        return postRepository.findAllByTitle(title);
    }

    public List<PostDTO> getListLikeTitle(String title) {
        return postRepository.findAllByTitleContaining(title);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (id != null && postRepository.findOne(id) != null) {
            postRepository.deleteById(id);
            postRepository.flush();
            return postRepository.getById(id) == null;
        }
        return false;
    }

    public PostDTO findById(long id) {
        return postRepository.findOne(id);
    }

    @Transactional
    public boolean deleteOwnById(String username, Long id) {
        UserDTO currentUser;
        PostDTO deletingPost;
        return username != null && id != null && (currentUser = userService.getByUserName(username)) != null &&
                (deletingPost = postRepository.findOne(id)) != null &&
                currentUser.getId() == deletingPost.getUserId() && deleteById(id);

    }
}
