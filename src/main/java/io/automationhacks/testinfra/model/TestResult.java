package io.automationhacks.testinfra.model;

import io.automationhacks.testinfra.constants.Oncalls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class TestResult {
    private String testClass;
    private String name;
    private int status;
    private String errorMessage;
    private Date timestamp;
    private Oncalls onCall;
    private String functionalFlow;
    private String serviceMethod;
}
