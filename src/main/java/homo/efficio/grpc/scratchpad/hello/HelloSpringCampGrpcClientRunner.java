package homo.efficio.grpc.scratchpad.hello;

import java.util.Arrays;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 4. 6.
 */
public class HelloSpringCampGrpcClientRunner {

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 54321;

        HelloSpringCampGrpcClientStub clientStub =
                new HelloSpringCampGrpcClientStub(host, port);

        HelloSpringCampGrpcClient grpcClient =
                new HelloSpringCampGrpcClient(clientStub.getBlockingStub(),
                                              clientStub.getAsyncStub(),
                                              clientStub.getFutureStub());

//        grpcClient.sendBlockingUnaryMessage("homo.efficio");

        grpcClient.sendAsyncClientStreamingMessage(
                Arrays.asList("클라이언트 스트리밍", "gRPC", "스프링캠프 2017")
        );

        Thread.sleep(2000);

        clientStub.shutdown();
    }
}
