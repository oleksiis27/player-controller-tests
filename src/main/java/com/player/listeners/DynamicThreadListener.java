package com.player.listeners;

import com.player.config.AppConfig;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

import java.util.List;

public class DynamicThreadListener implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {
        int threadCount = Integer.parseInt(
                System.getProperty("thread.count",
                        String.valueOf(AppConfig.getInstance().threadCount())));

        for (XmlSuite suite : suites) {
            suite.setThreadCount(threadCount);
        }
    }
}