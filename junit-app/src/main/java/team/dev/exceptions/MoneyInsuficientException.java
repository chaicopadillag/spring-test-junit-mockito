package team.dev.exceptions;

public class MoneyInsuficientException extends  RuntimeException{
    public MoneyInsuficientException(String message) {
        super(message);
    }
}
