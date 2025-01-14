package fit.fitspring.domain.trainer;

import fit.fitspring.domain.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long>, TrainerRepositoryCustom {
    boolean existsById(Long id);
    Optional<Trainer> findById(Long id);
    /*Page<Trainer> findAllById(Pageable pageable);*/
}

