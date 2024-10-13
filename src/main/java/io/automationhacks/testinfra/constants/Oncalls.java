package io.automationhacks.testinfra.constants;

public enum Oncalls {
    UNASSIGNED("", ""),
    // Group
    TEST_INFRA("testinfra", "automationhacks@gmail.com"),
    // Individual users
    ENGINEER_AH("U07NYLPTGDV", "automationhacks@gmail.com"),
    ENGINEER_D("U07R5HV1JUX", ""),
    ENGINEER_R("U07RLCB9FBM", ""),
    ENGINEER_RA("U07RYQMNXEV", "");

    private final String slackId;
    private final String email;

    Oncalls(String slackId, String email) {
        this.slackId = slackId;
        this.email = email;
    }

    public String getSlackId() {
        return slackId;
    }

    public String getEmail() {
        return email;
    }
}
