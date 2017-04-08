package homo.efficio.grpc.scratchpad.hello;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 4. 6.
 */
public class HelloSpringCampGrpcClient {

    private final Logger logger = Logger.getLogger(HelloSpringCampGrpcClient.class.getName());

    private final HelloSpringCampGrpc.HelloSpringCampBlockingStub blockingStub;
    private final HelloSpringCampGrpc.HelloSpringCampStub asyncStub;
    private final HelloSpringCampGrpc.HelloSpringCampFutureStub futureStub;

    public HelloSpringCampGrpcClient(HelloSpringCampGrpc.HelloSpringCampBlockingStub blockingStub,
                                     HelloSpringCampGrpc.HelloSpringCampStub asyncStub,
                                     HelloSpringCampGrpc.HelloSpringCampFutureStub futureStub) {
        this.blockingStub = blockingStub;
        this.asyncStub = asyncStub;
        this.futureStub = futureStub;
    }

    public void sendBlockingUnaryMessage(String clientName) {

        HelloRequest request = HelloRequest.newBuilder().setClientName(clientName).build();
        HelloResponse response;

        try {
            logger.info("Unary 서비스 호출, 메시지: [" + clientName + "]");
            response = blockingStub.unaryHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "Unary 서비스 호출 중 실패: " + e.getStatus());
            return;
        }

        logger.info("Unary 서버로부터의 응답: " + response.getWelcomeMessage());
    }

    public void sendAsyncUnaryMessage(String clientName) {

        HelloRequest request = HelloRequest.newBuilder().setClientName(clientName).build();

        logger.info("Async Unary 서비스 호출, 메시지: [" + clientName + "]");

        asyncStub.unaryHello(
                request,
                new StreamObserver<HelloResponse>() {
                    @Override
                    public void onNext(HelloResponse reeponse) {
                        logger.info("Async Unary 서버로부터의 응답\n" + reeponse.getWelcomeMessage());
                    }

                    @Override
                    public void onError(Throwable t) {
                        logger.log(Level.SEVERE, "Async Unary responseObserver.onError() 호출됨");
                    }

                    @Override
                    public void onCompleted() {
                        logger.info("Async Unary 서버 응답 completed");
                    }
                }
        );
        
        logger.info("(NonBlocking이면서) Async니까 서버 응답 기다리지 않고, 결과 신경쓰지 않고 다른 작업 할 수 있다.");
    }

    public void sendAsyncClientStreamingMessage(List<String> clientNames) {

        StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse value) {
                logger.info("Client Streaming, 서버로부터의 응답:\n" + value.getWelcomeMessage());
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.SEVERE, "Client Streaming, responseObserver.onError() 호출됨");
            }

            @Override
            public void onCompleted() {
                logger.info("Client Streaming, 서버 응답 completed");
            }
        };

        StreamObserver<HelloRequest> requestObserver = asyncStub.clientStreamingHello(responseObserver);

        try {

            for (String clientName: clientNames) {
                requestObserver.onNext(HelloRequest.newBuilder().setClientName(clientName).build());
            }
        } catch (Exception e) {
            requestObserver.onError(e);
        }

        requestObserver.onCompleted();
    }
}
