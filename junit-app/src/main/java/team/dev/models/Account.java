package team.dev.models;

import team.dev.exceptions.MoneyInsuficientException;

import java.math.BigDecimal;

public class Account  {
    private  String person;
    private BigDecimal balance;

    private  Bank bank;

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Account(String person, BigDecimal balance) {
        this.person = person;
        this.balance = balance;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public  void credit (BigDecimal amount){
        this.balance= this.balance.add(amount);

    }
    public  void debit (BigDecimal amount){

       BigDecimal newSald = this.balance.subtract(amount);

       if(newSald.compareTo(BigDecimal.ZERO)<0) throw  new MoneyInsuficientException("Dinero Insuficiente");

       this.balance=newSald;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Account) ){
            return  false;
        }
        Account account = (Account) obj;

        if(this.person==null || this.balance==null) {
            return false;
        }
        return this.person.equals(account.getPerson()) && this.balance.equals(account.getBalance());
    }
}
