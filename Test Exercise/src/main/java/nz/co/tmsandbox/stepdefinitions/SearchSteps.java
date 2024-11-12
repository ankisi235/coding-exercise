package nz.co.tmsandbox.stepdefinitions;

import io.cucumber.java8.En;
import nz.co.tmsandbox.pages.HomePage;
import nz.co.tmsandbox.pages.SearchPage;
import nz.co.tmsandbox.webinteractivities.IWebActions;


public class SearchSteps implements En {


    public SearchSteps() {

        When("^search with \"([^\"]*)\"$", (String searchKey) -> {
            IWebActions.set.enterValueInTextbox(searchKey, HomePage.txtSearch.get());
            IWebActions.click.button(HomePage.btnSearch.get());
        });

        And("^select \"([^\"]*)\" and \"([^\"]*)\"$", (String category, String location) -> {
            IWebActions.click.button(SearchPage.btnFilter.get().replace("{Filter}", "Category"))
                    .button(SearchPage.lblCatergoryRefiner.get().replace("{category}", category));
            IWebActions.click.button(SearchPage.btnFilter.get().replace("{Filter}", "Locations"));
            IWebActions.select.valueFromDropdown(location, SearchPage.ddlRegion.get());
            IWebActions.click.button(SearchPage.btnResults.get());
        });

        Then("^verify the number of listing is \"([^\"]*)\"$", (String numberOfListing) -> {
            String result = IWebActions.get.getTextOf(SearchPage.lblListing.get());
            IWebActions.validate.assertEquals(result.substring(result.indexOf(" "), result.indexOf("r") - 1).trim(), numberOfListing, "Number of results");
        });

        When("^open the first item$", () -> {
            IWebActions.click.button(SearchPage.lblFirstResult.get());
        });

        Then("^verify the \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$", (String address, String price,
                                                                                                     String beds, String baths, String agent) -> {
            IWebActions.validate.assertEquals(IWebActions.get.getTextOf(SearchPage.lbladdress.get()), address, "Address");
            IWebActions.validate.assertEquals(IWebActions.get.getTextOf(SearchPage.lblprice.get()), price, "Price");
            IWebActions.validate.contains(beds, IWebActions.get.getTextOf(SearchPage.lblAttribute.get().replace("{Att}", "Bed")).trim(), "Beds");
            IWebActions.validate.contains(baths, IWebActions.get.getTextOf(SearchPage.lblAttribute.get().replace("{Att}", "Bath")).trim(), "Baths");
            IWebActions.validate.assertEquals(IWebActions.get.getTextOf(SearchPage.lblAgent.get()), agent, "Agent");

        });

    }
}
