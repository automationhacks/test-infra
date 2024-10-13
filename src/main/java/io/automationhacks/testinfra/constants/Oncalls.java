package io.automationhacks.testinfra.constants;

public enum Oncalls {
    UNASSIGNED("No oncall assigned", "", ""),
    // Group
    TEST_INFRA("test infra", "testinfra", "automationhacks@gmail.com"),
    // Individual users
    AUTOMATION_HACKS("automation hacks", "U07NYLPTGDV", "automationhacks@gmail.com"),
    DISHA("Disha K", "U07R5HV1JUX", ""),
    RACHIT("Rachit M", "U07RLCB9FBM", ""),
    ROJA("Roja G", "U07RYQMNXEV", "");

    private final String alias;
    private final String slackId;
    private final String email;

    Oncalls(String alias, String slackId, String email) {
        this.alias = alias;
        this.slackId = slackId;
        this.email = email;
    }

    public String getSlackId() {
        return slackId;
    }

    public String getEmail() {
        return email;
    }

    public String getAlias() {
        return alias;
    }
}
