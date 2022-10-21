package team.dev.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    public Bank() {
        this.accounts = new ArrayList<>();
    }

    private String name;

    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

 public void addAccount(Account account) {
        this.accounts.add( account);
        account.setBank(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void  transference(Account accountOrigin, Account accountDest, BigDecimal amount){
        accountOrigin.debit(amount);
        accountDest.credit(amount);
    }
}
