package io.digisic.bank.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import io.digisic.bank.model.Account;
import io.digisic.bank.model.AccountType;
import io.digisic.bank.model.security.Users;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import io.digisic.bank.model.Account;
import org.springframework.transaction.annotation.Transactional; 

public interface AccountRepository extends CrudRepository<Account, Long> {

	@Query("SELECT MAX(a.accountNumber) FROM Account a")
    Long findMaxAccountNumber();
	Account findByAccountNumber(Long accountNumber);
    
    @Query(value = "SELECT next_val FROM account_number_seq", nativeQuery = true)
    Long getNextAccountNumber();

    @Modifying
    @Transactional
    @Query(value = "UPDATE account_number_seq SET next_val = next_val + 1", nativeQuery = true)
    void incrementAccountNumber();
    
  
    
    
    @Transactional // Required for delete operations
    void deleteByOwner(Users owner);
    
	List<Account> findAll ();
	
	List<Account> findByAccountType (AccountType accountType);
	
	List<Account> findByAccountType_Category (String category);
	
	List<Account> findByOwner (Users user);
	
	List<Account> findByCoowner (Users user);
	
	List<Account> findByOwnerAndAccountType (Users user, AccountType accountType);
	
	List<Account> findByCoownerAndAccountType (Users user, AccountType accountType);
	
	List<Account> findByOwnerAndAccountType_Category (Users user, String category);
	
	List<Account> findByCoownerAndAccountType_Category (Users user, String category);
	
}
