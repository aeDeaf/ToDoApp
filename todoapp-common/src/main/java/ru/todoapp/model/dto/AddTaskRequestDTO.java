package ru.todoapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Add task request.
 * Allows for user to save new planned tasks for themselves.
 */
@Getter
@NoArgsConstructor
public class AddTaskRequestDTO extends RequestDTO {
    /**
     * task description/name aka whatever the user is planning to do
     */
    private String description;

    /**
     * datetime in ISO 8601 format (yyyy-MM-ddThh:mm:ss+hh)
     */
    private String datetime;

    public AddTaskRequestDTO(String requestUUID, String userUUID, String description, String datetime) {
        super(requestUUID, userUUID, RequestType.ADD_TASK);
        this.description = description;
        this.datetime = datetime;
    }
}