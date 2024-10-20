package de.markant.lksg.application.task.constants;

/**
 * Constants for this application
 */
public final class Constants {
    private Constants() {
    }


    public static final String BASE_PATH = "/api/accounts";
    public static final String ACCOUNT_PATH = "/{accountNumber}";
    public static final String DEPOSIT_PATH = "/deposit";
    public static final String WITHDRAW_PATH = "/withdraw";
    public static final String TRANSFER_PATH = "/transfer";
    public static final String TRANSACTIONS_PATH = "/transactions";

    // API Messages
    public static final String ACCOUNT_NOT_FOUND = "Account not found!";
    public static final String ACCOUNT_ALREADY_EXISTS = "Account already exists.";
    public static final String DEPOSIT_SUCCESS = "Amount %s deposited successfully!";
    public static final String WITHDRAW_SUCCESS = "Amount %s has been withdrawn successfully!";
    public static final String TRANSFER_SUCCESS = "Amount %s transferred successfully!";
}
