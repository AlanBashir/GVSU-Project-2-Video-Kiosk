import java.text.*;
/**
 * Write a description of class VideoKiosk here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VideoKiosk
{
    /**Video Titles*/
    private String t1;
    private String t2;
    private String t3;

    /**Title Inventories*/
    private int inv1;
    private int inv2;
    private int inv3;

    /**credit balance*/
    private double cBalance;

    /**Amount Due*/
    private double aDue;

    /**Title Selected & Daily Transaction */
    private int cSelected;
    private int dTrans;

    /**Daily Sales*/
    private double dSales;

    /**Final Values*/
    //Price for one video
    public final double tPrice = 198.0;

    //Sales Tax
    public final double sTax = 1.06;

    //starting Inventory
    public final int sInv = 3;

    /**Number Format**/
    NumberFormat fmt;

    /**********************************************************
     * A Constructor which gives initial values to variables 
     **********************************************************/
    public VideoKiosk () {
        //Video Titles
        t1 = "Social Network";
        t2 = "Lord Of The Rings";
        t3 = "Book of Eli";

        //Starting Inventory 
        inv1 = sInv;
        inv2 = sInv;
        inv3 = sInv;

        //Credit Balance
        cBalance = 0;

        //Amount Due
        aDue = 0;

        //titles selected
        cSelected = 0;

        //Daily Transaction
        dTrans = 0;

        //Daily Sales
        dSales = 0;
        
        fmt = NumberFormat.getCurrencyInstance();

        displayGreeting();
    }

    /*************************************************
     * A Constructor which Initializes Values
     * 
     * @param units - Initial Inventory
     **********************************************/
    public VideoKiosk(int units) {
        //Video Titles
        t1 = "Social Network";
        t2 = "Lord Of The Rings";
        t3 = "Book of Eli";

        //Starting Inventory 
        inv1 = units;
        inv2 = units;
        inv3 = units;

        //Credit Balance
        cBalance = 0;

        //Amount Due
        aDue = 0;

        //titles selected
        cSelected = 0;

        //Daily Transaction
        dTrans = 0;

        //Daily Sales
        dSales = 0;
        
        fmt = NumberFormat.getCurrencyInstance();

        displayGreeting();
    }

    /******************************************************
     * Accessor Method Which returns price of One Video
     ******************************************************/
    public double getPrice() {
        return tPrice;
    }

    /*****************************************************
     * Accessor Method which returns Customer Credit;
     *****************************************************/
    public double getCredit() {
        return cBalance;
    }

    /******************************************************
     * Accessor Method which returns Amount Customer owes
     ******************************************************/
    public double getAmountDue() {
        return aDue;
    }

    /********************************************************
     * Return number of Transactions completed in the day
     *******************************************************/
    public int getTransactions() {
        return dTrans;
    }

    /***********************************************************
     * Accessor Method which returns number of titles selected
     ***********************************************************/
    public int getNumSelections() {
        return cSelected;
    }

    /************************************************
     * Accessor Method which returns total Inventory
     ************************************************/
    public int getTotalInventory() {
        int total = inv1 + inv2 + inv3;
        return total;
    }

    /**********************************************
     * Accessor Method which returns Daily Sales
     *********************************************/
    public double getDailySales() {
        return dSales;
    }

    /*****************************************************
     * get Video count
     * 
     * @param - id - Number which identitfies title
     *****************************************************/
    public int getVideoCount(int id) {
        if(id == 1) {
            return inv1;
        }
        if(id == 2) {
            return inv2;
        }
        if(id == 3) {
            return inv3;
        }
        else {
            return -1;
        }
    }

    /******************************************
     * Mutator Method which Selected Video
     * 
     * @param id - id for Video title
     ******************************************/
    public void selectVideo(int id) {
        if(id == 1 && inv1 > 0) {
            inv1--;
            cSelected++;
            System.out.println("You Have selected " + t1);
        }
        if(id == 2 && inv2 > 0) {
            inv2--;
            cSelected++;
            System.out.println("You Have selected " + t2);
        }
        if(id==3 && inv3 > 0) {
            inv3--;
            cSelected++;
            System.out.println("You Have selected " + t3);
        }
        else {
            System.out.println("No Movie selected");
        }
    }

    /********************************************
     * Mutator Method Which inserts Money
     * 
     * @param amount - Amount of money which customer inserts 
     *******************************************************/
    public void insertMoney(int amount) {
        System.out.println("Machine only accepts: Nickels, Dimes, Quarters, 1s, and 5s");

        if(amount % 5 == 0 && amount <= 500) {

            cBalance += amount;
            if(cBalance < aDue) {
                double r = aDue - cBalance;
                System.out.println("Please Insert " + fmt.format(r) +" To complete transaction.");
            }
            if(aDue<= cBalance) {
                double r = cBalance - aDue;
                System.out.println("Thank You for you purchase!");
                System.out.println("Your change is "+ fmt.format(r));
                finishTransaction();
            }
        }
        else {
            System.out.println ("Please enter a valid amount");
        }
    }

    /******************************************************
     * Mutator Method which checks out Selected Titles.
     * 
     *****************************************************/
    public void checkOut() {
        double total = cSelected * (tPrice * sTax);

        aDue = total;

        System.out.println("You Owe: " + fmt.format(aDue));
        System.out.print("Please Insert Money");

    }

    /**************************************
     * Mutator Method which returns videos
     * 
     * @param id - id for title
     **************************************/
    public void returnVideo (int id) {
        if(id==1) {
            inv1++;
            cSelected--;
            System.out.println("You have returned " + t1);
            finishTransaction();
        }
        if(id == 2) {
            inv2++;
            cSelected--;
            System.out.println("You Have returned " + t2);
            finishTransaction();
        }
        if(id == 3) {
            inv3++;
            cSelected--;
            System.out.println("You Have returned " + t3);
            finishTransaction();
        }
        else{
            System.out.println("Please make a valid return");
        }
    }

    /*************************
     * Method to reset Values
     **************************/
    public void resetKiosk() {
        //Credit Balance
        cBalance = 0;

        //Amount Due
        aDue = 0;

        //titles selected
        cSelected = 0;

        //Daily Transaction
        dTrans = 0;

        //Daily Sales
        dSales = 0;
    }

    /*************************************
     * Private method which concludes Transaction
     *************************************/
    private void finishTransaction() {
        dTrans++;
        dSales += aDue;
        aDue = 0;

        cSelected = 0;

        cBalance = 0;

        displayGreeting();
    }

    /***************************
     *A welcome Message
     **************************/
    private void displayGreeting() {
        System.out.println ("Welcome to The Movie Spot");
        System.out.println("Please Choose a Title: ");
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
    }
    
    public void createDailyReport() {
        System.out.println("Total Sales: " + fmt.format(getDailySales()));
        System.out.println("Total Transaction: " + dTrans);
        System.out.println("Total Inventory :" + getTotalInventory());
        resetKiosk();
    }
}
