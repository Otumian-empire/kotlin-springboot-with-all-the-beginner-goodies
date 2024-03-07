package com.otumianempire.kotlinspringbootwithallthebeginnergoodies

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/profiles")
    fun profiles(): String {
        return "profiles"
    }

    @GetMapping("/profile")
    fun profile(model: Model): String {
        model.addAttribute("id", 1)
        model.addAttribute("name", "Patrick Amin")
        model.addAttribute("firstName", "Patrick")
        // model.asMap()
        return "profile"
    }
}