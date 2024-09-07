##  Joo_Cafe -  enum Status class 분석 및 학습

### enum Status class code

    ● CartOrderStatus
    public enum CartOrderStatus {
        BEFORE_ORDER,
        WATTING_ACCEPTANCE
    }

    ● OrderCookingStatus
    public enum OrderCookingStatus {
        NONE,
        COOKING,
        PREPARED,
        FINISHED
    }

    ● OrderReceiptStatus
    public enum OrderReceiptStatus {
        CANCELED,
        REJECTED,
        WAITING,
        RECEIVED
    }

    ● Payment
    public enum Payment {
        CARD,
        ACCOUNT_TRANSFER,
        KAKAO_PAY,
        CASH
    }

### enum 개념

    Java에서 enum을 사용하여 다양한 상태 값을 정의한 클래스들이다.
    각각의 enum 클래스는 특정 도메인(ex 주문, 요리, 카트 상태 등)에서 사용될 수 있는 
    상태 값들을 미리 정의해 놓았으며, 이를 통해 코드의 가독성과 유지 보수성을 높인다.
    enum 클래스는 특정 값들을 상수처럼 사용하게 되어, 상태 값이 고정적이고 예측 가능한 경우에 적합하다.

### enum 활용

    ● 컨트롤러 및 서비스 계층에서 사용
        - 컨트롤러에서는 주문이나 카트, 요리 등의 상태 값을 enum을 통해 관리.
        - 각 상태는 명확하게 정의된 값이므로 타이핑 실수나 불명확한 값이 들어가는 것을 방지한다.
        - 비즈니스 로직에서 상태 전이를 다룰 때, enum으로 정의된 값들을 사용해 조건문이나
            상태 전환 로직을 쉽게 구현할 수 있다.

        ex) 
            public void modifyOrderCancel(String token, Long orderId) {
            
                Long userId = tokenProvider.getId(token);
                Order order = orderRepository.findById(orderId);
                    .orElseThrow(() -> new CustomException(ORDER_NOT_EXISTS));
                
                if (!order.getMember().getId().equals(userId)) {
                    throw new CustomException(ORDER_NOT_ACCESS);
                }

                if (order.getCookingStatus() == OrderCookingStatus.COOKING
                    || order.getReceiptStatus() == OrderReceiptStatus.RECEIVED) {
                    throw new CustomException(ORDER_ALREADY_COOKING_STATUS);
                }

                if (order.getReceiptStatus() == OrderReceiptStatus.CANCELED
                    || order.getReceiptStatus() == OrderReceiptStatus.REJECTED) {
                    throw new CustomException(ORDER_ALREADY_CANCELED);
                }
        
                order.modifyReceiptStatus(OrderReceiptStatus.CANCELED);
        
                orderRepository.save(order);
            }
            
            - 위 메서드의 코드 처럼 OrderCookingStatus.Cooking, OrderReceiptStatus.RECEIVED
                같은 여러 enum 클래스를 사용하여 상태를 확인하고 처리하는 구조이다.

            - OrderCookingStatus
                - enum 값으로 요리가 진행 중인 상태를 나타내고, 
                    이 코드는 (OrderCookingStatus.COOKING)에서 주문을 취소하지 못하도록 검증한다.

                    ex)
                        주문이 이미 요리 중이면 더 이상 취소할 수 없기 때문에,
                        OrderCookingStatus.COOKING 인 경우 예외를 던진다.
                            예외는 CustomException에서 개발자가 커스터마이징한 예외를 출력한다.
                            - ORDER_ALREADY_CANCELED("이미 취소된 주문입니다.", BAD_REQUEST),

            