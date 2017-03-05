package loc.task.controller;

import loc.task.command.CommandList;
import loc.task.command.RequestHandler;
import loc.task.entity.Task;
import loc.task.entity.User;
import loc.task.services.ITaskService;
import loc.task.services.IUserService;
import loc.task.services.exc.LocServiceException;
import loc.task.vo.Account;
import loc.task.vo.AccountSuperior;
import loc.task.vo.TaskOutFilter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j
@Controller
//@RequestMapping("/")
@SessionAttributes(types = Account.class)
public class WelcomeController {
    @Autowired
    private ITaskService taskService;
    @Autowired
    private IUserService userService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MessageSource taskStatuses;


    public final static Date currentDate = new Date();
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
    final int[] minuteList = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55}; //список минут
    final int[] hourList = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23}; //список часов

    final Integer employeeRole = userService.employeeRole;
    final Integer superiorRole = userService.superiorRole;

//    @Autowired
//    private Dao baseDao;


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String welcomePage(HttpSession session, String username, String password, Locale locale) {
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        String page = "index";
        StringBuilder message = new StringBuilder();
        Account account;
        try {
            String userLogin = username;
            String userPassword = password;
            try {
                int userId = Integer.parseInt(userLogin);
                account = userService.getAccount(userId, userPassword);
            } catch (NumberFormatException e) {
                account = userService.getAccount(userLogin, userPassword);
            }
            if (account != null) {
                session.setAttribute(ACCOUNT, account);
                if (account.getUser().getRole() == employeeRole) {
                    page = "redirect:/user/tasks";
                } else if (account.getUser().getRole() == superiorRole) {
                    page = "redirect:/superior/tasks";
                }
                message.append(getMessage("message.true.login", locale));
            } else message.append(getMessage("message.login.error", locale));
        } catch (Exception e) {
            log.error(e, e);
            message.append(getMessage("message.login.error", locale));
            message.append(e);
        }
        session.setAttribute(MESSAGE, message.toString());
        return page;
    }

    //User
    @RequestMapping(value = "/user/tasks", method = RequestMethod.GET)
    public String mainEmployee(Locale locale, HttpSession session) {
        Map<Integer, String> statuses = getStatuses(locale);
        session.setAttribute("statuses", statuses);
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            taskService.updateTaskList(account);
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e);
        }
        return "user";
    }

    @RequestMapping(value = "/user/tasks", method = RequestMethod.POST)
    public String mainEmployeePost(HttpServletRequest request, HttpSession session, Locale locale) {
//        statusManager.getStatusList(locale);
        Map<Integer, String> statuses = getStatuses(locale);
        session.setAttribute("statuses", statuses);
        RequestHandler requestHandler = new RequestHandler();
        CommandList command = requestHandler.extractValues(request);
        System.out.println(command);
        StringBuilder message = new StringBuilder();
        String page = "user";
        long taskId;
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            System.out.println(command.name() + 1);
            try {
                if (command != null) {
                    switch (command.name()) {
                        case "APPROVE_TASK": // set status 2
                            System.out.println(command.name());
                            taskId = Long.parseLong((String) requestHandler.getRequestAttributes().get(CMD_VALUE));
                            taskService.updateTaskStatus(account, taskId, command.getStatus());
                            message = message.append(getMessage("task.update", locale)).append(taskId);
                            break;
                        case "FOR_CHECKING":// set status 4
                            System.out.println(command.name() + 4);
                            taskId = Long.parseLong((String) requestHandler.getRequestAttributes().get(CMD_VALUE));
                            taskService.updateTaskStatus(account, taskId, command.getStatus());
                            message = message.append(getMessage("task.update", locale)).append(taskId);
                            break;
                        case "MAIN_FILTER":// filter task
                            System.out.println(command.name() + 4);
                            TaskOutFilter taskOutFilter = account.getCurrentTasksFilter();
                            try {
                                //TODO null pointer ошибка при не выбранных статусах, перекинуть все строки в константы
                                String[] statusesGet = (String[]) requestHandler.getRequestAttributes().get("include_status");
                                Set<Integer> includeStatus = new HashSet<>(statusesGet.length);
                                for (String str : statusesGet) {
                                    includeStatus.add(Integer.parseInt(str));
                                }
                                taskOutFilter.setIncludeStatus(includeStatus);
                            } catch (IllegalArgumentException e) {
                                log.error(e, e);

                            }
                            String ask = (String) requestHandler.getRequestAttributes().get("ask");
                            if (ask == null) {
                                taskOutFilter.setAsk(true);
                            } else taskOutFilter.setAsk(false);
                            taskOutFilter.setSort(Integer.parseInt((String) requestHandler.getRequestAttributes().get("sorting_column")));
                            taskOutFilter.setTasksPerPage(Integer.parseInt((String) requestHandler.getRequestAttributes().get("task_per_page")));
                            taskService.updateTaskList(account);
                            break;
                        case "PAGE":
                            int pageNumber = Integer.parseInt((String) requestHandler.getRequestAttributes().get(CMD_VALUE));
                            account.getCurrentTasksFilter().setPage(pageNumber);
                            account = taskService.updateTaskList(account);
                            break;
                        default:
                            break;
                    }
                }
            } catch (LocServiceException e) {
                message = message.append(getMessage("task.update.false", locale));
                log.error(e, e);
            }
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            //TODO Exception на UI для простоты ;)
            message = message.append(e);
        }
        session.setAttribute(MESSAGE, message.toString());
        return page;
    }

    @RequestMapping(path = "/user/tasks/newtask", method = RequestMethod.GET)
    public String mainEmployeeNewTask(HttpSession session) {
        String page = "addNewTask";
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            account.setCurrentTasks(null); //чистим коллекцию
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            page = "/login"; //TODO page = "/login";
        }
//        return page;
        return page;
    }

    @RequestMapping(value = "/user/tasks/newtask", method = RequestMethod.POST)
    public String mainEmployeeNewTask(Locale locale, HttpSession session, String titleTask,
                                      String bodyTask, String taskDeadline) {
        StringBuilder message = new StringBuilder();
        Account account = null;
        String page = "addNewTask";
        try {
            account = (Account) session.getAttribute(ACCOUNT); //TODO (ТЗ) вернуть в сессию уже введенные данные
//            String strTaskDeadline = taskDeadline;             //текущая дата формат 10/21/2016
            int responsiblePersonId = 0; //только для админа
            Date bodyDeadline;
            try {
                String uiDateFormat = "MM/dd/yyyy HH:mm:ss";
                int defaultHour = 20;
                int defaultMinute = 15;
                int defaultSecond = 0; //00??
                bodyDeadline = new SimpleDateFormat(uiDateFormat).
                        parse(taskDeadline.concat(" " + defaultHour + ":" + defaultMinute + ":" + defaultSecond));
                if (bodyDeadline.getTime() < (new Date().getTime())) {
                    message.append(getMessage("task.incorrect.deadline", locale));
                    throw new Exception(getMessage("task.incorrect.deadline", locale));
                }
                try {
                    Task newTask = taskService.addNewTask(account, responsiblePersonId, titleTask, bodyTask, bodyDeadline);
                    message.append(getMessage("message.task.add", locale)).append(newTask.getTaskId());
                    session.setAttribute(TASK, newTask);
                    page = "redirect:/user/tasks/task/".concat(newTask.getTaskId() + "");
                    System.out.println("addNewTask: " + newTask.getTaskId()); //для лога
                } catch (LocServiceException e) {
//                    throw new Exception(e);
                    log.error(e);
                }
            } catch (ParseException e) {
                throw new Exception(e);
            }
        } catch (Exception e) { //NumberFormatException, NullPointerException
            log.error(e, e);
            message.append(getMessage("message.task.add.false", locale));
        } finally {
            session.setAttribute(ACCOUNT, account);
            session.setAttribute(MESSAGE, message.toString());
        }
        return page;
    }

    @RequestMapping(value = "/user/tasks/task/{taskId}", method = RequestMethod.GET)
    public String mainEmployeeTask(@PathVariable(value = "taskId") Long taskId, HttpSession session, Locale locale) {
        System.out.println("********************");
        System.out.println(taskId);

        StringBuilder message = new StringBuilder();
        String page;
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            try {
                Task task = taskService.getTask(account, taskId);
                if (task != null) {
                    session.setAttribute(TASK, task);
                    account.setCurrentTasks(null); //чистим коллекции, чтобы не таскать на страницу
                    account.getUser().setTaskList(null);
                }
                page = "taskDetail";
                System.out.println(task);
                System.out.println(task.getDateCreation());
            } catch (LocServiceException e) {
                message.append(getMessage("task.detail.false", locale));
                page = "redirect:/user/tasks";
            }
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            message.append(getMessage("task.detail.false", locale));
            page = "redirect:/login"; //TODO page = "/login";
        }
        session.setAttribute(MESSAGE, message.toString());
        return page;
    }

    @RequestMapping(value = "/user/tasks/task/{taskId}", method = RequestMethod.POST)
    public String mainEmployeeTaskCorrect(HttpSession session, Locale locale, String bodyTask) {
//        System.out.println(taskId);
        StringBuilder message = new StringBuilder();
        String page = "taskDetail";
        Account account;
        try {
            account = (Account) session.getAttribute(ACCOUNT);
            Task task = (Task) session.getAttribute(TASK);
//            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            try {
                taskService.updateTaskBody(account, task, bodyTask);
                message.append(getMessage("task.update", locale)).append(task.getTaskId());
                log.info("command update task:" + task.getTaskId());
            } catch (LocServiceException e) {
                message.append(getMessage("task.update.false", locale));
//                content.getSessionAttributes().put(POST_BODY, bodyTask); //TODO вывести на UI в случае неудачи
            }
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            message.append(getMessage("task.update.false", locale));
            message = message.append(e); //TODO убрать
            page = "redirect:/user/tasks";
        }
        session.setAttribute(MESSAGE, message.toString());
        return page;
    }


    //superior
    @RequestMapping(value = "/superior/tasks", method = RequestMethod.GET)
    public String mainSuperior(Locale locale, HttpSession session) {
        Map<Integer, String> statuses = getStatuses(locale);
        session.setAttribute("statuses", statuses);
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            taskService.updateTaskList(account);
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e);
        }
        return "superior";
    }

    @RequestMapping(value = "/superior/tasks", method = RequestMethod.POST)
    public String mainSuperiorPost(HttpServletRequest request, HttpSession session, Locale locale) {
//        statusManager.getStatusList(locale);
        Map<Integer, String> statuses = getStatuses(locale);
        session.setAttribute("statuses", statuses);
        RequestHandler requestHandler = new RequestHandler();
        CommandList command = requestHandler.extractValues(request);
        System.out.println(command);
        StringBuilder message = new StringBuilder();
        String page = "superior";
        long taskId;
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            System.out.println(command.name() + 1);
            try {
                if (command != null) {
                    switch (command.name()) {
                        case "PRODUCTION": // set status 3
                            System.out.println(command.name());
                            taskId = Long.parseLong((String) requestHandler.getRequestAttributes().get(CMD_VALUE));
                            taskService.updateTaskStatus(account, taskId, command.getStatus());
                            message = message.append(getMessage("task.update", locale)).append(taskId);
                            break;
                        case "PAY_TASK":// set status 5 //назначить прием
                            taskId = Long.parseLong((String) requestHandler.getRequestAttributes().get(CMD_VALUE));

//                            page = "redirect:/superior/tasks/assignment/new";
                            page = "redirect:/superior/tasks/assignment/".concat(taskId + "");
//                            taskService.updateTaskStatus(account, taskId, command.getStatus());
//                            message = message.append(getMessage("task.update", locale)).append(taskId);
                            break;
                        case "TASK_READY":// set status 6
                            taskId = Long.parseLong((String) requestHandler.getRequestAttributes().get(CMD_VALUE));
                            taskService.updateTaskStatus(account, taskId, command.getStatus());
                            message = message.append(getMessage("task.update", locale)).append(taskId);
                            break;
                        case "TASK_DEL":// set status 7
                            taskId = Long.parseLong((String) requestHandler.getRequestAttributes().get(CMD_VALUE));
                            taskService.updateTaskStatus(account, taskId, command.getStatus());
                            message = message.append(getMessage("task.update", locale)).append(taskId);
                            break;
                        case "MAIN_FILTER":// filter task
                            System.out.println(command.name());
                            TaskOutFilter taskOutFilter = account.getCurrentTasksFilter();
                            try {
                                //TODO null pointer ошибка при не выбранных статусах, перекинуть все строки в константы
                                String[] statusesGet = (String[]) requestHandler.getRequestAttributes().get("include_status");
                                Set<Integer> includeStatus = new HashSet<>(statusesGet.length);
                                for (String str : statusesGet) {
                                    includeStatus.add(Integer.parseInt(str));
                                }
                                taskOutFilter.setIncludeStatus(includeStatus);
                            } catch (IllegalArgumentException e) {
                                log.error(e, e);
                            }
                            String ask = (String) requestHandler.getRequestAttributes().get("ask");
                            if (ask == null) {
                                taskOutFilter.setAsk(true);
                            } else taskOutFilter.setAsk(false);
                            taskOutFilter.setSort(Integer.parseInt((String) requestHandler.getRequestAttributes().get("sorting_column")));
                            taskOutFilter.setTasksPerPage(Integer.parseInt((String) requestHandler.getRequestAttributes().get("task_per_page")));
                            taskService.updateTaskList(account);
                            break;
                        case "PAGE":
                            int pageNumber = Integer.parseInt((String) requestHandler.getRequestAttributes().get(CMD_VALUE));
                            account.getCurrentTasksFilter().setPage(pageNumber);
                            account = taskService.updateTaskList(account);
                            break;
                        default:
                            break;
                    }
                }
            } catch (LocServiceException e) {
                message = message.append(getMessage("task.update.false", locale));
                log.error(e, e);
            }
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            //TODO Exception на UI для простоты ;)
            message = message.append(e);
        }
        session.setAttribute(MESSAGE, message.toString());
        return page;
    }

    //    @RequestMapping(value = "/user/tasks/task/{taskId}", method = RequestMethod.POST)
    @RequestMapping(path = "/superior/tasks/assignment/{taskId}", method = RequestMethod.GET)
    public String superiorAssignmentTask(@PathVariable(value = "taskId") Long taskId, HttpSession session) {
//        System.out.println(taskId);
        String page = "taskAssignment";
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            Task task = taskService.getTask(account, taskId);
            if (task != null) {
                session.setAttribute(TASK, task);
                System.out.println(task);
                account.setCurrentTasks(null); //чистим коллекции, чтобы не таскать на страницу
//                    account.getUser().setTaskList(null);
                session.setAttribute("minuteList", minuteList);
                session.setAttribute("hourList", hourList);

                TaskOutFilter taskOutFilter = account.getCurrentTasksFilter();
                try {
                    //TODO null pointer ошибка при не выбранных статусах, перекинуть все строки в константы
                    Set<Integer> includeStatus = new HashSet<>();
                    includeStatus.add(taskService.statusTaskReport);
                    taskOutFilter.setIncludeStatus(includeStatus);
                } catch (IllegalArgumentException e) {
                    log.error(e, e);
                }
//                String ask = (String) requestHandler.getRequestAttributes().get("ask");
                //TODO поддержка обратной сортировки + сделать сохранение изначального фильтра
                String ask = null;
                if (ask == null) {
                    taskOutFilter.setAsk(true);
                } else taskOutFilter.setAsk(false);
                //TODO вынести в константы
                taskOutFilter.setSort(6);  //6 колонка даты сдачи - для сортировки по ней

//                taskOutFilter.setSort(Integer.parseInt((String) requestHandler.getRequestAttributes().get("sorting_column")));
//                taskOutFilter.setTasksPerPage(Integer.parseInt((String) requestHandler.getRequestAttributes().get("task_per_page")));
                taskService.updateTaskList(account);

            } else {
                System.out.println("task not found" + taskId);
            }

            //1 выводим таск и данные по нему
            //2 выводим список тасков в работу - фильтр

        } catch (Exception e) {
            log.error(e, e);
            //TODO redirect logout
        }
        return page;
    }

    @RequestMapping(path = "/superior/tasks/assignment/{taskId}", method = RequestMethod.POST)
    public String superiorAssignmentTask(@PathVariable(value = "taskId") Long taskId, HttpSession session,
                                         HttpServletRequest request, Locale locale, Integer hour, Integer minute, String taskAssignmentData) {

        //можно не передавать taskId
        StringBuilder message = new StringBuilder();
        Account account = null;
        String page = "taskAssignment";
        Task currentTask = null;
//        TASK_READY
        try {
            account = (Account) session.getAttribute(ACCOUNT); //TODO (ТЗ) вернуть в сессию уже введенные данные
            currentTask = (Task) request.getSession().getAttribute(TASK);

//            из формы приходит hour minute PAY_TASK
            //текущая дата формат 10/21/2016
            //TODO вынести текущую дату в константы
            Date assignmentDate;
//            Integer employeeInt = Integer.parseInt(employee);
            try {
                String uiDateFormat = "MM/dd/yyyy HH:mm:ss";//TODO вынести в константы
                int defaultSecond = 0; //00??
                assignmentDate = new SimpleDateFormat(uiDateFormat).
                        parse(taskAssignmentData.concat(" " + hour + ":" + minute + ":" + defaultSecond));

                if (assignmentDate.getTime() < (currentDate.getTime())) {
                    message.append(getMessage("task.incorrect.assignmentDate", locale));
                    throw new Exception(getMessage("task.incorrect.assignmentDate", locale));
                }
                //refresh Task
                try {
                    taskService.setAssignmentTaskTime(account, assignmentDate, currentTask, currentDate);
                    message.append(getMessage("task.set.time.reporting", locale)).append(currentTask.getTaskId());
                    session.setAttribute(TASK, currentTask);
                    //TODO выбрать страницу для редиректа
                    page = "redirect:/superior/tasks/task/".concat(currentTask.getTaskId() + "");
//                    System.out.println("addNewTask: " + newTask.getTaskId()); //для лога
                } catch (LocServiceException e) {
                    throw new Exception(e); //TODO подкорректировать
//                    log.error(e);
                }
            } catch (ParseException e) {
                throw new Exception(e);
            }
        } catch (Exception e) { //NumberFormatException, NullPointerException
            log.error(e, e);
            message.append(getMessage("task.incorrect.assignmentDate", locale));
        } finally {
            session.setAttribute(ACCOUNT, account);
            session.setAttribute(MESSAGE, message.toString());
        }

        return page;
    }

    @RequestMapping(path = "/superior/tasks/newtask", method = RequestMethod.GET)
    public String mainSuperiorNewTask(HttpSession session) {
        String page = "addNewTask";
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            if (account instanceof AccountSuperior) {
                System.out.println("**account instanceof AccountSuperior**");
                ((AccountSuperior) account).setEmployee(userService.getAllEmployee());
                for (User user : ((AccountSuperior) account).getEmployee()) {
                    System.out.println("user.getUserId()" + user.getUserId() + ": " + user.getLogin());
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return page;
    }

    @RequestMapping(value = "/superior/tasks/newtask", method = RequestMethod.POST)
    public String mainEmployeeNewTaskPost(Locale locale, HttpSession session, String titleTask,
                                          String bodyTask, String taskDeadline, String employee) {
        StringBuilder message = new StringBuilder();
        Account account = null;
        String page = "addNewTask";
        try {
            account = (Account) session.getAttribute(ACCOUNT); //TODO (ТЗ) вернуть в сессию уже введенные данные
            String strTaskDeadline = taskDeadline;             //текущая дата формат 10/21/2016
            Date bodyDeadline;
            Integer employeeInt = Integer.parseInt(employee);
            try {
                String uiDateFormat = "MM/dd/yyyy HH:mm:ss";
                int defaultHour = 20;
                int defaultMinute = 15;
                int defaultSecond = 0; //00??
                bodyDeadline = new SimpleDateFormat(uiDateFormat).
                        parse(strTaskDeadline.concat(" " + defaultHour + ":" + defaultMinute + ":" + defaultSecond));
                if (bodyDeadline.getTime() < (new Date().getTime())) {
                    message.append(getMessage("task.incorrect.deadline", locale));
                    throw new Exception(getMessage("task.incorrect.deadline", locale));
                }
                try {
                    Task newTask = taskService.addNewTask(account, employeeInt, titleTask, bodyTask, bodyDeadline);
                    message.append(getMessage("message.task.add", locale)).append(newTask.getTaskId());
                    session.setAttribute(TASK, newTask);
                    page = "redirect:/superior/tasks/task/".concat(newTask.getTaskId() + "");
                    System.out.println("addNewTask: " + newTask.getTaskId()); //для лога
                } catch (LocServiceException e) {
//                    throw new Exception(e);
                    log.error(e);
                }
            } catch (ParseException e) {
                throw new Exception(e);
            }
        } catch (Exception e) { //NumberFormatException, NullPointerException
            log.error(e, e);
            message.append(getMessage("message.task.add.false", locale));
        } finally {
            session.setAttribute(ACCOUNT, account);
            session.setAttribute(MESSAGE, message.toString());
        }
        return page;
    }

    @RequestMapping(value = "/superior/tasks/task/{taskId}", method = RequestMethod.GET)
    public String mainSuperiorTask(@PathVariable(value = "taskId") Long taskId, HttpSession session, Locale locale) {
        StringBuilder message = new StringBuilder();
        String page = "taskDetail";
        try {
            Account account = (Account) session.getAttribute(ACCOUNT);
            try {
                Task task = taskService.getTask(account, taskId);
                if (task != null) {
                    session.setAttribute(TASK, task);
                    account.setCurrentTasks(null); //чистим коллекции, чтобы не таскать на страницу
//                    account.getUser().setTaskList(null);
                }
                System.out.println(task);
                System.out.println(task.getDateCreation());
            } catch (LocServiceException e) {
                message.append(getMessage("task.detail.false", locale));
            }
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            message.append(getMessage("task.detail.false", locale));
            page = "redirect:/superior/tasks";
        }
        session.setAttribute(MESSAGE, message.toString());
        return page;
    }

    @RequestMapping(value = "/superior/tasks/task/{taskId}", method = RequestMethod.POST)
    public String mainSuperiorTaskCorrect(HttpSession session, Locale locale, String bodyTask) {
//        System.out.println(taskId);
        StringBuilder message = new StringBuilder();
        String page = "taskDetail";
        Account account;
        try {
            account = (Account) session.getAttribute(ACCOUNT);
            Task task = (Task) session.getAttribute(TASK);
//            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            try {
                taskService.updateTaskBody(account, task, bodyTask);
                message.append(getMessage("task.update", locale)).append(task.getTaskId());
                log.info("command update task:" + task.getTaskId());
            } catch (LocServiceException e) {
                message.append(getMessage("task.update.false", locale));
//                content.getSessionAttributes().put(POST_BODY, bodyTask); //TODO вывести на UI в случае неудачи
            }
            session.setAttribute(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            message.append(getMessage("task.update.false", locale));
            message = message.append(e); //TODO убрать
            page = "redirect:/superior/tasks";
        }
        session.setAttribute(MESSAGE, message.toString());
        return page;
    }

    //        return "indexTest";
    private String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    private Map<Integer, String> getStatuses(Locale locale) {
        int i = 1;
        Map<Integer, String> statusMap = new HashMap<>(6);
        while (i < 7) {
            statusMap.put(i, taskStatuses.getMessage("" + i, null, locale));
            System.out.println();
            i++;
        }
        return statusMap;
    }


    //                page ="redirect:/user";
    @RequestMapping(value = "/login-fail", method = RequestMethod.GET)
    @RequestWrapper
    public String loginFail(ModelAndView model, @RequestParam(value = "login-fail") String error) {
        if ("error".equals(error)) {
            model.addObject("error", "Authentication error");
        }

        return "login";
    } //                page ="redirect:/user";

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @RequestWrapper
    public String loginFail(HttpSession session) {
        session.invalidate();
        return "login";
    }


    //    не используется ??
//    @RequestMapping(path = "/taskDetail", method = RequestMethod.POST)
//    public String main(HttpSession session, Locale locale) {
//        StringBuilder message = new StringBuilder();
//        String page = null;
//        try {
//            Account account = (Account) session.getAttribute(ACCOUNT);
//            Long taskId = Long.parseLong((String) session.getAttribute(CMD_VALUE));
//            try {
//                Task task = taskService.getTask(account, taskId);
//                if (task != null) {
//                    session.setAttribute(TASK, task);
//                    account.setCurrentTasks(null); //чистим коллекции, чтобы не таскать на страницу
//                    account.getUser().setTaskList(null);
////                page = PageMapper.getPageMapper().getTaskDetailsPage(account.getUser().getRole());
//                    page = "taskDetail";
//                    session.setAttribute(ACCOUNT, account);
//                }
//            } catch (LocServiceException e) {
////            message = message.append(MessageManager.getProperty("task.detail.false"));
//                message.append(getMessage("task.detail.false", locale));
//
//                //TODO не удалось получить детали таска, пользователя оставить на той же странице
////				System.out.println("TaskDetailsCommand не удалось получить детали таска");
////            page = PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
//                if (account.getUser().getRole() == employeeRole) {
//                    page = "user";
//                } else if (account.getUser().getRole() == superiorRole) {
//                    page = "superior";
//                }
//            }
//        } catch (Exception e) {
//            log.error(e, e);
//            message.append(getMessage("task.detail.false", locale));
//        }
//        session.setAttribute(MESSAGE, message.toString());
//        return page;
//    }


}
//        System.out.println(model.getModel());
//        System.out.println(model.getViewName());
//        System.out.println(model.getView());

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


//    пример рест запроса
//    @RequestMapping("/user/tasks/task/dfsf/{taskId}")
////    @RequestMapping("/user/tasks/task?taskId=${taskId}/{someID}")
////    public @ResponseBody int getAttr(@PathVariable(value="taskId") String id, String task,ModelAndView modelAndView) {
//    public @ResponseBody int getAttr(@PathVariable(value="taskId") String id, String task,ModelAndView modelAndView) {
//        System.out.println("********************");
//        System.out.println(task);
//        System.out.println(id);
//        System.out.println("********************");
//        modelAndView.setViewName("redirect:user");
////        redir.addFlashAttribute("USERNAME",uname);
//        return 201;
////        return "user";
//    }

