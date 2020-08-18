package com.example.demo

import org.lognet.springboot.grpc.GRpcService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@RestController
@RequestMapping("/hello")
class HelloController {

    @GetMapping
    fun hello(@RequestParam(required = false, defaultValue = "Koo") name: String): String {
        return "Hello $name!"
    }
}

// Basic GET request without parameter
// http://localhost:8080/whatismyname?name=Chris
@RestController
@RequestMapping("/whatismyname")
class NameController {
    @GetMapping
    fun name(@RequestParam(required = false, defaultValue = "Chris") name: String): String {
        return "Your Name is $name!"
    }
}

// GET request with parameter required
// http://localhost:8080/whatismynumber?number=2177789504
@RestController
@RequestMapping("/whatismynumber")
class NumberController {
    @GetMapping
    fun number(@RequestParam(required = true) number: String): String {
        return "Your Number is $number!"
    }
}

// GET request with multiple parameters required
// http://localhost:8080/whatismynumberwithname?name=Chris&number=141233123
@RestController
@RequestMapping("/whatismynumberwithname")
class NameNumberController {
    @GetMapping
    fun number(@RequestParam(required = true) number: String, @RequestParam(required = true) name: String): String {
        return "Hello, $name! Your Number is $number!"
    }
}


@GRpcService
class GrpcHelloService : ReactorHelloServiceGrpc.HelloServiceImplBase() {
    override fun getHello(request: Mono<HelloOuterClass.Name>): Mono<HelloOuterClass.Hello> = request
        .map { name -> name.value }
        .map { value ->
            HelloOuterClass.Hello.newBuilder()
                .setMessage("Hello $value!")
                .build()
        }
    // String
    override fun whatIsMyName(request: Mono<HelloOuterClass.NameRequest>): Mono<HelloOuterClass.Reply> = request
            .map { name -> name.value }
            .map { value ->
                HelloOuterClass.Reply.newBuilder()
                        .setMessage("Your Name is $value!")
                        .build()
            }
    // int64
    override fun whatIsMyNumber(request: Mono<HelloOuterClass.NumberRequest>): Mono<HelloOuterClass.Reply> = request
            .map { name -> name.value }
            .map { value ->
                HelloOuterClass.Reply.newBuilder()
                        .setMessage("Your Number is $value!")
                        .build()
            }
    override fun whatIsMyNumberWithName(request: Mono<HelloOuterClass.NumberNameRequest>): Mono<HelloOuterClass.Reply> = request
            .map { value -> value}
            .map { value ->
                HelloOuterClass.Reply.newBuilder()
                        .setMessage("Hello, ${value.name}! Your Number is ${value.value}!")
                        .build()
            }
}
