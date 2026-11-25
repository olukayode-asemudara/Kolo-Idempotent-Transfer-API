package com.kolo.idempotencytransfer.data.repositories;

import com.kolo.idempotencytransfer.data.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
