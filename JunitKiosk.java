import static org.junit.Assert.*;
import org.junit.*;
/*******************************************
 * The test class for VideoKiosk.
 *
 * @author Professor Grissom
 * @version Sept 4, 2013
 ******************************************/
import java.io.PrintStream;
import java.io.OutputStream;

public class JunitKiosk
{
    private static StringBuilder textLog;
    private static PrintStream stdout;
    private VideoKiosk kiosk;
    /** allow for round off error */
    private double TOLERANCE = 0.5; 
    /** sales tax rate */
    private double TAX = 0.06;
    private int PRICE = 198;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        textLog = new StringBuilder();
        stdout = System.out;
        //System.setOut (new PrintStream(logger));
    }

    /******************************************************
     * Instantiate a Kiosk object.
     *
     * Called before every test case method.
     *****************************************************/
    @Before
    public void setUp()
    {
        kiosk = new VideoKiosk();  
    }

    /******************************************************
     * Test getVideoCount
     *****************************************************/
    @Test 
    public void testGetVideoCount()
    {
        // confirm three copies of each title were initially created
        Assert.assertEquals("getVideoCount(1): number of DVDs should be 3", 
            3, kiosk.getVideoCount(1));                  
        Assert.assertEquals("getVideoCount(2): number of DVDs should be 3", 
            3, kiosk.getVideoCount(2));  
        Assert.assertEquals("getVideoCount(3): number of DVDs should be 3", 
            3, kiosk.getVideoCount(3));  

        // confirm correct error flag if incorrect video ID        
        Assert.assertEquals("getVideoCount(99): value should be -1 for bad video ID", 
            -1, kiosk.getVideoCount(99));                  
    }  

    /******************************************************
     * Test initial values of the constructor
     *****************************************************/
    @Test 
    public void testConstructor()
    {
        // create a new kiosk with ONE copy of each title
        textLog.setLength (0);
        VideoKiosk kiosk = new VideoKiosk(1);
        //checkForAnyOutput(textLog, "Constructor must display the initial welcome" +
        //    " message by invoking displayGreetings()");
        int inStock = kiosk.getVideoCount(1);

        // confirm there is only one copy of Title 1
        Assert.assertEquals("VideoKiosk(1): should have stocked kiosk with 1 DVDs" +
            " (one per title)", 1, inStock);                  
    } 

    /******************************************************
     * Test Daily Report
     *****************************************************/
    @Test 
    public void testDailyReport()
    {
        // rent three titles and insert sufficient money
        kiosk.selectVideo(1); 
        kiosk.selectVideo(2);  
        //kiosk.selectVideo(3);  
        kiosk.checkOut();
        kiosk.insertMoney(500);

        Assert.assertEquals("createDailyReport(): daily sales should be $4.20", 
            4.20, kiosk.getDailySales(), TOLERANCE);                  
        Assert.assertEquals("createDailyReport(): transactions should be 1", 
            1, kiosk.getTransactions());  

        // check value of total daily sales
        textLog.setLength(0);
        kiosk.createDailyReport();
        //String[] dailyout = textLog.toString().split("\n");
        //Assert.assertTrue("createDailyReport must produce at least 4 lines of output", 
        //    dailyout.length >= 4);
        //checkForCurrencyOutput ("createDailyReport()", textLog, 1);

        Assert.assertEquals("createDailyReport(): daily sales should be reset to 0", 
            0, kiosk.getDailySales(), TOLERANCE);                  
        Assert.assertEquals("createDailyReport(): transactions should be reset to 0", 
            0, kiosk.getTransactions());    
    }   

    /******************************************************
     * Test selecting a video
     *****************************************************/
    @Test 
    public void testSelection()
    {
        // select one video and confirm inventory goes down
        for (int k = 3; k >= 1; k--)
        {
            int count = kiosk.getVideoCount(k);
            textLog.setLength (0);
            kiosk.selectVideo(k);
            //checkForAnyOutput(textLog, "selectVideo() must display an appropriate message");
            Assert.assertEquals("selectVideo(3): inventory of video 3 should decrement", 
                count - 1, kiosk.getVideoCount(k));
        }
    }   

    /******************************************************
     * Test number of transactions
     *****************************************************/
    @Test 
    public void testTransactions()
    {
        // Kiosk should start with zero transactions
        Assert.assertEquals("getTransactions(): kiosk should start with zero transactions", 
            0, kiosk.getTransactions());  

        // perform one full transaction and confirm count        
        kiosk.selectVideo(2);
        kiosk.checkOut();
        kiosk.insertMoney(500);
        Assert.assertEquals("getTransactions(): a single rental counts as a transaction", 
            1, kiosk.getTransactions());  

        // perform second transaction and confirm count
        kiosk.selectVideo(3);
        kiosk.selectVideo(3); 
        kiosk.checkOut();
        kiosk.insertMoney(500);        
        Assert.assertEquals("getTransactions(): multiple rentals for one customer is only one transaction", 
            2, kiosk.getTransactions()); 

        // perform third transaction and confirm count                        
        kiosk.returnVideo(3);        
        Assert.assertEquals("getTransactions(): a return counts as one transaction", 
            3, kiosk.getTransactions());                    
    }       

    /******************************************************
     * Test if out of stock
     *****************************************************/
    @Test 
    public void testOutOfStock()
    {
        // select all videos in stock for title 3
        int count = kiosk.getVideoCount(3);
        Assert.assertTrue ("getVideoCount(x) should have returned non-zero", count != 0);
        for(int i = 1; i <= count; i++){
            textLog.setLength (0);
            kiosk.selectVideo(3);
            //checkForAnyOutput(textLog, "selectVideo() must display appropriate message");
        }

        // confirm inventory is zero
        Assert.assertEquals("selectVideo(3): video 3 should be out of stock", 
            0, kiosk.getVideoCount(3));  

        // attempt to select a video out of stock
        String previousMessage = textLog.toString();
        textLog.setLength (0);
        kiosk.selectVideo(3);
        Assert.assertNotSame("selectVideo() should display a different message" +
            " when video is out of stock", previousMessage, textLog.toString());
        Assert.assertEquals("selectVideo(3): video 3 should not have changed since out of stock", 
            0, kiosk.getVideoCount(3));  
    }  

    /******************************************************
     * Test returning a video
     *****************************************************/
    @Test 
    public void testReturnVideo()
    {
        // return title #3 and confirm inventory increases
        int count;
        for (int id = 3; id >= 1; id--) {
            count = kiosk.getVideoCount(id);
            textLog.setLength (0);
            kiosk.returnVideo(id);
            //checkForAnyOutput(textLog, "returnVideo() should display appropriate message");
            Assert.assertEquals("returnVideo(3): video 3 should increase by 1", 
                count + 1, kiosk.getVideoCount(id));  
        }

        // try to return an incorrect title ID
        count = kiosk.getTotalInventory();
        String prevMsg = textLog.toString();
        kiosk.returnVideo(5);
        Assert.assertNotSame("returnVideo() should display an error message"
            + " when the video id is incorrect", prevMsg, textLog.toString());
        Assert.assertEquals("returnVideo(5): inventory should not change with bad video ID", 
            count, kiosk.getTotalInventory());  
    }      

    /******************************************************
     * Test for selection errors
     *****************************************************/
    @Test 
    public void testSelectionError()
    {
        int selected = kiosk.getNumSelections(); 
        textLog.setLength (0);
        kiosk.selectVideo(1);  
        Assert.assertEquals("selectVideo() should update number of user selections", 
            selected + 1, kiosk.getNumSelections());
        String prevMsg = textLog.toString();
        selected = kiosk.getNumSelections(); 
        kiosk.selectVideo(5);
        Assert.assertNotSame("selectVideo() should display an error message"
            + " when the video id is incorrect", prevMsg, textLog.toString());
        Assert.assertEquals("selectVideo(5): should not change inventory if bad video ID is used", 
            selected, kiosk.getNumSelections());         
    } 

    /******************************************************
     * Test inserting money
     *****************************************************/
    @Test 
    public void testInsertMoney()
    {
        // credit should start with zero
        int credit = 0;
        kiosk.selectVideo(1);
        kiosk.checkOut();
        Assert.assertEquals("credit should be zero before inserting money", 
            credit, kiosk.getCredit(), TOLERANCE);  

        // insert a nickel
        textLog.setLength (0);
        kiosk.insertMoney(5);
        //checkForAnyOutput(textLog, "insertMoney() should display appropriate message");
        //checkForCurrencyOutput("insertMoney()", textLog, 2);
        credit+= 5;
        Assert.assertEquals("insertMoney(5): credit should be 5 after a nickel", 
            credit, kiosk.getCredit(), TOLERANCE);  

        // insert a dime
        textLog.setLength (0);
        kiosk.insertMoney(10);
        //checkForCurrencyOutput("insertMoney()", textLog, 2);
        credit+= 10;
        Assert.assertEquals("insertMoney(10): credit should be 15 after a dime", 
            credit, kiosk.getCredit(), TOLERANCE);   

        // insert a quarter        
        textLog.setLength (0);
        kiosk.insertMoney(25);
        //checkForCurrencyOutput("insertMoney()", textLog, 2);
        credit+= 25;
        Assert.assertEquals("insertMoney(25): credit should be 40 after a quarter", 
            credit, kiosk.getCredit(), TOLERANCE); 

        // insert a dollar        
        textLog.setLength (0);
        kiosk.insertMoney(100);
        //checkForCurrencyOutput("insertMoney()", textLog, 2);
        credit+= 100;
        Assert.assertEquals("insertMoney(100): credit should be 140 after a dollar", 
            credit, kiosk.getCredit(), TOLERANCE);

        // attempt an incorrect value
        textLog.setLength (0);
        kiosk.insertMoney(17);
        //checkForAnyOutput(textLog, "insertMoney() should display a warning when"
        //    + " the payment is not a valid amount");
        Assert.assertEquals("insertMoney(17): credit should not change if bad coin", 
            credit, kiosk.getCredit(), TOLERANCE);   

        // insert $5        
        textLog.setLength (0);
        kiosk.insertMoney(500); /* this big payment should trigger finishTransaction() */
        //checkForCurrencyOutput("insertMoney()", textLog, 1);
        Assert.assertEquals("insertMoney(500): credit should be reset to zero after"
            + " sufficient payment received at the end of a transaction", 
            0, kiosk.getCredit(), TOLERANCE);                  

    } 

    /******************************************************
     * Test checkout
     *****************************************************/
    @Test 
    public void testCheckOut()
    {
        // calculate cost of one video
        double cost = kiosk.getPrice() * (1.0 + TAX);

        // confirm amount due starts at zero
        double due = kiosk.getAmountDue();
        kiosk.checkOut();
        Assert.assertEquals("checkOut(): amount due should be zero if no selections", 
            0, kiosk.getAmountDue(), TOLERANCE);  

        // confirm amount due after one selection        
        kiosk.selectVideo(1);
        textLog.setLength(0);
        kiosk.checkOut();
        //checkForCurrencyOutput("checkOut()", textLog, 1);
        //checkForAnyOutput(textLog, "checkOut() should display an appropriate message");
        due = kiosk.getAmountDue();
        Assert.assertEquals("checkOut(): amount due should be "+cost, 
            cost, kiosk.getAmountDue(), TOLERANCE);  
    }       

    @Test
    public void testReset()
    {
        kiosk.selectVideo(1);
        kiosk.checkOut();
        kiosk.createDailyReport();

        Assert.assertEquals("createDailyReport() should invoke resetKiosk() "
            + "to reset kiosk data ", 0, kiosk.getDailySales(), TOLERANCE);
        Assert.assertEquals("createDailyReport() should invoke resetKiosk() "
            + "to reset kiosk data ", 0, kiosk.getTransactions());
    }

    //     private static OutputStream logger = new OutputStream() {
    //             @Override
    //             public void write(int b)
    //             {
    //                 textLog.append((char) b);
    //             }
    //         };

    private void checkForCurrencyOutput (String method, StringBuilder b, int N)
    {
        /* Regular expressions for $d?d?d(,ddd)*.dd */
        String regex = "\\$\\d?\\d?\\d(\\,\\d\\d\\d)*\\.\\d\\d";
        int currencyCount = 0;
        for (String line : b.toString().split("\n"))
        {
            for (String s : line.split(" "))
            {
                if (s.matches(regex)) {
                    currencyCount++;
                }
            }
        }
        Assert.assertTrue(method + ": monetary amount must be displayed in currency format",
            currencyCount >= N);
    }

    private void checkForAnyOutput (StringBuilder b, String msg)
    {
        Assert.assertTrue(msg, b.length() > 0);
    }

    @Test
    public void transactionInProgress()
    {
        textLog.setLength(0);
        kiosk.checkOut();
        //checkForAnyOutput(textLog, "checkOut() should display a warning when not in transaction");

        textLog.setLength(0);
        kiosk.insertMoney(10);
        String errMsg = textLog.toString();

        kiosk.selectVideo(1);
        kiosk.checkOut();
        textLog.setLength(0);
        kiosk.insertMoney(10);
        //checkForAnyOutput(textLog, "insertMoney() should display a warning when not in transaction");
        //Assert.assertTrue("insertMoney() should display a warning when " +
        //    "not in transaction", errMsg.equals(textLog.toString()) == false);
    }
}