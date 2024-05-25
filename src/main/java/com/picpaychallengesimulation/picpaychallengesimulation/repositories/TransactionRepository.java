package com.picpaychallengesimulation.picpaychallengesimulation.repositories;

import com.picpaychallengesimulation.picpaychallengesimulation.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

}
