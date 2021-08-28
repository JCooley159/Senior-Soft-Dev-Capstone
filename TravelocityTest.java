import org.jetbrains.annotations.NotNull;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TravelocityTest
{


    private static WebDriver driver;
    private static ArrayList<String> destinations;
    private static ArrayList<Ticket> tickets;



    @BeforeClass
    public static void loadIn()
    {
        destinations = new ArrayList<String>();
        tickets = new ArrayList<Ticket>();

        ///////////////////////////////      JUST FOR TESTING
//        destinations.add("Cancun");
//        destinations.add("Las Vegas");
        destinations.add("Denver");
        destinations.add("Rome");
        destinations.add("Milan");
        destinations.add("Paris");
        destinations.add("Madrid");
//        destinations.add("Amsterdam");
//        destinations.add("Singapore");


        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        options.addArguments("disable-popup-blocking");
        options.addArguments("test-type");
        options.addArguments("start-maximized");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\JCool\\Downloads\\chromedriver_win32y\\chromedriver.exe");
        //        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kaise\\Documents\\chromedriver.exe");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        driver.get("https://www.travelocity.com/");
//





        ///////////////      DESTINATION  LOOP        /////////////////////
        try
        {
            // Create an PrintWriter for file CheapestTickets.dat
            FileWriter fw = new FileWriter("CheapestTickets.txt");


            for (String dest : destinations)
            {
                //  Pass the destination and the FileWriter to dateRangeLoop() to
                //  find the cheapest Ticket, print it, and write it to the file.
                Ticket cheapestTicketForDest = dateRangeLoop(dest, fw);

                //  Add the Ticket to the ArrayList
                tickets.add(cheapestTicketForDest);
            }

            // Close the FileWriter & Driver once each destination has a ticket
            fw.close();
            driver.close();
        }
        catch (IOException e)
        {
//            System.out.println("Problem saving ticket to file!");
        }
    }



    @Test
    public void testTicketsExist()
    {
        Assert.assertTrue("Checks to make sure the list of cheapest tickets is not empty", tickets.size() > 0);
    }


    @Test
    public void testEachDestinationHasTicket() // throws NoSuchElementException, NullPointerException
    {
        Assert.assertEquals("Tests to make sure a cheapest ticket is found for each destination. " +
                 "Automatically fails if the program never completes the loops through the destinations",
                 destinations.size(), tickets.size());
    }


    @Test
    public void testPrintTickets()
    {
        for ( Ticket t : tickets )
        {
            System.out.println(t.toString());
        }
    }


    @Test
    public void testReadTicketsFromFile()
    {
        String result = readTicketsFromFile();
        Assert.assertEquals("Should return a string once it hits an error or all lines in the file have been read",
                "File printed to console!", result);
    }


    /**
     * @param dest              Destination for the Flight in question
     * @param fw                File Writer to record the cheapest Ticket with
     * @return                  A Ticket describing the best dates to leave for and return from a week long vacation.
     * @throws IOException      If the FileWriter cannot write the ticket for some reason.
     */
    private static Ticket dateRangeLoop(String dest, FileWriter fw) throws IOException {
        //  currentCheapestPrice gets initialized as an unrealistically large number
        double currentCheapestPrice = 10000;
        double cheapestPrice = 9999;
        Ticket cheapestTicketForDest = new Ticket();



        ///////////////      MONTH LOOP        /////////////////////
        for (int startMonth = 6; startMonth <= 8; startMonth++)
        {

            // sets the # of days in each month and thus, the day loop's # of iterations
            int endMonth;
            int endDay;
            int monthMax;
//            if(startMonth == 5 || startMonth == 7) { monthMax = 31; }
            if(startMonth == 7) { monthMax = 31; }
            else { monthMax = 30; }



            ///////////////       DAY LOOP        /////////////////////
            for (int startDay =1; startDay <= monthMax; startDay++)
            {

                if (startDay + 6 >= monthMax)
                {
                    endDay = (startDay + 6) - monthMax;
                    endMonth = startMonth + 1;
                }
                else
                {
                    endDay = startDay + 6;
                    endMonth = startMonth;
                }



                ///////////////       Finds cheapest price for a given range        /////////////////////
                try
                {

                    cheapestPrice = getPriceForRange(dest, startMonth, startDay, endMonth, endDay);

                }
                catch (org.openqa.selenium.NoSuchElementException e)
                {
                    ///////////  Catches Element Timeout and Writes the last ticket to the File  ////////
                    String lastTicketStr = "\nThe last ticket I found before crashing was to  " + dest +
                            "  for $" + cheapestPrice +
                            "  from " + startMonth + "/" + startDay + "/2019" +
                            "  to  " + endMonth + "/" + endDay + "/2019\n";

                    System.out.println( lastTicketStr );
                    fw.write( lastTicketStr );
                    // e.printStackTrace();
                }


                //////////////   Test to see if the price for the given range is cheaper than the currentCheapestPrice

                if (cheapestPrice < currentCheapestPrice)
                {
                    currentCheapestPrice = cheapestPrice;

                    cheapestTicketForDest = new Ticket(dest, cheapestPrice, startMonth, startDay, endMonth, endDay);

                    System.out.println(
                            "Congrats! We found you a flight to  " + dest +
                            "  for $" + currentCheapestPrice +
                            "  from " + startMonth + "/" + startDay + "/2019" +
                            "  to  " + endMonth + "/" + endDay + "/2019");
                }


                // Break when the endDate is the 15th of August
                if(endMonth == 8 && endDay == 15) { break; }


            } ///////////////       END of DAY LOOP              /////////////////////

        } ///////////////       END of MONTH LOOP            /////////////////////


        // The ticket is reassigned every time a better price is found, and is only added
        //  to the ArrayList of Tickets once the cheapest ticket for a destination is found.

        fw.write("\n" + cheapestTicketForDest.toString() + "\n");
        System.out.println("\n" + cheapestTicketForDest.toString() + "\n");
        return cheapestTicketForDest;
    }


    /**
     * @param dest          Destination
     * @param startMonth    Start Month
     * @param startDay      Start Day
     * @param endMonth      End Month
     * @param endDay        End Day
     * @return              the Cheapest Price found for the given date range
     * @throws org.openqa.selenium.NoSuchElementException   Can time out and throw this exception if a web element is not found.
     */
    private static double getPriceForRange(String dest, int startMonth, int startDay, int endMonth, int endDay) throws org.openqa.selenium.NoSuchElementException
    {

        //////////////////////////   Search  Travelocity  Once per Date Range   //////////////////////////////
        driver.get("https://www.travelocity.com/");
        driver.manage().window().maximize();



        ////////////////////////////////////   Flights Tab   /////////////////////////////////////////
        WebElement airlineSelect = null;
        while (airlineSelect == null)
        {
            airlineSelect = driver.findElement(By.xpath("//*[@id=\"tab-flight-tab-hp\"]"));
        }
        if (!airlineSelect.isSelected())
        {
            airlineSelect.click();
        }




        ///////////////////////////////
        ////////////////////////////////////  From Atlanta  ////////////////////////////////////
        ///////////////////////////
        WebElement fromAirport = null;
        while (fromAirport == null)
        {
            fromAirport = driver.findElement(By.xpath("//*[@id=\"flight-origin-hp-flight\"]"));
        }

        // This if statement greatly reduces the time the test takes to run because there is no need to update the value
        //    of this field if Travelocity remembers it after the page reloads.
        if (!fromAirport.getText().contains("Atl"))
        {
            fromAirport.click();
            fromAirport.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
            fromAirport.sendKeys("Atlanta, GA (ATL-Hartsfield-Jackson Atlanta Intl.)");
//            fromAirport.sendKeys(Keys.ENTER);
        }


        ///////////////////////////////
        ////////////////////////////////////  To Destination  ////////////////////////////////////
        ///////////////////////////
        WebElement toAirport = null;
        while (toAirport == null)
        {
            toAirport = driver.findElement(By.xpath("//*[@id=\"flight-destination-hp-flight\"]"));
        }
        // This if statement greatly reduces the time the test takes to run because there is no need to update the value
        //    of this field if Travelocity remembers it after the page reloads.
        if (!toAirport.getText().contains(dest))
        {
            toAirport.click();
            toAirport.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
            toAirport.sendKeys(dest);
            toAirport.sendKeys(Keys.DOWN, Keys.ENTER);
        }


        ///////////////////////////////
        ////////////////////////////////////   Start Date   ////////////////////////////////////
        ///////////////////////////
        WebElement startDateInput = null;
        while (startDateInput == null)
        {
            startDateInput = driver.findElement(By.xpath("//*[@id=\"flight-departing-hp-flight\"]"));
        }
        startDateInput.click();
        startDateInput.sendKeys(Keys.CONTROL + "a");
        startDateInput.sendKeys(Keys.DELETE);
        startDateInput.sendKeys(startMonth + "/" + startDay + "/2019");


        ///////////////////////////////
        ////////////////////////////////////   End Date   ////////////////////////////////////
        ///////////////////////////
        WebElement endDayInput = null;
        while (endDayInput == null)
        {
            endDayInput = driver.findElement(By.xpath("//*[@id=\"flight-returning-hp-flight\"]"));
        }
        endDayInput.click();
        endDayInput.sendKeys(Keys.CONTROL + "a");
        endDayInput.sendKeys(Keys.DELETE);
        endDayInput.sendKeys(endMonth + "/" + endDay + "/2019");







        /////////////////////////////////   Find Search Button   ////////////////////////////////////////
        /////////////////////////////   Execute Search on 1st Page   ////////////////////////////////////
        ///////////////////////   Attempt to Maximize Window to Clear Popup   ///////////////////////////
        WebElement searchButton = null;
        while (searchButton == null)
        {
            searchButton = driver.findElement(By.xpath("//*[@id=\"gcw-flights-form-hp-flight\"]/div[9]/label/button"));
        }
        searchButton.click();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);





        /////////////////////////////////////
        //////////////////////////////////////////   Cheapest Price   ///////////////////////////////////
        ////////////////////////////////
        WebElement cheapestPriceWebElement = null;
        String cheapPrice;
        while (cheapestPriceWebElement == null)
        {
            try
            {
                cheapestPriceWebElement = driver.findElement(By.xpath("//*[@id=\"stops\"]/div/label[1]/div/div[2]/div"));
            }
            catch (org.openqa.selenium.NoSuchElementException e)
            {
//                cheapestWebElement = driver.findElement(By.xpath("//*[@id=\"stops\"]/div/label[1]/div/div[2]/div"));
//                e.printStackTrace();
                System.out.println("Oops, I got impatient and/or failed to find a price on a page...");
            }
        }
        cheapPrice =  cheapestPriceWebElement.getText();
        cheapPrice = cheapPrice.replace("\n", "");
        cheapPrice = cheapPrice.replace(",", "");
        cheapPrice = cheapPrice.replace("$", "");



        return Double.valueOf(cheapPrice);

    }



    @NotNull
    private String readTicketsFromFile()
    {
        try
        {
            Scanner scan = new Scanner(new File("C:\\Users\\JCool\\Documents\\___GGC\\Soft Testing & QA\\HelloSelenium\\CheapestTickets.txt"));
            while (scan.hasNext())
            {
                System.out.println(scan.nextLine());
            }
        }
        catch (FileNotFoundException e)
        {
            return "File Not Found!";
        }

        return "File printed to console!";
    }



//    @AfterClass
//    public static void breakDown() { driver.close(); }



}
