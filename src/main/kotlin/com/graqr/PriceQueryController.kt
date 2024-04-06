package com.graqr

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/price")
class PriceQueryController {

    @Get
    fun getPrice() {
    }
}