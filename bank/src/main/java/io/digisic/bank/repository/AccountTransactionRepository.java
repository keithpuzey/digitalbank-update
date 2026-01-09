package io.digisic.bank.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import io.digisic.bank.model.Account;
import io.digisic.bank.model.AccountTransaction;
import io.digisic.bank.model.TransactionCategory;


public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long> {
	
	@Query(value = "SELECT next_val FROM transaction_number_seq", nativeQuery = true)
	Long getNextTransactionNumber();

	@Modifying
	@Query(value = "UPDATE transaction_number_seq SET next_val = next_val + 1", nativeQuery = true)
	void incrementTransactionNumber();

	AccountTransaction findByTransactionNumber(Long transactionNumber);
	
	
	public List<AccountTransaction> findAllByAccount (Account account);
	public AccountTransaction findTopByAccountOrderByTransactionDateDesc (Account account);
	public List<AccountTransaction> findTop2ByAccountOrderByTransactionDateDesc (Account account);
	
	public List<AccountTransaction> findAllByAccountAndAmountGreaterThan (Account account, BigDecimal amount);
	public List<AccountTransaction> findAllByAccountAndAmountLessThan (Account account, BigDecimal amount);
	
	public List<AccountTransaction> findAllByAccountAndAmountGreaterThanAndTransactionDateAfter (Account account, BigDecimal amount, Date date);
	public List<AccountTransaction> findAllByAccountAndAmountLessThanAndTransactionDateAfter (Account account, BigDecimal amount, Date date);
	
	public List<AccountTransaction> findAllByAccountAndTransactionCategory (Account account, TransactionCategory category);
	public List<AccountTransaction> findAllByAccountAndTransactionCategoryAndTransactionDateAfter (Account account, TransactionCategory category, Date date);

}
