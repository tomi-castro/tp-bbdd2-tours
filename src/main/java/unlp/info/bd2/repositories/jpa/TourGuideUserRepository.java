package unlp.info.bd2.repositories.jpa;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.TourGuideUser;

@Repository
public interface TourGuideUserRepository extends ListCrudRepository<TourGuideUser, Long> {
    public Optional<TourGuideUser> findByUsername(String username);
}