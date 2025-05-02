# Plan

## Developer Prompt

As a Java programmer, I want you to implement a developer productivity feature.

I want you to be able to connect to report portal instance running on localhost:8080 on this machine and figure out the reason for a failing test from the stack trace and then suggest a fix for this in the tests under `src/test/java/io/automationhacks/testinfra/reqres` package.

This should be built in a scalable manner such that this auto analysis can run on every test suite run or at a scheduled time.

Report portal has an MCP server here:
<https://github.com/reportportal/reportportal-mcp-server>

API docs can be found here
<https://developers.reportportal.io/api-docs/service-api/get-test-items>

## Plan

1. **Understand the Requirements**:
   - Connect to a Report Portal instance running on `localhost:8080`.
   - Analyze failing test cases from the stack trace.
   - Suggest fixes for failing tests in the `src/test/java/io/automationhacks/testinfra/reqres` package.
   - Ensure the solution is scalable to run automatically on every test suite run or at a scheduled time.

2. **Research and Setup**:
   - Review the Report Portal MCP server documentation and API endpoints.
   - Identify the API endpoints required to fetch test results and stack traces.
   - Set up a connection to the Report Portal instance.

3. **Implementation Plan**:
   - **Step 1**: Create a utility class to interact with the Report Portal API.
     - Fetch test results and stack traces for failing tests.
   - **Step 2**: Implement a stack trace analyzer to parse the stack trace and identify potential issues.
   - **Step 3**: Suggest fixes for failing tests based on the analysis.
   - **Step 4**: Integrate the solution to run automatically:
     - On every test suite run.
     - At a scheduled time using a scheduler (e.g., Quartz Scheduler or Java's `ScheduledExecutorService`).

4. **Documentation**:
   - Update `ai_changes/plan.md` with the plan and developer prompt.
   - Update `ai_changes/changelog.md` with changes made and timestamps.

5. **Testing**:
   - Test the solution with a mock Report Portal instance.
   - Validate the analysis and suggestions for failing tests.

6. **Scalability**:
   - Ensure the solution is modular and extensible for future enhancements.
