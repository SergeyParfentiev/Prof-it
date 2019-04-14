package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.UserDTO;
import project.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void save(List<UserDTO> userList) {
        userRepository.save(userList);
    }

    public List<UserDTO> getListByCommentId(long commentId) {
        return userRepository.getListByCommentId(commentId);
    }

    public UserDTO getByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
