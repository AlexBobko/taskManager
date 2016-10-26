package service;

import dao.TaskDAO;
import dao.TaskMetaDAO;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import utDao.PoolConnection;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskService {
    private TaskDTO currentTask;
    private TaskMetaDTO currentTaskMeta;

    public TaskService(String titleTask, String bodyTask, String strTaskDeadline, int responsiblePersonId) {
        GregorianCalendar taskDeadline = convertDate(strTaskDeadline);
        //TODO ?? перенести проверку даты в комманд, утилита
        if (taskDeadline != null) {
            TaskDTO newTask = new TaskDTO(titleTask, bodyTask, taskDeadline);
            TaskMetaDTO newTaskMeta = new TaskMetaDTO(0, responsiblePersonId, 1); // int taskId, int userId, int statusId
            if (addNewTask(newTask, newTaskMeta)) {
                this.currentTask = newTask;
                this.currentTaskMeta = newTaskMeta;
                System.out.println("addNewTask: " + newTask.getId());
            } else {
                this.currentTask = null;
                this.currentTaskMeta = null;
            }
        }
    }

    public TaskService() {

    }

    public TaskService(TaskDTO task, TaskMetaDTO taskMeta, SimpleDateFormat dateFormat) {
        this.currentTask = task;
        this.currentTaskMeta = taskMeta;

    }


    private boolean addNewTask(TaskDTO newTask, TaskMetaDTO newTaskMeta) {
        boolean b = false;
        int id = 0;
        TaskDAO taskDao = null;
        TaskMetaDAO metaDao = null;
        Connection connection = null;
        try {
            connection = PoolConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            taskDao = new TaskDAO(connection);
            id = taskDao.create(newTask);
            newTaskMeta.setTaskId(id);
            if (id != 0) {
                metaDao = new TaskMetaDAO(connection);
                if (metaDao.create(newTaskMeta) != 0) {
                    connection.commit();
                    b = true;
                    System.out.println("new TaskDAO id: " + id);
                }
            } else {
                connection.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);// TODO (почитать) необходимость закрытия
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return b;
    }

    protected GregorianCalendar convertDate(String postDate) {
        String[] dl = postDate.split("/");
        // GregorianCalendar.set(year, month, date, hrs, min) 10/21/2016
        int year = Integer.parseInt(dl[2]);
        int month = Integer.parseInt(dl[0]);
        int dayOfMonth = Integer.parseInt(dl[1]);
        int hourOfDay = 20;
        int minute = 15;
        GregorianCalendar bodyDeadline = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute);
        return bodyDeadline;
    }

    public boolean updateTaskMeta(TaskDTO task, TaskMetaDTO meta, SimpleDateFormat dateFormat) {
        boolean b = false;
        TaskMetaDAO metaDao;
        TaskDAO taskDao;
        Connection connection = null;
        try {
            connection = PoolConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            metaDao = new TaskMetaDAO(connection);
            b = metaDao.updateStatus(meta);
            if (b) {
                Calendar calendar = Calendar.getInstance();
//				SimpleDateFormat dateFormat = Account.dateFormat;
                // System.out.println("** AddNewTaskComandImpl.convertDate ** date: " + dateFormat.format(bodyDeadline.getTime()));
                StringBuffer history = task.getHistory();
                history.append(dateFormat.format(calendar.getTime())).append("~status:").append(meta.getStatusId()).append("~~");
                taskDao = new TaskDAO(connection);
                b = taskDao.updateHistory(task);
                if (b) {
                    TaskDTO taskDTO = taskDao.update(task);
                    b = (taskDTO != null);
                }
            }
            if (b) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); //актуальность удалить и проверить
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public boolean updateTaskBody(TaskDTO task, TaskMetaDTO meta, SimpleDateFormat dateFormat) {
        boolean b = false;
        TaskMetaDAO metaDao;
        TaskDAO taskDao;
        Connection connection = null;
        try {
            connection = PoolConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            metaDao = new TaskMetaDAO(connection);
            b = metaDao.updateStatus(meta);
            if (b) {
                Calendar calendar = Calendar.getInstance();
//				SimpleDateFormat dateFormat = Account.dateFormat;
                // System.out.println("** AddNewTaskComandImpl.convertDate ** date: " + dateFormat.format(bodyDeadline.getTime()));
                StringBuffer history = task.getHistory();
                history.append(dateFormat.format(calendar.getTime())).append("~status:").append("~korrect body").append(meta.getStatusId()).append("~~");
                taskDao = new TaskDAO(connection);
//                b = taskDao.updateHistory(task);
                if (b) {
                    TaskDTO taskDTO = taskDao.update(task);
                    b = (taskDTO != null);
                }
            }
            if (b) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); //актуальность удалить и проверить
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public TaskDTO getCurrentTask() {
        return currentTask;
    }

    public TaskMetaDTO getCurrentTaskMeta() {
        return currentTaskMeta;
    }
}
