package homo.efficio.grpc.scratchpad.hello;

import io.grpc.StatusRuntimeException;

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
}
