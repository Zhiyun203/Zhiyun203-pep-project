package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account getAccountByUsername(String user){
        return accountDAO.getAccountByUsername(user);
    }

    public Account getAccountByID(int account_id){
        return accountDAO.getAccountByID(account_id);
    }

    public Account createAccount(Account account) {
        accountDAO.createAccount(account);
        return account;
    }
}
