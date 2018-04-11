package pmsAutomation;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.example.server.GreetingServiceGrpc;
import com.example.server.HelloRequest;
import com.example.server.HelloResponse;
import com.google.protobuf.LazyStringArrayList;
import com.google.protobuf.ProtocolStringList;
import com.nest.example.service.GreetingServiceImpl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * 
 * @author sergio.garcia
 *
 */
public class MicroServicesTest {
	private Server server;

	@BeforeSuite
	public void initServer() throws IOException, InterruptedException {
		server = ServerBuilder.forPort(9080).addService(new GreetingServiceImpl()).build();
		server.start();
		// server.awaitTermination();
		System.out.println("Server started at port 9080");
	}

	/**
	 * Prueba del microservicio de saludos
	 * 
	 * @param name
	 * @param hobbies
	 * @param bagOfTricks
	 */
	@Test(dataProvider = "getInfoGreeting")
	public void greetingTest(String name, ProtocolStringList hobbies, Map<String, String> bagOfTricks) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9080).usePlaintext().build();
		// Pasamos los parametros al servicio
		GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
		HelloResponse response = stub.greeting(
				HelloRequest.newBuilder().setName(name).putAllBagOfTricks(bagOfTricks).addAllHobbies(hobbies).build());
		// Realizamos los assert correspondientes
		assertEquals(response.getGreeting(), "Hello: Sergio");
		assertEquals(response.getHobbies(1), "comics");
		assertEquals(response.getBagOfTricksMap().get("Trick1"), "magic!");
	}

	@AfterSuite
	public void stopServer() {
		server.shutdown();
	}

	/**
	 * Informacion para el test
	 * 
	 * @return
	 */
	@DataProvider(name = "getInfoGreeting")
	public Object[][] getInfoGreeting() {
		ProtocolStringList hobbies = new LazyStringArrayList();
		hobbies.add("chess");
		hobbies.add("comics");
		Map<String, String> bagTricks = new HashMap<String, String>();
		bagTricks.put("Trick1", "magic!");
		return new Object[][] { { "Sergio", hobbies, bagTricks } };
	}

}
