package io.digisic.bank.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

import io.digisic.bank.model.Account;
import io.digisic.bank.model.AccountTransaction;
import io.digisic.bank.model.UserProfile;
import io.digisic.bank.model.security.Role;
import io.digisic.bank.model.security.Users;
import io.digisic.bank.util.Constants;
import io.digisic.bank.model.OwnershipType;
import io.digisic.bank.repository.*;

@Service
@Transactional
public class SampleDataService {

    @Autowired
    private AccountService accountService;

    @Autowired
    @Lazy
    private UserService userService;

    private static final Faker faker = new Faker(new Locale("en-US"));

    /* ==========================================================
     * Constants
     * ========================================================== */
    public static final String SMPL_MALE_EMAIL = "jsmith@demo.io";
    public static final String SMPL_FEMALE_EMAIL = "nsmith@demo.io";
    public static final String SMPL_COMMON_PASSWORD = "Demo123!";
    public static final String SMPL_MALE_FIRST_NAME = "Josh";
    public static final String SMPL_FEMALE_FIRST_NAME = "Nicole";
    public static final String SMPL_COMMON_LAST_NAME = "Smith";

    public static final String SMPL_JOINT_CHECKING = "Joint Checking";
    public static final String SMPL_JOINT_SAVINGS = "Joint Savings";
    public static final String SMPL_INDIVIDUAL_CHECKING = "Individual Checking";
    public static final String SMPL_INDIVIDUAL_SAVINGS = "Individual Savings";

    private static final String street = faker.address().streetAddress();
    private static final String city = faker.address().city();
    private static final String zip = faker.address().zipCode().split("-")[0];
    private static final String state = faker.address().stateAbbr();
    private static final String homePhone = faker.numerify("###-###-####");

    /* ==========================================================
     * Entry point
     * ========================================================== */
    public void loadSampleData() {

        if (userService.checkEmailAdressExists(SMPL_MALE_EMAIL) ||
            userService.checkEmailAdressExists(SMPL_FEMALE_EMAIL)) {
            return;
        }

        Users male = createSampleMaleUser();
        Users female = createSampleFemaleUser();

        createIndividualSavings(male);
        createIndividualSavings(female);
        createIndividualChecking(female);

        createJointSavings(male, female);
        createJointChecking(male, female);
    }

    /* ==========================================================
     * Users
     * ========================================================== */
    public Users createSampleMaleUser() {
        return createUser(
                SMPL_MALE_EMAIL,
                SMPL_MALE_FIRST_NAME,
                "Mr.",
                "M"
        );
    }

    public Users createSampleFemaleUser() {
        return createUser(
                SMPL_FEMALE_EMAIL,
                SMPL_FEMALE_FIRST_NAME,
                "Mrs.",
                "F"
        );
    }

    private Users createUser(String email, String firstName, String title, String gender) {

        Users user = new Users(email, SMPL_COMMON_PASSWORD);
        UserProfile profile = new UserProfile();

        String ssn = faker.numerify("###-##-####");
        while (userService.checkSsnExists(ssn)) {
            ssn = faker.numerify("###-##-####");
        }

        profile.setEmailAddress(email);
        profile.setFirstName(firstName);
        profile.setLastName(SMPL_COMMON_LAST_NAME);
        profile.setTitle(title);
        profile.setGender(gender);
        profile.setDob(faker.date().birthday());
        profile.setSsn(ssn);
        profile.setAddress(street);
        profile.setCountry("US");
        profile.setLocality(city);
        profile.setPostalCode(zip);
        profile.setRegion(state);
        profile.setHomePhone(homePhone);
        profile.setMobilePhone(faker.numerify("###-###-####"));
        profile.setWorkPhone(faker.numerify("###-###-####"));

        user.setUserProfile(profile);

        userService.createUser(user, Role.ROLE_USER);
        userService.addRole(user, Role.ROLE_API);

        return user;
    }

    /* ==========================================================
     * Accounts
     * ========================================================== */
    public void createJointSavings(Users owner, Users coowner) {
        Account account = createBaseAccount(
                SMPL_JOINT_SAVINGS,
                owner,
                coowner,
                Constants.ACCT_SAV_STD_CODE,
                accountService.getOwnershipTypeJoint(),
                faker.random().nextInt(675, 2390),
                -2
        );

        addSavingsTransactions(account, 2);
    }

    public void createJointChecking(Users owner, Users coowner) {
        Account account = createBaseAccount(
                SMPL_JOINT_CHECKING,
                owner,
                coowner,
                Constants.ACCT_CHK_STD_CODE,
                accountService.getOwnershipTypeJoint(),
                faker.random().nextInt(2300, 2800),
                -2
        );

        addCheckingTransactions(account, owner);
    }

    public void createIndividualChecking(Users user) {
        Account account = createBaseAccount(
                SMPL_INDIVIDUAL_CHECKING,
                user,
                null,
                Constants.ACCT_CHK_STD_CODE,
                accountService.getOwnershipTypeIndividual(),
                faker.random().nextInt(2300, 2800),
                -2
        );

        addCheckingTransactions(account, user);
    }

    public void createIndividualSavings(Users user) {
        Account account = createBaseAccount(
                SMPL_INDIVIDUAL_SAVINGS,
                user,
                null,
                Constants.ACCT_SAV_MMA_CODE,
                accountService.getOwnershipTypeIndividual(),
                faker.random().nextInt(800, 1900),
                -6
        );

        addSavingsTransactions(account, 6);
    }

    /* ==========================================================
     * Helpers
     * ========================================================== */
@Autowired
private AccountRepository accountRepository;

    private Account createBaseAccount(
            String name,
            Users owner,
            Users coowner,
            String acctCode,
            OwnershipType ownershipType,
            int openingBalance,
            int monthsBack) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, monthsBack);

        Account account = new Account();
        account.setName(name);
        account.setOwner(owner);
        account.setCoowner(coowner);
        account.setDateOpened(cal.getTime());
        account.setOpeningBalance(BigDecimal.valueOf(openingBalance));
        account.setCurrentBalance(BigDecimal.valueOf(openingBalance)); // initialize current balance
        account.setAccountType(accountService.getAccoutTypeByCode(acctCode));
        account.setOwnershipType(ownershipType);
        
        accountService.createNewAccount(account);
        return account;
    }

    private void addSavingsTransactions(Account account, int months) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, -months);

        for (int i = 0; i < months; i++) {
            cal.add(Calendar.MONTH, 1);
            addInterestTransaction(account, new AccountTransaction(), cal);
        }
    }

    private void addCheckingTransactions(Account account, Users user) {
        List<Account> savings = accountService.getSavingsAccounts(user);
        GregorianCalendar cal = new GregorianCalendar();

        AccountTransaction txn = new AccountTransaction();
        txn.setTransactionDate(cal.getTime());
        txn.setDescription("Direct Deposit");
        txn.setAmount(BigDecimal.valueOf(2000));
        txn.setTransactionCategory(
                accountService.getTransactionCategoryByCode(Constants.ACCT_TRAN_CAT_INC_CODE));
        txn.setTransactionType(
                accountService.getTransactionTypeByCode(Constants.ACCT_TRAN_TYPE_DIRECT_DEP_CODE));

        accountService.creditTransaction(account, txn);

        if (!savings.isEmpty()) {
            AccountTransaction transfer = new AccountTransaction();
            transfer.setTransactionDate(new Date());
            transfer.setAmount(BigDecimal.valueOf(500));
            accountService.transfer(account, savings.get(0), transfer);
        }
    }

    private GregorianCalendar addDays(GregorianCalendar cal, int days) {
        GregorianCalendar copy = (GregorianCalendar) cal.clone();
        copy.add(Calendar.DAY_OF_MONTH, days);
        return copy;
    }

    private void addInterestTransaction(
            Account account,
            AccountTransaction transaction,
            GregorianCalendar cal) {

        transaction = new AccountTransaction();
        transaction.setTransactionDate(cal.getTime());
        transaction.setDescription("Monthly Interest");
        transaction.setAmount(BigDecimal.valueOf(faker.random().nextDouble() + 5));
        transaction.setTransactionCategory(
                accountService.getTransactionCategoryByCode(Constants.ACCT_TRAN_CAT_INC_CODE));
        transaction.setTransactionType(
                accountService.getTransactionTypeByCode(Constants.ACCT_TRAN_TYPE_INT_CODE));

        accountService.creditTransaction(account, transaction);
    }
}