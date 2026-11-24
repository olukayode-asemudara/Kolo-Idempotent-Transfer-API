package com.kolo.idempotencytransfer.data.repositories;

import com.kolo.idempotencytransfer.data.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
