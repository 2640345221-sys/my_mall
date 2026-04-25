package my_mall.exception;

public class CartItemNotExistException extends BaseException {
    public CartItemNotExistException(String message) {
        super(message);
    }
}
