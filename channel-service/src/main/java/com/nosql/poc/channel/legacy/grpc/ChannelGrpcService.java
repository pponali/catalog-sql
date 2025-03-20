package com.nosql.poc.channel.legacy.grpc;

import com.scaler.grpc.channel.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Legacy gRPC service implementation - kept for backward compatibility
 * This is a simplified version that logs requests but returns not implemented errors
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ChannelGrpcService extends ChannelServiceGrpc.ChannelServiceImplBase {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void getChannel(GetChannelRequest request, StreamObserver<ChannelResponse> responseObserver) {
        log.info("Received legacy gRPC request to get channel with ID: {}", request.getId());
        responseObserver.onError(Status.UNIMPLEMENTED
                .withDescription("This legacy gRPC method is no longer supported")
                .asRuntimeException());
    }

    @Override
    public void getPlatform(GetPlatformRequest request, StreamObserver<PlatformResponse> responseObserver) {
        log.info("Received legacy gRPC request to get platform with ID: {}", request.getId());
        responseObserver.onError(Status.UNIMPLEMENTED
                .withDescription("This legacy gRPC method is no longer supported")
                .asRuntimeException());
    }

    @Override
    public void getStore(GetStoreRequest request, StreamObserver<StoreResponse> responseObserver) {
        log.info("Received legacy gRPC request to get store with ID: {}", request.getId());
        responseObserver.onError(Status.UNIMPLEMENTED
                .withDescription("This legacy gRPC method is no longer supported")
                .asRuntimeException());
    }

    @Override
    public void createChannel(CreateChannelRequest request, StreamObserver<ChannelResponse> responseObserver) {
        log.info("Received legacy gRPC request to create channel");
        responseObserver.onError(Status.UNIMPLEMENTED
                .withDescription("This legacy gRPC method is no longer supported")
                .asRuntimeException());
    }

    @Override
    public void updateChannel(UpdateChannelRequest request, StreamObserver<ChannelResponse> responseObserver) {
        log.info("Received legacy gRPC request to update channel");
        responseObserver.onError(Status.UNIMPLEMENTED
                .withDescription("This legacy gRPC method is no longer supported")
                .asRuntimeException());
    }

    @Override
    public void deleteChannel(DeleteChannelRequest request, StreamObserver<DeleteChannelResponse> responseObserver) {
        log.info("Received legacy gRPC request to delete channel with ID: {}", request.getId());
        
        DeleteChannelResponse response = DeleteChannelResponse.newBuilder()
                .setSuccess(false)
                .setMessage("This legacy gRPC method is no longer supported")
                .build();
                
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}