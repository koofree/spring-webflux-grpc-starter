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

@GRpcService
class GrpcHelloService : ReactorHelloServiceGrpc.HelloServiceImplBase() {
    override fun getHello(request: Mono<HelloOuterClass.Name>): Mono<HelloOuterClass.Hello> = request
        .map { name -> name.value }
        .map { value ->
            HelloOuterClass.Hello.newBuilder()
                .setMessage("Hello $value!")
                .build()
        }
}
