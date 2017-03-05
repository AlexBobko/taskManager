package loc.task.command;

public enum CommandList {
    LOGIN {
        {
            this.command = "new LoginUserCommand()";
        }
    },
    LOGOUT {
        {
            this.command = "new LogoutUserCommand()";
        }
    },
    TASK_ADD {
        {
            this.command = "new TaskNewAddCommand()"; // add task to BD
        }
    },
    TASK_UPDATE {
        {
            this.command = "new TaskUpdateCommand()"; //task add correction
        }
    },
    APPROVE_TASK {
        {
            this.status = 2; // set status 2
        }
    },
    PRODUCTION {
        {
            this.status = 3; // set status 3
        }
    },
    FOR_CHECKING {
        {
            this.status = 4; // set status 4
        }
    },
    PAY_TASK {
        {
            this.status = 5; // set status 5
        }
    },
    TASK_READY {
        {
            this.status = 6; // set status 6
        }
    },
    TASK_DEL {
        {
            this.status = 7; // таски не удаляются, только корректируются
        }
    },
    TASK_DETAIL {
        {
            this.command = "new TaskDetailsCommand()"; //детали, подробное описание //TODO перенести в GET?
        }
    },
    GO_ADD{
        {
            this.command = "new TaskNewCommand()"; // go page add task //TODO перенести в GET
        }
    },
    GO_TASK_LIST{
        {
            this.command = "new TaskListCommand()"; // go page add task //TODO перенести в GET
        }
    },
    PAGE {
        {
            this.command = "new TaskGetPageCommand()"; //пагинация
        }
    },
    MAIN_FILTER {
        {
            this.command = "new TaskFilterCommand()"; // фильтрация основная
        }
    };

    String command;

    public Integer getStatus() {
        return status;
    }

    Integer status;

    public String getCurrentCommand() {
        return command;
    }
}
