Feature: Perform a property search and count distinct car names

  As a tester
  I want to search house in Wellington on TradeMe
  So that I can get the number of listing
  and verify the details of the first listing.

  Rule://

    Background:
      Given set the file location

    Scenario Outline: Search and verify the details of the first property
      When load "trademe" website
      And search with "<searchKey>"
      And select "<category>" and "<location>"
      Then verify the number of listing is "284"
      When open the first item
      Then verify the "<address>", "<price>","<beds>", "<baths>" and "<agent>"

      @test
      Examples:
        | searchKey | category          | location   | price         | beds  | baths  | agent             | address                                           |
        | house     | Trade Me Property | Wellington | $600 per week | 1 Bed | 1 Bath | Quinovic Takapuna | 511/9 Byron Avenue, Taita, Lower Hutt, Wellington |


    @test
    Scenario: verify the number of the named car brands available

      When get the number of the named "car" brands available
      Then verify the count of unique cars is "86"

