package dat;

import dat.config.HibernateConfig;
import dat.daos.impl.HotelDAO;
import dat.daos.impl.RoomDAO;
import dat.dtos.HotelDTO;
import dat.dtos.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
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
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");

        Set<Room> calRooms = getCalRooms();
        Set<Room> hilRooms = getHilRooms();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel california = new Hotel("Hotel California", "California", Hotel.HotelType.LUXURY);
            Hotel hilton = new Hotel("Hilton", "Copenhagen", Hotel.HotelType.STANDARD);
            california.setRooms(calRooms);
            hilton.setRooms(hilRooms);
            em.persist(california);
            em.persist(hilton);
            em.getTransaction().commit();

            return new ArrayList<>(List.of(new HotelDTO(california), new HotelDTO(hilton)));
        }
    }

    @NotNull
    private static Set<Room> getCalRooms() {
        Room r100 = new Room(100, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r101 = new Room(101, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r102 = new Room(102, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r103 = new Room(103, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r104 = new Room(104, new BigDecimal(3200), Room.RoomType.DOUBLE);
        Room r105 = new Room(105, new BigDecimal(4500), Room.RoomType.SUITE);

        Room[] roomArray = {r100, r101, r102, r103, r104, r105};
        return Set.of(roomArray);
    }

    @NotNull
    private static Set<Room> getHilRooms() {
        Room r111 = new Room(111, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r112 = new Room(112, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r113 = new Room(113, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r114 = new Room(114, new BigDecimal(2520), Room.RoomType.DOUBLE);
        Room r115 = new Room(115, new BigDecimal(3200), Room.RoomType.DOUBLE);
        Room r116 = new Room(116, new BigDecimal(4500), Room.RoomType.SUITE);

        Room[] roomArray = {r111, r112, r113, r114, r115, r116};
        return Set.of(roomArray);
    }

    public void cleanUpHotels() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room").executeUpdate();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE hotel_hotel_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
