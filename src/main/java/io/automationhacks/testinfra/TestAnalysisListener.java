package io.automationhacks.testinfra;

import org.testng.IExecutionListener;

import java.util.logging.Logger;

public class TestAnalysisListener implements IExecutionListener {
    private static final Logger logger = Logger.getLogger(TestAnalysisListener.class.getName());
    private final AutoTestAnalyzer analyzer;

    public TestAnalysisListener() {
        this.analyzer = new AutoTestAnalyzer();
    }

    @Override
    public void onExecutionFinish() {
        logger.info("Test execution finished. Starting test analysis...");
        analyzer.analyzeFailingTests();
    }
}
