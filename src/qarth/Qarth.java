/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qarth;

import domain.Apartment;
import domain.Room;
import domain.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import resources.Terms;
import services.RealEstateBusinessService;
import services.ObjectPrintingService;

/**
 *
 * @author Marin
 */
public class Qarth {
    //#region Fields
    private static RealEstateBusinessService _realEstateService = null;
    private static ObjectPrintingService _printingService = null;
    private static final boolean TEST_CONF = false;
    private static Reader _reader = null;
    //#endregion Fields
    
    //#region inner Reader class
    private static class Reader{
        private Scanner _input = null;
        
        Reader(Scanner input){
            _input = input;
        }
        private String ReadUsername(){
            if (TEST_CONF) return "kupac";
            System.out.println(Terms.InsertYourUsername);
            return _input.nextLine();
        }
        
        private String ReadPassword(){
            if (TEST_CONF) return "test";
            System.out.println(Terms.InsertYourPassword);
            return _input.nextLine();
        }
        
        private String ReadState(){
            if (TEST_CONF) return "Srbija";
            System.out.println(Terms.WritelineDashSeparator);
            System.out.println(Terms.InsertApartmentState);
            String state = _input.nextLine();
            return state;
        }

        private String ReadCity(){
            if (TEST_CONF) return null;
            System.out.println(Terms.InsertApartmentCity);
            String city = _input.nextLine();
            if (city.equals("")){
                city = null;
            }
            return city;
        }
        
        private int ReadChoice(){
            int command = _input.nextInt();
            _input.nextLine(); // clear last cr from buffered reader
            return command;
        }
        
        private Date ReadDate(){
            try{
                String dateInput = _input.nextLine();
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = formatter.parse(dateInput);
                return date;
            }
            catch (Exception ex){
                System.out.println(Terms.ErrorDateIsNotInCorrectFormat);
                return null;
            }     
        }
    }   
    //#endregion inner Reader class
    
    private static User tryLogin(){
        String username = _reader.ReadUsername();
        if (!_realEstateService.DoesUserExist(username)){
            System.out.println(Terms.ErrorUserNotFound);
            return null;
        }
        String password = _reader.ReadPassword();
        User user = _realEstateService.LoadUser(username, password);
        if (user == null){
            System.out.println(Terms.ErrorUserNotFound);
        }
        return user;
    }
    
    private static int searchAparatments(){
        String state = _reader.ReadState();
        String city = _reader.ReadCity();
        
        List<Apartment> apartments = _realEstateService.FindApartmentsOnLocation(state, city);
        System.out.println(Terms.WritelineDashSeparator);
        System.out.println(Terms.ApartmentsOnLocation(state, city));               
        
        System.out.println(_printingService.StringifyApartmentList(apartments));
        System.out.println(Terms.WritelineDashSeparator);
        System.out.println(Terms.InsertApartmantIdOrZero);
        int command = _reader.ReadChoice();
        if (command == 0) return 0;
        Apartment ap = _realEstateService.LoadApartment(command);
        if (ap == null) return 0;
        System.out.println(_printingService.StringifyApartmentDetails(ap));
        return command;
    }   
    
    private static void roomReservation(User user){
        int command = searchAparatments();
        if (command == 0) return;
        
        System.out.println(Terms.WritelineDashSeparator);
        System.out.println(Terms.RoomReservation);
        System.out.println(Terms.InsertCheckInDate);
        Date startDate = _reader.ReadDate();
        if (startDate == null) return;
        System.out.println(Terms.InsertCheckOutDate);
        Date endDate = _reader.ReadDate();
        if (endDate == null) return;
        if (startDate.after(endDate) || startDate.equals(endDate)){
            System.out.println(Terms.ErrorCheckOutAfterCheckIn);
        }
        // find rooms for aparatment with command id, that are not reserved from start until end.
        List<Room> freeRooms = _realEstateService.GetFreeRoomsForApartment(command, startDate, endDate);
        if (freeRooms != null){
            System.out.println(Terms.FreeRooms);
            System.out.print(_printingService.StringifyRoomList(freeRooms));
            System.out.println(Terms.InsertRoomIdOrZero);
            command = _reader.ReadChoice();
            if (command == 0) return;
            Room room = _realEstateService.LoadRoom(command);
            if (room == null) return; // exception
            _realEstateService.SaveReservation(startDate, endDate, room, user);
            System.out.println(Terms.RoomIsReserved);
        }
        else{
            System.out.println(Terms.ThereAreNoFreeRooms);
        }
    }
    
    public static void main(String[] args) {
        _realEstateService = new RealEstateBusinessService();
        _printingService = new ObjectPrintingService();
        _reader = new Reader(new Scanner(System.in));
        
        User user = tryLogin();
        while(user != null){
            System.out.println(Terms.Choose);
            System.out.println(Terms.ApartmentSearchOption);
            if (!user.getIsSeller()){
                System.out.println(Terms.RoomReservationOption); 
            }
            
            System.out.println(Terms.ExitOption);
            int command = _reader.ReadChoice();

            if (command == 0) break;
            else if (command == 1){
                searchAparatments();
            }
            else if (command == 2 && !user.getIsSeller()){
                roomReservation(user);
            }
        }
        
        _realEstateService.close();       
    }
    
}
