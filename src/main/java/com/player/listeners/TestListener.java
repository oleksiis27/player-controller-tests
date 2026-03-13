package com.player.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("TEST STARTED: {}.{} [Thread: {}]",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                Thread.currentThread().getId());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("TEST PASSED: {}.{} ({}ms)",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("TEST FAILED: {}.{} — {}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("TEST SKIPPED: {}.{}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }
}