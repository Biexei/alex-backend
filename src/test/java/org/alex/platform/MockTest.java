package org.alex.platform;

import org.mockserver.integration.ClientAndServer;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockTest {

    public static void main(String[] args) {

        ClientAndServer server = new ClientAndServer(1080);

        System.out.println(server.toString());

        server.when(
                request()
                        .withMethod("GET")
                        .withPath("/hello/say")
        ).respond(
                response()
                        .withBody("mock successfully")
        );
//
//        server = new ClientAndServer(7777);
//
//        server.when(
//                request()
//                        .withMethod("GET")
//                        .withPath("/hello/good")
//        ).respond(
//                response()
//                        .withBody("mock successfully")
//        );
    }
}

