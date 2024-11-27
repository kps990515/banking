package com.kakaobank.repository;

import org.com.kakaobank.domain.config.JpaConfig;
import org.com.kakaobank.domain.entity.AccountEntity;
import org.com.kakaobank.domain.entity.TransferEntity;
import org.com.kakaobank.domain.entity.TransferStatus;
import org.com.kakaobank.domain.entity.UserEntity;
import org.com.kakaobank.domain.repository.AccountRepository;
import org.com.kakaobank.domain.repository.TransferRepository;
import org.com.kakaobank.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = JpaConfig.class)
@TestPropertySource(locations = "classpath:application-local.yaml") // domain 모듈 설정 참조
class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Test
    void userRepositoryTest() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("01012345678");

        // When
        UserEntity savedUser = userRepository.save(user);

        // Then
        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("John Doe");
    }

    @Test
    void accountRepositoryTest() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPhone("01098765432");
        userRepository.save(user);

        AccountEntity account = new AccountEntity();
        account.setUser(user);
        account.setAccountNumber("1234567890");
        account.setAccountBank("RYAN_BANK");
        account.setBalance(new BigDecimal("10000.00"));

        // When
        AccountEntity savedAccount = accountRepository.save(account);

        // Then
        Optional<AccountEntity> foundAccount = accountRepository.findById(savedAccount.getId());
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getAccountNumber()).isEqualTo("1234567890");
    }

    @Test
    void transferRepositoryTest() {
        // Given
        UserEntity user1 = new UserEntity();
        user1.setUsername("Sender");
        user1.setEmail("sender@example.com");
        user1.setPhone("01011112222");
        userRepository.save(user1);

        UserEntity user2 = new UserEntity();
        user2.setUsername("Receiver");
        user2.setEmail("receiver@example.com");
        user2.setPhone("01033334444");
        userRepository.save(user2);

        AccountEntity fromAccount = new AccountEntity();
        fromAccount.setUser(user1);
        fromAccount.setAccountNumber("1111111111");
        fromAccount.setAccountBank("RYAN_BANK");
        fromAccount.setBalance(new BigDecimal("5000.00"));
        accountRepository.save(fromAccount);

        AccountEntity toAccount = new AccountEntity();
        toAccount.setUser(user2);
        toAccount.setAccountNumber("2222222222");
        toAccount.setAccountBank("CHUNSIK_BANK");
        toAccount.setBalance(new BigDecimal("2000.00"));
        accountRepository.save(toAccount);

        TransferEntity transfer = new TransferEntity();
        transfer.setFromAccount(fromAccount);
        transfer.setToAccount(toAccount);
        transfer.setAmount(new BigDecimal("1000.00"));
        transfer.setTxID("TX1234567890");
        transfer.setStatus(TransferStatus.COMPLETED);

        // When
        TransferEntity savedTransfer = transferRepository.save(transfer);

        // Then
        Optional<TransferEntity> foundTransfer = transferRepository.findById(savedTransfer.getId());
        assertThat(foundTransfer).isPresent();
        assertThat(foundTransfer.get().getTxID()).isEqualTo("TX1234567890");
        assertThat(foundTransfer.get().getAmount()).isEqualTo(new BigDecimal("1000.00"));
    }
}
