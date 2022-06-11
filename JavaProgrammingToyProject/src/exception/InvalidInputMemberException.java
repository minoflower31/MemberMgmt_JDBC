package exception;

public class InvalidInputMemberException extends RuntimeException {

    public InvalidInputMemberException() {
        super();
    }

    public InvalidInputMemberException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
