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

import loc.task.dao.Dao;
import loc.task.managers.MessageManager;
import loc.task.services.IUserService;
import loc.task.services.UserService;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;

@Log4j
@Controller
//@RequestMapping("/")
@SessionAttributes(types = Account.class)
public class WelcomeController {
    @Autowired
    private IUserService userService;



    // Attribute name
    static final String CMD_VALUE = "cmdValue";
    //    static final String PARAM_SESSION_USER = "currentUser";
    static final String ACCOUNT = "account";
    static final String MESSAGE = "message";
    static final String TASK = "curTask";
    static final String LOGIN = "username";
    static final String PASSWORD = "password";
    //    static final String TASK_META = "curTaskMeta";
    static final String TASK_EMPLOYEE = "employee";
    final String POST_TITLE = "titleTask";
    final String POST_BODY = "bodyTask";
    final String POST_DEADLINE = "taskDeadline";
    final Integer employeeRole = UserService.employeeRole;
    final Integer superiorRole = UserService.superiorRole;


    @Autowired
    private Dao baseDao;


    @RequestMapping(value = "/go", method = RequestMethod.POST)

    public String welcomePage(ModelAndView model2,Model model,HttpSession session, Authentication principal,String username, String password ){
//            System.out.println(principal.getPrincipal());
//        User person = new User();
//        person.setLogin("Name");
//        person.setRole(1);
//        person.setPasswordHash("dfsasfd");

//        Assert.notNull(str);

//        SessionFactory sessionFactory;


        Session ses = baseDao.getSession();
        System.out.println(ses.getStatistics());

        System.out.println("username: " + username);
        System.out.println("password: " + password);
//        System.out.println(model.getModel());
//        System.out.println(model.getViewName());
//        System.out.println(model.getView());

//        Session ses=

        String page=null;
        StringBuffer message = new StringBuffer();
        Account account;
        System.out.println("login1");
        try {
            String userLogin = username;
            String userPassword = password;
            System.out.println("login2");
            try {
                int userId = Integer.parseInt(userLogin);
                System.out.println("login3");
                account= userService.getAccount(userId, userPassword);
            } catch (NumberFormatException e) {
                System.out.println("login3");
                account= userService.getAccount(userLogin, userPassword);
                System.out.println("login4");
            }

            //TODO удалить проверку + проверка ЕХ юзер сервис
            if (account != null) {
//                content.getSessionAttributes().put(ACCOUNT, account);
                session.setAttribute(ACCOUNT, account);
                page= PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
                message.append(MessageManager.getProperty("message.true.login"));
            }else message.append(MessageManager.getProperty("message.login.error"));
            System.out.println("login4");
        } catch (Exception e) {
            log.error(e,e);
            message.append(MessageManager.getProperty("message.login.error"));
            message.append(e);
        }
        session.setAttribute(MESSAGE, message.toString());
        System.out.println(page);
        return page;
//        return "indexTest";
    }

    @RequestMapping(value = "/login-fail", method = RequestMethod.GET)
    @RequestWrapper
    public String loginFail(ModelAndView model, @RequestParam(value = "login-fail") String error ) {
        if ("error".equals(error)) {
            model.addObject("error", "Authentication error");
        }

        return "welcome";
    }
//
//    @Controller
//    @RequestMapping(value = "/booking")
//    @SessionAttributes(types = TicketForm.class)
//    public class BookTicketController {
//
//        @RequestMapping(method = RequestMethod.GET)
//        public String start(Model model) {
//            // после выхода из start() форма будет скопирована в http session
//            // атрибуты благодаря @SessionAttributes(types = TicketForm.class)
//            model.addAttribute(new TicketForm());
//            return "booking/booking";
//        }

//        @RequestMapping(value = "/movie", method = RequestMethod.POST)
//        public String selectMovie(TicketForm ticketForm) {
//
//            Assert.notNull(ticketForm);
//            Assert.notNull(ticketForm.getMovieId());
//
//            return "booking/customer";
//        }
//
//        @RequestMapping(value = "/customer", method = RequestMethod.POST)
//        public String enterCustomerData(TicketForm ticketForm) {
//
//            Assert.notNull(ticketForm);
//            // movieId не передавался с customer.jsp, но он был сохранен в сессии во время selectMovie()
//            Assert.notNull(ticketForm.getMovieId());
//            Assert.notNull(ticketForm.getLastName());
//
//            return "booking/payment";
//        }
//
//        @RequestMapping(value = "/payment", method = RequestMethod.POST)
//        public String enterPaymentDetails(TicketForm ticketForm) {
//
//            // movieId не передавался с customer.jsp, но он был сохранен в сессии во время selectMovie()
//            Assert.notNull(ticketForm.getMovieId());
//            // lastName не передавался с payment.jsp, но он был сохранен в сессии во время enterCustomerData()
//            Assert.notNull(ticketForm.getLastName());
//            Assert.notNull(ticketForm.getCreditCardNumber());
//
//            return "redirect:/booking/confirmation";
//        }
//
//        @RequestMapping(value = "/confirmation", method = RequestMethod.GET)
//        public String confirmation(SessionStatus status) {
//            status.setComplete(); // очищаем Spring Session в целях безопасности личных данных
//            return "booking/confirmation";
//        }
//
//



    }



