package nz.co.tmsandbox.pages;


import nz.co.tmsandbox.webinteractivities.MasterPage;

public enum SearchPage {

    btnFilter(MasterPage.findingBy.xpath, "//button[contains(text(),'{Filter}')]"),
    lblCatergoryRefiner(MasterPage.findingBy.xpath, "//span[contains(text(),'{category}')]"),
    ddlRegion(MasterPage.findingBy.css, "select[class*='__select ng']"),

    btnResults(MasterPage.findingBy.xpath,"//button[contains(text(),'results')]"),
    lblFirstResult(MasterPage.findingBy.css, "tg-col[order='0'] a"),
    lblListing(MasterPage.findingBy.css, "h3[class*='tm-search-header-result-count__heading']"),
    lbladdress(MasterPage.findingBy.css, "h1[class*='location']"),
    lblprice(MasterPage.findingBy.css, "h2[class*='price']"),
    lblAttribute(MasterPage.findingBy.xpath, "//div[contains(text(),'{Att}')]"),
    lblAgent(MasterPage.findingBy.css, "div[class*='seller-contact'] h3 span");


    private final String value;

    SearchPage(MasterPage.findingBy byMethod, String byValue) {
        value = byMethod.toString() + ";" + byValue + ";" + getDeclaringClass().toString().replace("class ", "") + "." + name();
    }

    public String get() {
        return value;
    }
}
