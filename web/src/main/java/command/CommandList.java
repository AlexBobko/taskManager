package command;

public enum CommandList {
    LOGIN {
        {
            this.command = new LoginUserICommand();
        }
    },
    LOGOUT {
        {
            this.command = new LogoutUserCommand();
        }
    },
    TASK_ADD {
        {
            this.command = new TaskAddCommand(); // add task to BD
        }
    },
    TASK_UPDATE {
        {
            this.command = new UpdateTaskCommand(); //task add correction
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
            this.command = new ViewTaskDetailCommand(); //детали, подробное описание //TODO перенести в GET?
        }
    },
    GO_ADD{
        {
            this.command = new GoAddCommand(); // go page add task //TODO перенести в GET
        }
    },
    GO_TASK_LIST{
        {
            this.command = new GoTaskListCommand(); // go page add task //TODO перенести в GET
        }
    },
    PAGE {
        {
            this.command = new PageCommand(); //пагинация
        }
    },
    TASK_DEL {
        {
            this.command = new DeleteTaskCommand(); // таски не удаляются, только корректируются
        }
    };

    ICommand command;

    public ICommand getCurrentCommand() {
        return command;
    }
}
