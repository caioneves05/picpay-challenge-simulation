package com.picpaychallengesimulation.picpaychallengesimulation.dtos;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal amount, Long senderId, Long receiverId) {
}
