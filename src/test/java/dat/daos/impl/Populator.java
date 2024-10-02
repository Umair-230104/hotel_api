package dat.daos.impl;


import dat.dtos.HotelDTO;
import dat.dtos.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManagerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Populator
{

    private static HotelDAO hotelDao;
    private static RoomDAO roomDao;
    private static EntityManagerFactory emf;

    public Populator(HotelDAO hotelDao, RoomDAO roomDao, EntityManagerFactory emf)
    {
        this.hotelDao = hotelDao;
        this.roomDao = roomDao;
        this.emf = emf;
    }

    public List<HotelDTO> populate3Hotels()
    {
        // Create HotelDTOs
        HotelDTO h1 = new HotelDTO("Hilton", "1234 Main St", Hotel.HotelType.LUXURY);
        HotelDTO h2 = new HotelDTO("Marriott", "5678 Elm St", Hotel.HotelType.BUDGET);
        HotelDTO h3 = new HotelDTO("Holiday Inn", "91011 Oak St", Hotel.HotelType.STANDARD);

        // Create RoomDTOs
        RoomDTO r1 = new RoomDTO(101, 100, Room.RoomType.SINGLE);
        RoomDTO r2 = new RoomDTO(102, 200, Room.RoomType.DOUBLE);
        RoomDTO r3 = new RoomDTO(103, 300, Room.RoomType.SUITE);

        // Create Hotel entities and add rooms
        Hotel hotel1 = new Hotel(h1.getHotelName(), h1.getHotelAddress(), h1.getHotelType()); // Assuming constructor uses raw values
        hotel1.addRoom(new Room(r1.getRoomNumber(), new BigDecimal(r1.getRoomPrice()), r1.getRoomType())); // Add Room objects

        Hotel hotel2 = new Hotel(h2.getHotelName(), h2.getHotelAddress(), h2.getHotelType());
        hotel2.addRoom(new Room(r2.getRoomNumber(), new BigDecimal(r2.getRoomPrice()), r2.getRoomType()));

        Hotel hotel3 = new Hotel(h3.getHotelName(), h3.getHotelAddress(), h3.getHotelType());
        hotel3.addRoom(new Room(r3.getRoomNumber(), new BigDecimal(r3.getRoomPrice()), r3.getRoomType()));

        // Convert Hotel entities back to DTOs if needed for persistence
        HotelDTO savedH1 = hotelDao.create(convertToDTO(hotel1));
        HotelDTO savedH2 = hotelDao.create(convertToDTO(hotel2));
        HotelDTO savedH3 = hotelDao.create(convertToDTO(hotel3));

        // Return the saved HotelDTOs
        return new ArrayList<>(List.of(savedH1, savedH2, savedH3));
    }

    // Helper method to convert Hotel entity to HotelDTO
    private HotelDTO convertToDTO(Hotel hotel)
    {
        // Assuming you have a method to convert a Hotel to HotelDTO
        return new HotelDTO(hotel.getHotelName(), hotel.getHotelAddress(), hotel.getHotelType());
    }


    public void cleanUpHotels()
    {
        // Delete all data from database
        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE hotel_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
