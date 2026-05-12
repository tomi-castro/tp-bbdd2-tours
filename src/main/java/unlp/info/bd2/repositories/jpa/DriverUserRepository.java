package unlp.info.bd2.repositories.jpa;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.DriverUser;

@Repository
public interface DriverUserRepository extends ListCrudRepository<DriverUser, Long> {
    public Optional<DriverUser> findByUsername(String username);
}