import dat.daos.impl.HotelDAO;
import dat.daos.impl.RoomDAO;
import dat.dtos.HotelDTO;
import dat.dtos.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Populator
{

    private static HotelDAO hotelDao;
    private static RoomDAO roomDao;
    private static EntityManagerFactory emf;

    public Populator(HotelDAO hotelDao, RoomDAO roomDao, EntityManagerFactory emf)
    {
        Populator.hotelDao = hotelDao;
        Populator.roomDao = roomDao;
        Populator.emf = emf;
    }

    public List<HotelDTO> populateHotels()
    {
        Set<RoomDTO> calRooms = getCalRoomDTOs();
        Set<RoomDTO> hilRooms = getHilRoomDTOs();

        // Create and save hotels using DAO
        HotelDTO california, hilton;

        california = new HotelDTO("Hotel California", "California", Hotel.HotelType.LUXURY);
        hilton = new HotelDTO("Hilton", "Copenhagen", Hotel.HotelType.STANDARD);

        HotelDTO savedCalifornia = hotelDao.create(california);
        HotelDTO savedHilton = hotelDao.create(hilton);

        // Add rooms to hotels
        addRoomsToHotel(savedCalifornia, calRooms);
        addRoomsToHotel(savedHilton, hilRooms);

        return new ArrayList<>(List.of(savedCalifornia, savedHilton));
    }

    private void addRoomsToHotel(HotelDTO hotel, Set<RoomDTO> rooms)
    {
        for (RoomDTO room : rooms)
        {
            roomDao.addRoomToHotel(hotel.getId(), room);
        }
    }

    private static Set<RoomDTO> getCalRoomDTOs()
    {
        RoomDTO r100 = new RoomDTO(100, 2520, Room.RoomType.SINGLE);
        RoomDTO r101 = new RoomDTO(101, 2520, Room.RoomType.SINGLE);
        RoomDTO r102 = new RoomDTO(102, 2520, Room.RoomType.SINGLE);
        RoomDTO r103 = new RoomDTO(103, 2520, Room.RoomType.SINGLE);
        RoomDTO r104 = new RoomDTO(104, 3200, Room.RoomType.DOUBLE);
        RoomDTO r105 = new RoomDTO(105, 4500, Room.RoomType.SUITE);

        return Set.of(r100, r101, r102, r103, r104, r105);
    }

    private static Set<RoomDTO> getHilRoomDTOs()
    {
        RoomDTO r111 = new RoomDTO(111, 2520, Room.RoomType.SINGLE);
        RoomDTO r112 = new RoomDTO(112, 2520, Room.RoomType.SINGLE);
        RoomDTO r113 = new RoomDTO(113, 2520, Room.RoomType.SINGLE);
        RoomDTO r114 = new RoomDTO(114, 2520, Room.RoomType.DOUBLE);
        RoomDTO r115 = new RoomDTO(115, 3200, Room.RoomType.DOUBLE);
        RoomDTO r116 = new RoomDTO(116, 4500, Room.RoomType.SUITE);

        return Set.of(r111, r112, r113, r114, r115, r116);
    }

    public void cleanUpHotels() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE hotel_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
