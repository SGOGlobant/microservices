package com.nest.example.service;

import java.util.Map;

import com.example.server.GreetingServiceGrpc;
import com.example.server.HelloRequest;
import com.example.server.HelloResponse;
import com.example.server.HelloResponse.Builder;
import com.google.protobuf.ProtocolStringList;

import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
	@Override
	public void greeting(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
		String name = request.getName();
		Map<String, String> bagOfTricks = request.getBagOfTricksMap();
		ProtocolStringList hobbies = request.getHobbiesList();
		Builder builderResponse = HelloResponse.newBuilder().setGreeting("Hello: " + name);

		builderResponse.addAllHobbies(hobbies);
		builderResponse.putAllBagOfTricks(bagOfTricks);

		HelloResponse response = builderResponse.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
