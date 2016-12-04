package controller;

import lombok.extern.log4j.Log4j;
import managers.PageManager;
import loc.task.service.UserService;

@Log4j
public class PageMapper {
    static final Integer employeeRole = UserService.employeeRole;
    static final Integer superiorRole = UserService.superiorRole;
    private static PageMapper pageMapper = null;

    private PageMapper() {
    }

    private static synchronized PageMapper getInstance() {
        if (pageMapper == null) {
            pageMapper = new PageMapper();
        }
        return pageMapper;
    }

    public static PageMapper getPageMapper() {
        if (pageMapper == null) {
            return getInstance();
        }
        return pageMapper;
    }

    public String getTaskListPage(Integer role) {
        String page = null;
        if (role == employeeRole) {
            page = PageManager.getProperty("path.page.user");
        } else if (role == superiorRole) {
            page = PageManager.getProperty("path.page.superior");
        }
        return page;
    }

    public String getTaskDetailsPage(Integer role) {
        String page = null;
        if (role == employeeRole) {
            page = PageManager.getProperty("path.page.task");
        } else if (role == superiorRole) {
            //TODO добавить: возм. установить любой статус, вкл. удаление, переназначить ответственного и даты.
            //расширить возможности редактирования
            page = PageManager.getProperty("path.page.task");
        }
        return page;

    }

    public String getNewTaskPage(Integer role) {
        String page = null;
        if (role == employeeRole) {
            page = PageManager.getProperty("path.page.add.task");
        } else if (role == superiorRole) {
            //TODO добавить: возм. установить любой статус, вкл. удаление, переназначить ответственного и даты.
            //расширить возможности добавления
            page = PageManager.getProperty("path.page.add.task");
        }
        return page;

    }

}
