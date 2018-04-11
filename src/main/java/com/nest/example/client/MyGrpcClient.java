package com.nest.example.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.example.server.GreetingServiceGrpc;
import com.example.server.HelloRequest;
import com.example.server.HelloResponse;
import com.google.protobuf.Descriptors.FieldDescriptor;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MyGrpcClient {
  public static void main(String[] args) throws InterruptedException {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9080)
    	.usePlaintext()
        .build();

    GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
    HelloResponse response = stub.greeting(HelloRequest.newBuilder()
        .setName("Adriana")
        .putBagOfTricks("Trick1", "magic!")
        .addHobbies("soccer")
        .build());

    System.out.print(response);
    Map<FieldDescriptor, Object> map = response.getAllFields();
    Iterator<Entry<FieldDescriptor, Object>> obj = map.entrySet().iterator();
    while(obj.hasNext()) {
    	System.out.println(obj.next());
	}
    System.out.println(response.getAllFields().toString());
  }
}
