package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.dto.PostDTO;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostDTO, Long> {

    @Query("SELECT p FROM PostDTO p WHERE p.id = :id")
    PostDTO getById(@Param("id") Long id);

    List<PostDTO> findAllByTitle(String title);

    List<PostDTO> findAllByTitleContaining(String title);

    @Modifying
    @Transactional
    @Query(value = "DELETE p, c FROM post p INNER JOIN comment c ON p.id = c.post_id WHERE p.id = :id", nativeQuery = true)
    void deleteById(@Param("id") long id);
}
