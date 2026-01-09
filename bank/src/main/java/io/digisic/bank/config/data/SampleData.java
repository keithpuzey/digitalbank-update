package io.digisic.bank.config.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.digisic.bank.model.security.Users;
import io.digisic.bank.repository.AccountRepository;
import io.digisic.bank.service.SampleDataService;
import io.digisic.bank.service.UserService;


@Component
public class SampleData implements CommandLineRunner, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(SampleData.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SampleDataService sampleDataService;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public int getOrder() {
        return 1;
    }
   		
    
    @Override
    @Transactional
    public void run(String... args) {

        LOG.info("*********************************");
        LOG.info("***** Checking Sample Data ******");

        // -------------------------------------------------
        // 1. Ensure Users Exist (FK parents)
        // -------------------------------------------------
        Users owner;
        Users coowner;

        if (!userService.checkEmailAdressExists(SampleDataService.SMPL_MALE_EMAIL)) {
            LOG.info("** Creating Sample User: {}", SampleDataService.SMPL_MALE_EMAIL);
            owner = sampleDataService.createSampleMaleUser();
        } else {
            owner = userService.findByUsername(SampleDataService.SMPL_MALE_EMAIL);
        }

        if (!userService.checkEmailAdressExists(SampleDataService.SMPL_FEMALE_EMAIL)) {
            LOG.info("** Creating Sample User: {}", SampleDataService.SMPL_FEMALE_EMAIL);
            coowner = sampleDataService.createSampleFemaleUser();
        } else {
            coowner = userService.findByUsername(SampleDataService.SMPL_FEMALE_EMAIL);
        }

        // -------------------------------------------------
        // 2. Create Accounts ONLY if none exist
        // -------------------------------------------------
        if (accountRepository.count() == 0) {

            LOG.info("** Initializing Sample Accounts...");

            sampleDataService.createIndividualSavings(owner);
            sampleDataService.createIndividualSavings(coowner);
            sampleDataService.createIndividualChecking(coowner);
            sampleDataService.createJointSavings(owner, coowner);
            sampleDataService.createJointChecking(owner, coowner);

            LOG.info("** Sample Data Load Complete.");

        } else {
            LOG.info("** Accounts already exist. Skipping to prevent FK / unique key conflicts.");
        }

        LOG.info("*********************************");
    }
}