package org.jbox2d.testbed.framework;

public class ListItem {
    public String category;
    public TestbedTest test;

    public ListItem(String argCategory) {
        category = argCategory;
    }

    public ListItem(TestbedTest argTest) {
        test = argTest;
    }

    public boolean isCategory() {
        return category != null;
    }

    @Override
    public String toString() {
        return isCategory() ? category : test.getTestName();
    }
}