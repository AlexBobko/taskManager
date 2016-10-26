package command;

public enum CommandList {
    LOGIN {
        {
            this.command = new LoginUserCommand();
        }
    },
    LOGOUT {
        {
            this.command = new LogoutUserCommand(); //
        }
    },
    TASK_DEL {
        {
            this.command = new DeleteTaskCommand();
        }
    },
    TASK_UPDATE {
        {
            this.command = new UpdateTaskCommand();
        }
    },
    TASK_LIST {
        {
            this.command = new ListTaskCommand();
        }
    },
    TASK_ADD {
        {
            this.command = new TaskAddCommand(); // add task to BD
        }
    },
    VIEW_HISTORY {
        {
            this.command = new ViewTaskHistoryCommand(); // TODO история видна в подробном описании
        }
    },
    TASK_DETAIL {
        {
            this.command = new ViewTaskDetailCommand(); // детали, подробное описание
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
    GO_ADD{
        {
            this.command = new GoAddCommand(); // go page add task //TODO перенести в гет
        }
    },
    GO_TASK_LIST{
        {
            this.command = new GoTaskListCommand(); // go page add task //TODO перенести в гет
        }
    },
    TASK_CORRECT {
        {
            this.command = new TaskCorrectCommand(); // set status 6
        }
    },
    TASK_READY {
        {
            this.command = new TaskReadyCommand(); // set status 6
        }
    };

    ICommand command;

    public ICommand getCurrentCommand() {
        return command;
    }
}
