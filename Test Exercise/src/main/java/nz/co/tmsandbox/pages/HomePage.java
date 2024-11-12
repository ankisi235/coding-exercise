package nz.co.tmsandbox.pages;


import nz.co.tmsandbox.webinteractivities.MasterPage;

public enum HomePage {

    txtSearch(MasterPage.findingBy.css, "#search"),
    btnSearch(MasterPage.findingBy.css,"button[type='Submit']"),;


    private final String value;

    HomePage(MasterPage.findingBy byMethod, String byValue) {
        value = byMethod.toString() + ";" + byValue + ";" + getDeclaringClass().toString().replace("class ", "") + "." + name();
    }

    public String get() {
        return value;
    }
}
