package com.jetbrains.handson.httpapi.routes

import io.ktor.routing.*
import com.jetbrains.handson.httpapi.models.orderStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

fun Route.listOrdersRoutes() {
    get("/order") {
        if (orderStorage.isNotEmpty()) {
            call.respond(orderStorage)
        }
    }
}

fun Route.getOrderRoute() {
    get("/order/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
        val order = orderStorage.find { it.number == id  } ?: return@get call.respondText("Not Found",  status = HttpStatusCode.NotFound)
        call.respond(order)
    }
}

fun Route.totalizeOrderRoute() {
    get("/order/{id}/total") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
        val order = orderStorage.find {it.number == id} ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)
        val total = order.contents.map {it.price * it.amount}.sum()
        call.respond(total)
    }
}

fun Application.registerOrderRoute() {
    routing {
        listOrdersRoutes()
        getOrderRoute()
        totalizeOrderRoute()
    }
}