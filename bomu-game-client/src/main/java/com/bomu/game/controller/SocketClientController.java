package com.bomu.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class SocketClientController extends BaseController {

    @RequestMapping("/index")
    public String index(Model model) {
        return "index";
    }

}
