package command;

public enum CommandList {
    LOGIN {
        {
            this.command = new LoginUserCommand();
        }
    },
    LOGOUT {
        {
            this.command = new LogoutUserCommand();
        }
    },
    TASK_ADD {
        {
            this.command = new TaskNewAddCommand(); // add task to BD
        }
    },
    TASK_UPDATE {
        {
            this.command = new TaskUpdateCommand(); //task add correction
        }
    },
    APPROVE_TASK {
        {
            this.command = new TaskInApproveCommand(); // set status 2
        }
    },
    PRODUCTION {
        {
            this.command = new TaskInProductionCommand(); // set status 3
        }
    },
    FOR_CHECKING {
        {
            this.command = new TaskInCheckingCommand(); // set status 4
        }
    },
    PAY_TASK {
        {
            this.command = new TaskInPayCommand(); // set status 5
        }
    },
    TASK_READY {
        {
            this.command = new TaskReadyCommand(); // set status 6
        }
    },
    TASK_DETAIL {
        {
            this.command = new TaskDetailCommand(); //детали, подробное описание //TODO перенести в GET?
        }
    },
    GO_ADD{
        {
            this.command = new TaskNewCommand(); // go page add task //TODO перенести в GET
        }
    },
    GO_TASK_LIST{
        {
            this.command = new TaskListCommand(); // go page add task //TODO перенести в GET
        }
    },
    PAGE {
        {
            this.command = new TaskGetPageCommand(); //пагинация
        }
    },
    TASK_DEL {
        {
            this.command = new TaskDeleteCommand(); // таски не удаляются, только корректируются
        }
    },
    MAIN_FILTER {
        {
            this.command = new TaskFilterCommand(); // фильтрация основная
        }
    };

    ICommand command;

    public ICommand getCurrentCommand() {
        return command;
    }
}
