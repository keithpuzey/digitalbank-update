package io.digisic.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.digisic.bank.model.security.Users;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key, auto-generated

    @Column(name = "account_number", unique = true, nullable = false)
    private Long accountNumber; // Will be generated in code, not by DB

    private String name;
    private BigDecimal currentBalance;
    private BigDecimal openingBalance;
    private double interestRate;
    private double paymentAmount;
    private int paymentTerm;

    @OneToOne(fetch = FetchType.EAGER)
    private AccountType accountType;

    @OneToOne(fetch = FetchType.EAGER)
    private OwnershipType ownershipType;

    @OneToOne(fetch = FetchType.EAGER)
    private AccountStanding accountStanding;

    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm")
    private Date dateOpened;

    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm")
    private Date dateClosed;

    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm")
    private Date paymentDue;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Users owner;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Users coowner;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("transaction_date DESC")
    @JsonIgnore
    private List<AccountTransaction> accountTransactionList;

    // Constructors, getters & setters
    public Account() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAccountNumber() { return accountNumber; }
    public void setAccountNumber(Long accountNumber) { this.accountNumber = accountNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public double getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(double paymentAmount) { this.paymentAmount = paymentAmount; }

    public int getPaymentTerm() { return paymentTerm; }
    public void setPaymentTerm(int paymentTerm) { this.paymentTerm = paymentTerm; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public OwnershipType getOwnershipType() { return ownershipType; }
    public void setOwnershipType(OwnershipType ownershipType) { this.ownershipType = ownershipType; }

    public AccountStanding getAccountStanding() { return accountStanding; }
    public void setAccountStanding(AccountStanding accountStanding) { this.accountStanding = accountStanding; }

    public Date getDateOpened() { return dateOpened; }
    public void setDateOpened(Date dateOpened) { this.dateOpened = dateOpened; }

    public Date getDateClosed() { return dateClosed; }
    public void setDateClosed(Date dateClosed) { this.dateClosed = dateClosed; }

    public Date getPaymentDue() { return paymentDue; }
    public void setPaymentDue(Date paymentDue) { this.paymentDue = paymentDue; }

    public Users getOwner() { return owner; }
    public void setOwner(Users owner) { this.owner = owner; }

    public Users getCoowner() { return coowner; }
    public void setCoowner(Users coowner) { this.coowner = coowner; }

    public List<AccountTransaction> getAccountTransactionList() { return accountTransactionList; }
    public void setAccountTransactionList(List<AccountTransaction> accountTransactionList) {
        this.accountTransactionList = accountTransactionList;
    }


	public String toString() {
	    
		String account = "\n\nAccount ***********************";
    
		account += "\nId:\t\t\t" 		+ this.getId();
		account += "\nName:\t\t" 		+ this.getName();
		account += "\nType:\t\t\t" 		+ this.getAccountType();
	    
	    return account;
	    
	}
	
}
