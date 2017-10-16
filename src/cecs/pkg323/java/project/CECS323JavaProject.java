
package cecs.pkg323.java.project; 
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CECS323JavaProject {
    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
 //   static final String displayFormat="15s%-15s%-15s%-15s%\n";
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
/**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }

    public static void main(String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
        
        Scanner in = new Scanner(System.in);
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        PreparedStatement preparedStatement = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("We have started!!");
            stmt = conn.createStatement();
            preparedStatement = conn.prepareStatement("SELECT * FROM WritingGroup");
            String sql;
            ResultSet rs = stmt.executeQuery("SELECT * FROM WritingGroup");
            int option;
            String tempName;
            do{
                option = menu();
                switch(option){
                    case 1:
                        sql = "SELECT * FROM WritingGroup";
                        rs = stmt.executeQuery(sql);
                        printWriterGroup(rs);
                        break;
                    case 2:
                        preparedStatement = conn.prepareStatement("Select * FROM"
                                + " WritingGroup WHERE WGroupName = ?");
                        System.out.print("Enter a name: ");
                        tempName = in.next();
                        preparedStatement.setString(1, tempName);
                        rs = preparedStatement.executeQuery();
                        printAllGroupInfo(rs);
                        break;
                    case 3:
                        sql = "SELECT * FROM Publisher";
                        rs = stmt.executeQuery(sql);
                        printPublisherNames(rs);
                        break;
                    case 4:
                        preparedStatement = conn.prepareStatement("SELECT * From"
                                + " Publisher WHERE PPublisherName = ?");
                        System.out.print("Enter a name: ");
                        tempName = in.next();
                        preparedStatement.setString(1, tempName);
                        rs = preparedStatement.executeQuery();
                        printAllPublisherInfo(rs);
                        break;
                    case 5:
                        sql = "SELECT * FROM Book";
                        rs = stmt.executeQuery(sql);
                        printAllBookNames(rs);
                        break;
                    case 6:
                        preparedStatement = conn.prepareStatement("SELECT * FROM"
                                + " Book WHERE BookTitle = ?");
                        System.out.print("Enter a title: ");
                       // System.out.println("Hello");
                        in.nextLine();
                        tempName = in.nextLine();
                        preparedStatement.setString(1, tempName);
                        rs = preparedStatement.executeQuery();
                        printAllBookInfo(rs);
                        break;
                    case 7:
                        System.out.println("Entering a book");
                        String bookName;
                        String groupName;
                        String publisherName;
                        String yearPublished;
                        String pageCount;
                        boolean validGroup = false;
                        boolean validPublisher = false;
                        boolean firstRound = true;
                        preparedStatement = conn.prepareStatement("INSERT INTO Book(BookTitle, WGroupName, PPublisherName,"
                                + " YearPublished, NumberPage) VALUES (?, ?, ?, ?, ?)");
                        System.out.print("Enter Book title: ");
                        bookName = in.nextLine();
                        System.out.print("Enter Writing Group: ");
                        groupName = in.nextLine();
                        System.out.print("Enter publisher: ");
                        publisherName = in.nextLine();
                        System.out.print("Enter the year the book was published: ");
                        yearPublished = in.nextLine();
                        System.out.print("Enter the page count: ");
                        pageCount = in.nextLine();
                        
                        do{
                            if(!validGroup && !firstRound){
                                System.out.println("We were not able to find the "
                                        + "writing grup in the database");
                                System.out.print("Reenter name of group: ");
                                groupName = in.nextLine();
                            }
                            if(!validPublisher && !firstRound){
                                System.out.println("We were not able to find the "
                                        + "publisher in the database");
                                System.out.print("Reenter name the publisher: ");
                                publisherName = in.nextLine();
                            }
                            
                            sql = "SELECT * FROM WritingGroup";
                            rs = stmt.executeQuery(sql);
                            while(rs.next()){
                                if(groupName.equals(rs.getString("WGroupName"))){
                                    validGroup = true;
                                    break;
                                }
                            }
                            
                            sql = "SELECT * FROM Publisher";
                            rs = stmt.executeQuery(sql);
                            while(rs.next()){
                                if(publisherName.equals(rs.getString("PPublisherName"))){
                                    validPublisher = true;
                                    break;
                                }
                            }
                            firstRound = false;
                            }while(!(validGroup && validPublisher));
                        preparedStatement.setString(1, bookName);
                        preparedStatement.setString(2, groupName);
                        preparedStatement.setString(3, publisherName);
                        preparedStatement.setString(4, yearPublished);
                        preparedStatement.setString(5, pageCount);
                        
                        try{
                            preparedStatement.executeUpdate();
                        }
                        catch(SQLException ex){
                           System.out.println("A book with the same tile, "
                                   + "writing group and pusbliher exists in the"
                                   + "system."); 
                        }
                        break;
                    case 8:
                        System.out.println("Entering a publisher");
                        String name;
                        String address;
                        String phone;
                        String email;
                        preparedStatement = conn.prepareStatement("INSERT INTO Publisher(PPublisherName, PPublisherAddress, PPublisherPhone, PPublisherEmail)"
                                + " VALUES (?, ?, ?, ?)");
                        System.out.print("Enter Publisher Name: ");
                        name = in.nextLine();
                        System.out.print("Enter address: ");
                        address = in.nextLine();
                        System.out.print("Enter phone: ");
                        phone = in.nextLine();
                        System.out.print("Enter email: ");
                        email = in.nextLine();
                        
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, address);
                        preparedStatement.setString(3, phone);
                        preparedStatement.setString(4, email);
                        
                        try{
                            preparedStatement.executeUpdate();
                        }
                        catch(SQLException ex){
                            System.out.println("A publisher with this name exists"
                                    + " in the system."); 
                        }
                        break;
                    case 9:
                        System.out.println("Updating a publisher in a book");
                        String newPublisher;
                        String oldPublisher;
                        boolean oldPublisherInSystem = false;
                        boolean newPublisherInSystem = false;
                      
                        System.out.print("Enter new publisher: ");
                        newPublisher = in.nextLine();
                        System.out.print("Which publisher will be it be replacing? ");
                        oldPublisher = in.nextLine();
                        
                        sql = "SELECT * FROM Publisher";
                        rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            if(oldPublisher.equals(rs.getString("PPublisherName"))){
                            oldPublisherInSystem = true;
                            break;
                            }
                        }
                        
                        sql = "SELECT * FROM Publisher";
                        rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            if(newPublisher.equals(rs.getString("PPublisherName"))){
                            newPublisherInSystem = true;
                            break;
                            }
                        }
                        
                        if(newPublisherInSystem && oldPublisherInSystem){
                            preparedStatement = conn.prepareStatement("UPDATE Book"
                                    + " SET PPublisherName = ?"
                                    + " WHERE PPublisherName = ?");
                            preparedStatement.setString(1, newPublisher);
                            preparedStatement.setString(2, oldPublisher);
                            
                            try{
                                preparedStatement.executeUpdate();
                            }
                            catch(SQLException ex){
                                System.out.println("You broke it!!");
                            }
                            
                        }
                        else{
                            if(!oldPublisherInSystem){
                                System.out.println("The publisher you want to replace"
                                    + "with does not exit in the system try again");
                            }
                            if(!newPublisherInSystem){
                                System.out.println("The pusblisher that you are trying to"
                                        + " replace with is not is the system. Choose option"
                                        + " 8 in the menu to add this publisher");
                            }
                        }
                        
                        break;
                    case 10:
                        System.out.println("Deleting book");
                        String bookDeleted;
                        boolean bookInSystem = false;
                 
                        System.out.print("Enter a name of the book: ");
                        bookDeleted = in.nextLine();
                        
                        sql = "SELECT * FROM Book";
                        rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            if(bookDeleted.equals(rs.getString("BookTitle"))){
                                bookInSystem = true;
                            }
                        }
                        
                        if(bookInSystem){
                            preparedStatement = conn.prepareStatement("DELETE"
                                    + " FROM Book WHERE BookTitle = ?");
                            preparedStatement.setString(1, bookDeleted);
                            try{
                                preparedStatement.executeUpdate();
                            }
                            catch(SQLException ex){
                                
                            }
                        }
                        else{
                            System.out.println("The book that you are trying to "
                                    + "delete does not exist in the system");
                        }
                        break;
                    case 11:
                        sql = "SELECT *"
                                + " FROM WritingGroup w LEFT OUTER JOIN Book b"
                                + " ON w.WGROUPNAME = b.WGROUPNAME"
                                + " WHERE BookTitle is NULL";
                        rs = stmt.executeQuery(sql);
                        printWriterGroup(rs);
                        break;
                    case 12:
                        sql = "SELECT DISTINCT*"
                        + " FROM Publisher p LEFT OUTER JOIN Book b"
                        + " ON p.PPublisherName = b.PPublisherName"
                        + " WHERE BookTitle is NULL";
                        rs = stmt.executeQuery(sql);
                        printPublisherNames(rs);
                        break;
                    case 13:
                        break;
                    default:
                        break;  
                }
            }while(option != 13);
            
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
    
    public static int menu(){
        Scanner input = new Scanner(System.in);
        int number;
        System.out.println("Please select an option from the menu");
        System.out.println("1) Print all Writing Groups");
        System.out.println("2) Print all the data for a Writing group");
        System.out.println("3) Print all Publishers");
        System.out.println("4) Print all the data for a Publisher");
        System.out.println("5) Print all Books");
        System.out.println("6) Print all the data for a Book");
        System.out.println("7) Add a new book");
        System.out.println("8) Add a new publisher");
        System.out.println("9) Update a publisher in books");
        System.out.println("10) Delete a book");
        System.out.println("11) Unpublished Writing groups");
        System.out.println("12) Publishers who have never published");
        System.out.println("13) exit");
        do{
        while(!input.hasNextInt()){
            String garbage = input.nextLine();
            System.out.println("Please enter an integer an integer");
        }
        number = input.nextInt();
        String junk = input.nextLine();
        if(number < 1 || number > 13){
           System.out.println("Not a valid option"); 
        }
        }while((number < 1 || number > 13) );
        
        return number;
    }
    
    
    public static void printWriterGroup(ResultSet a) throws SQLException{
        System.out.println("Printing all group names");
        int i = 0;
        while(a.next()){
            i++;
            String name;
            try{
                
                name = a.getString("WGroupName");
                System.out.println(i + ") " + name);
            }
            catch (SQLException ex) {
                Logger.getLogger(CECS323JavaProject.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
    
    public static void printAllGroupInfo(ResultSet a) throws SQLException{
        System.out.println("Printing all attributes of selected name");
        int i = 0;
        String name;
        String headWriter;
        String dateFormed;
        String subject;
        String format = "Name: %s\nHead Writer: %s\nDate Formed: %s\nSubject: %s\n";
        //System.out.format(format, "Name", "Head Writer", "Date Formed", "Subject");
        while(a.next()){
            try{
                name = a.getString("WGroupName");
                headWriter = a.getString("WHeadWriter");
                dateFormed = a.getString("WYearFormed");
                subject = a.getString("Subject");
                System.out.format(format, dispNull(name), dispNull(headWriter), 
                        dispNull(dateFormed), dispNull(subject));   
            }
            catch(SQLException ex){
             System.out.println("It's broken!!!");   
            }   
        }
    }
    
    
    public static void printPublisherNames(ResultSet a) throws SQLException{
        System.out.println("Printing all publisher names");
        int i = 0;
        while(a.next()){
            String name;
            try{
                i++;
                name = a.getString("PPublisherName");
                System.out.println(i + ") " + name);
            }
            catch (SQLException ex){
                System.out.println("You broke it!!");
            }
        }
    }
    
    public static void printAllPublisherInfo(ResultSet a) throws SQLException{
        System.out.println("Printing all attributes of selected name");
        int i = 0;
        String name;
        String address;
        String phone;
        String email;
        String format = "Name: %s\nAddress: %s\nPhone Number: %s\nEmail: %s\n";
        //System.out.format(format, "Name", "Address", "Phone", "Email");
        while(a.next()){
            try{
                name = a.getString("PPublisherName");
                address = a.getString("PPublisherAddress");
                phone = a.getString("PPublisherPhone");
                email = a.getString("PPublisherEmail");
                System.out.format(format, name, address, phone, email); 
            }
            catch(SQLException ex){
                
            }
        }
    }
    
    public static void printAllBookNames(ResultSet a) throws SQLException{
        String name;
        int i = 0;
        while(a.next()){
            try{
                i++;
                name = a.getString("BookTitle");
                System.out.println(i + ") " + name);
            }
            catch(SQLException ex){
                
            }
        }    
    }
    
    public static void printAllBookInfo(ResultSet a) throws SQLException{
        String bookName;
        String groupName;
        String publisherName;
        String yearPublished;
        String pageCount;
        String format = "Book Title: %s\nWritten By: %s\nPublished By: %s\nYear Published: %s\nPage Count: %s\n";
  //      System.out.format(format, "Book Title", "Written By", "Published By", "Year Published", "Page Count");
        while(a.next()){
            try{
                bookName = a.getString("BookTitle");
                groupName = a.getString("WGroupName");
                publisherName = a.getString("PPublisherName");
                yearPublished = a.getString("YearPublished");
                pageCount = a.getString("NumberPage");
                System.out.format(format, bookName, groupName, publisherName, yearPublished, pageCount);
            }
            catch(SQLException ex){
                
            }
            
        }
        
        
        
    } 
}