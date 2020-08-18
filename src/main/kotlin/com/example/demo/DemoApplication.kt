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

class HelloController {
    @RequestMapping("/hello")
    fun hello(@RequestParam(required = false, defaultValue = "Koo") name: String): String {
        return "Hello $name!"
    }
    // http://localhost:8080/whatismyname?name=Chris
    @RequestMapping("/whatismyname")
    fun name(@RequestParam(required = false, defaultValue = "Chris") name: String): String {
        return "Your Name is $name!"
    }
    // http://localhost:8080/whatismynumber?number=2177789504
    @RequestMapping("/whatismynumber")
    fun number(@RequestParam(required = true) number: String): String {
        return "Your Number is $number!"
    }
    // http://localhost:8080/whatismynumberwithname?name=Chris&number=141233123
    @RequestMapping("/whatismynumberwithname")
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
