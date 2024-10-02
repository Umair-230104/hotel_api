package dat.daos.impl;

import dat.Populator;
import dat.config.HibernateConfig;
import dat.dtos.HotelDTO;
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
        roomDAO =  RoomDAO.getInstance(emf);
        populator = new Populator(hotelDAO,roomDAO, emf);


    }

    @BeforeEach
    void setUp()
    {
        hotels = populator.populateHotels();
        h1 = hotels.get(0);
        h2 = hotels.get(1);

    }

   /* @AfterEach
    void tearDown()
    {
        populator.cleanUpHotels();
    }*/


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

    }

    @Test
    void update()
    {
    }

    @Test
    void delete()
    {
    }
}