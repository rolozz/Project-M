package com.java.project.userinfoservice.repositories;

import com.java.project.userinfoservice.entities.AccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountIdRepository extends JpaRepository<AccountId, Long> {
    Optional<AccountId> findByUsername(String username);

    List<AccountId> findByUsernameIn(List<String> usernames);
}
