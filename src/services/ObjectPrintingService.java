/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Apartment;
import domain.Room;
import java.util.List;

/**
 *
 * @author Marin
 */
public class ObjectPrintingService {
    public String StringifyApartmentList(List<Apartment> apararments){
        StringBuilder sb = new StringBuilder("Id = Naziv : Drzava : Grad : Ulica : Broj\n");
        apararments.stream().forEach((aparatment) -> {
            sb.append(aparatment.getId())
                    .append(" = ")
                    .append(aparatment.getName())
                    .append(" : ")
                    .append(aparatment.getState())
                    .append(" : ")
                    .append(aparatment.getCity())
                    .append(" : ")
                    .append(aparatment.getStreet())
                    .append(" : ")
                    .append(aparatment.getNumberInStreet())
                    .append("\n");
        });
        
        return sb.toString();
    }
    
    public String StringifyApartmentDetails(Apartment entity){
        return "Apartman '" + entity.getName() + "' ima " + entity.getRoomCollection().size() + " sobe.";
    }
    
    public String StringifyRoomList(List<Room> rooms){
        StringBuilder sb = new StringBuilder("Id = BrojSobe : BrojOsoba : Opis\n");
        rooms.stream().forEach((r) -> {
            sb
                    .append(r.getId())
                    .append(" = ")
                    .append(r.getNumberInApartment())
                    .append(" : ")
                    .append(r.getMaxNumberOfOccupants())
                    .append(" : ")
                    .append(r.getDescription())
                    .append("\n");
        });
        return sb.toString();
    }
}
