/*
 * Copyright (C) 2014 GHX, Inc.
 *  Louisville, Colorado, USA.
 *  All rights reserved.
 *
 *  Warning: Unauthorized reproduction or distribution of this program, or
 *  any portion of it, may result in severe civil and criminal penalties,
 *  and will be prosecuted to the maximum extent possible under the law.
 *
 *  Created on 023 23.05.2014
 */
package loc.task.controller;

import loc.task.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.ws.RequestWrapper;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {


    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcomePage() {

        User person = new User();
        person.setLogin("Name");
        person.setRole(1);
        person.setPasswordHash("dfsasfd");

        return "welcome";
    }

    @RequestMapping(value = "/login-fail", method = RequestMethod.GET)
    @RequestWrapper
    public String loginFail(ModelAndView model, @RequestParam(value = "login-fail") String error ) {
        if ("error".equals(error)) {
            model.addObject("error", "Authentication error");
        }

        return "welcome";
    }
}



