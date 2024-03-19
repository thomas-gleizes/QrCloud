package org.tamikalat.qrcloud.tokens;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tamikalat.qrcloud.users.User;

public interface TokenRepository extends JpaRepository<Token, Long> {
   Optional<Token> findByHash(String hash);
   List<Token> findAllByUser(User user);
}
