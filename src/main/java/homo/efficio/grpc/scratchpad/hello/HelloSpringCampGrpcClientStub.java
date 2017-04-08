package homo.efficio.grpc.scratchpad.hello;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017-04-08.
 */
public class HelloSpringCampGrpcClientStub {

    private final Logger logger = Logger.getLogger(HelloSpringCampGrpcClientStub.class.getName());

    private final ManagedChannel channel;
    private final HelloSpringCampGrpc.HelloSpringCampBlockingStub blockingStub;
    private final HelloSpringCampGrpc.HelloSpringCampStub asyncStub;
    private final HelloSpringCampGrpc.HelloSpringCampFutureStub futureStub;

    public HelloSpringCampGrpcClientStub(String host, int port) {
        ManagedChannelBuilder channelBuilder = ManagedChannelBuilder.forAddress(host, port)
                                                                    .usePlaintext(true);
        this.channel = channelBuilder.build();
        this.blockingStub = HelloSpringCampGrpc.newBlockingStub(channel);
        this.asyncStub = HelloSpringCampGrpc.newStub(channel);
        this.futureStub = HelloSpringCampGrpc.newFutureStub(channel);
    }

    public void shutdown() throws InterruptedException {
        logger.info("gRPC Channel shutdown...");
        this.channel.shutdown().awaitTermination(2, TimeUnit.SECONDS);
    }

    public HelloSpringCampGrpc.HelloSpringCampBlockingStub getBlockingStub() {
        return blockingStub;
    }

    public HelloSpringCampGrpc.HelloSpringCampStub getAsyncStub() {
        return asyncStub;
    }

    public HelloSpringCampGrpc.HelloSpringCampFutureStub getFutureStub() {
        return futureStub;
    }
}
