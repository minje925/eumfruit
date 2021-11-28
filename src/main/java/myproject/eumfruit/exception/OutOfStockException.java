package myproject.eumfruit.exception;

public class OutOfStockException extends RuntimeException{
    // 상품의 주문 수량보다 재고의 수가 적을 때 발생하는 에러 클래스
    public OutOfStockException(String message) {
        super(message);
    }
}
