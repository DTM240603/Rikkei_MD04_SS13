package ra.edu.hw.md04.ss13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.hw.md04.ss13.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
}
