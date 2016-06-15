/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.*;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Marin
 */
public class RealEstateBusinessService extends UnitOfWork {
    
    public boolean DoesUserExist(String username){
        Object [] params = {username};
        List<User> result = GetResults("SELECT u FROM User as u WHERE u.username = ?1", params);
        return !result.isEmpty();        
    }
    
    public User LoadUser(String username, String password){
        Object [] params = {username, password};
        List<User> result = GetResults("SELECT u FROM User as u WHERE u.username = ?1 AND u.password = ?2", params);
        if (result.isEmpty() || result.size() > 1) return null;
        return result.get(0);        
    }
       
    public List<Apartment> FindApartmentsOnLocation(String state, String city){
        if (state == null){
            throw new IllegalArgumentException("State must be defined");
        }
        StringBuilder sb = new StringBuilder("SELECT a FROM Apartment as a WHERE ");
        sb.append(" a.state = '").append(state).append("'");
        if (city != null){
            sb.append(" AND a.city = '").append(city).append("'");
        }
        
        return GetResults(sb.toString());
    }
    
    public Apartment LoadApartment(int id){
        Object [] params = {id};
        List<Apartment> result = GetResults("SELECT a FROM Apartment as a WHERE a.id = ?1", params);
        if (result.isEmpty()) return null;
        return result.get(0);
    }
    
    public Room LoadRoom(int id){
        Object [] params = {id};
        List<Room> result = GetResults("SELECT r FROM Room as r WHERE r.id = ?1", params);
        if (result.isEmpty()) return null;
        return result.get(0);
    }
    
    private List<Room> GetUnlockedRooms(int id){
        Object [] params = {id};
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT r FROM Apartment a INNER JOIN FETCH a.roomCollection r LEFT OUTER JOIN FETCH r.reservationCollection res"); // find all rooms and fetch all possible reservations
        query.append(" WHERE a.id = ?1"); // get only rooms for current apartment
        query.append(" AND a.isLocked = 0 "); // load only if apartment is not locked
        query.append(" AND r.isLocked = 0 "); // get only rooms that are not locked
        
        // left join sub query with reservation

        return GetResults(query.toString(), params);
    }
    
    private boolean IsRoomAvailableInPeriod(Collection<Reservation> reservations, Date start, Date end){
        // if there is at least one reservation in the start-end room cannot be reserved
        for (Reservation reservation: reservations){
            if (end.before(reservation.getCheckInDate()) || start.after((reservation.getCheckOutDate()))) continue;
            return false;
        }
        return true;
    }
    
    public List<Room> GetFreeRoomsForApartment(int id, Date startDate, Date endDate){
        List<Room> rooms = GetUnlockedRooms(id);
        List<Room> freeRooms = new LinkedList<>();
        rooms.stream().filter((room) -> (IsRoomAvailableInPeriod(room.getReservationCollection(), startDate, endDate))).forEach((room) -> {
            freeRooms.add(room);
        });
        return freeRooms;
    }
    
    public void SaveReservation(Date start, Date end, Room room, User buyer){
        Reservation reservation = new Reservation();
        reservation.setCheckInDate(start);
        reservation.setCheckOutDate(end);
        reservation.setRoomId(room);
        reservation.setBuyerId(buyer);
        
        PersistObject(reservation);
    }
}
