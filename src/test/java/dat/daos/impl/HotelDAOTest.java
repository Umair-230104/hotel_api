package dat.daos.impl;

import dat.Populator;
import dat.config.HibernateConfig;
import dat.dtos.HotelDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotelDAOTest
{

    private static HotelDAO hotelDAO;
    private static RoomDAO roomDAO;
    private static EntityManagerFactory emf;
    private static Populator populator;


    public HotelDAOTest(HotelDAO hotelDAO, RoomDAO roomDAO, EntityManagerFactory emf)
    {

        this.hotelDAO = hotelDAO;
        this.roomDAO = roomDAO;
        this.emf = emf;
    }


    @BeforeAll
    static void setUpAll()
    {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactory("hotel");


    }

    @BeforeEach
    void setUp()
    {

    }


    @AfterAll
    void closeDown()
    {
        if (emf != null)
        {
            emf.close();
        }
    }

    @Test
    void readAll()
    {
    }

    @Test
    void create()
    {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("Test Hotel");
        hotelDTO.setHotelAddress("123 Test Street");
        //hotelDTO.setHotelType("Luxury");

        HotelDTO createdHotel = hotelDAO.create(hotelDTO);

        assertNotNull(createdHotel);
        assertEquals("Test Hotel", createdHotel.getHotelName());
        assertEquals("123 Test Street", createdHotel.getHotelAddress());
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