package Module;

import java.io.Serializable;

/**
 * Класс enum связанный с командами
 * @autor Svytoq
 * @version 1.0
 */
public enum Command implements Serializable {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    SAVE("save"),
    EXECUTE_SCRIPT("execute_script"),
    EXIT("exit"),
    ADD_IF_MAX("add_if_max"),
    ADD_IF_MIN("add_if_min"),
    REMOVE_GREATER("remove_greater"),
    COUNT_LESS_THAN_OWNER("count_less_than_owner"),
    PRINT_DESCENDING("print_descending"),
    PRINT_FIELD_ASCENDING_OWNER("print_field_ascending_owner"),
    SIGN("sign"),
    REG("reg"),
    INVALID_COMMAND("invalid_command");

    private final String commandName;

    private Command(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return this.commandName;
    }
}
