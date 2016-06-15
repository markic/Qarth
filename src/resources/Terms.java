/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

/**
 *
 * @author Marin
 * TODO move to resources.
 */
public final class Terms {
    public static final String WritelineDashSeparator = "--------------------------------------";
    public static final String InsertYourUsername = "Unesite vaše korisničko ime:";
    public static final String InsertYourPassword = "Unesite vašu lozinku:";
    public static final String InsertApartmentState = "Unesite državu apartmana:";
    public static final String InsertApartmentCity = "Unesite grad apartmana (ENTER za sve gradove):";
    
    public static final String ErrorDateIsNotInCorrectFormat = "Datum nije ispravno unet.";
    public static final String ErrorUserNotFound = "Korisnik nije pronađen.";
    public static final String ErrorCheckOutAfterCheckIn = "Početak boravka ne može biti kasniji od završetka.";
            
    public static final String InsertApartmantIdOrZero = "Unesite Id apratmana ili 0 za izlaz:";
    public static final String InsertCheckInDate = "Unesite datum početka boravka u formatu dan/mesec/godina (pr. 23/05/2016)";
    public static final String InsertCheckOutDate = "Unesite datum završetka boravka u formatu dan/mesec/godina (pr. 23/05/2016)";
    public static final String InsertRoomIdOrZero = "Unesite Id sobe za rezervaciju ili 0 za izlaz.";
    public static final String RoomIsReserved = "Soba je uspešno rezervisana.";
    public static final String ThereAreNoFreeRooms = "Aparatman nema raspoloživih soba.";
    public static final String Choose = "+ Izberite: ";
    public static final String RoomReservation = "+ Rezervacije Sobe:";
    public static final String FreeRooms = "+ Slobodne sobe:";

    public static final String ApartmentSearchOption = "1 - Pretraga aparatmana";
    public static final String RoomReservationOption = "2 - Rezervacija sobe";
    public static final String ExitOption = "0 - Izlaz";
    
    public static final String ApartmentsOnLocation(String state, String city)
    {
        return "Apartmani u : '" + state + "' " + (city != null ? " - " + city : "");
    }           
}
