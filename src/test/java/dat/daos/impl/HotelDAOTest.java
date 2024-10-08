package dat.daos.impl;

import dat.Populator;
import dat.config.HibernateConfig;
import dat.dtos.HotelDTO;
import dat.entities.Hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class HotelDAOTest
{

    private static HotelDAO hotelDAO;
    private static RoomDAO roomDAO;
    private static EntityManagerFactory emf;
    private static Populator populator;
    private static HotelDTO h1, h2;
    private static List<HotelDTO> hotels;


    @BeforeAll
    static void setUpAll()
    {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactory("test_db");
        hotelDAO = HotelDAO.getInstance(emf);
        roomDAO = RoomDAO.getInstance(emf);
        populator = new Populator(hotelDAO, roomDAO, emf);


    }

    @BeforeEach
    void setUp()
    {
        hotels = populator.populateHotels();
        h1 = hotels.get(0);
        h2 = hotels.get(1);

    }

    @AfterEach
    void tearDown()
    {
        populator.cleanUpHotels();
    }


    @AfterAll
    static void closeDown()
    {
        if (emf != null)
        {
            emf.close();
        }
    }

    @Test
    void readAll()
    {
        List<HotelDTO> hotels = hotelDAO.readAll();
        assertNotNull(hotels);
        assertEquals(2, hotels.size());
    }

    @Test
    void create()
    {
        HotelDTO h3 = new HotelDTO("Hotel 3", "Copenhagen", Hotel.HotelType.STANDARD);
        HotelDTO createdHotel = hotelDAO.create(h3);
        assertNotNull(createdHotel);
        assertEquals(h3.getHotelName(), createdHotel.getHotelName());
        assertEquals(h3.getHotelAddress(), createdHotel.getHotelAddress());
        assertEquals(h3.getHotelType(), createdHotel.getHotelType());

    }

    @Test
    void update()
    {
        h1.setHotelName("Hotel 1 Updated");
        HotelDTO updatedHotel = hotelDAO.update(h1.getId(), h1);
        assertNotNull(updatedHotel);
        assertEquals(h1.getHotelName(), updatedHotel.getHotelName());
        assertEquals(h1.getHotelAddress(), updatedHotel.getHotelAddress());
        assertEquals(h1.getHotelType(), updatedHotel.getHotelType());
    }


    @Test
    void delete()
    {
        hotelDAO.delete(h1.getId());
        List<HotelDTO> hotels = hotelDAO.readAll();
        assertEquals(1, hotels.size());
    }
}