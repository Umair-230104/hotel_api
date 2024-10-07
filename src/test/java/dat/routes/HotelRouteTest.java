package dat.routes;

import dat.Populator;
import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.HotelDAO;
import dat.daos.impl.RoomDAO;
import dat.dtos.HotelDTO;
import dat.entities.Hotel;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class HotelRouteTest
{
    private static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static String BASE_URL = "http://localhost:7070/api/v1";
    private static HotelDAO hotelDao = HotelDAO.getInstance(emf);
    private static RoomDAO roomDao = RoomDAO.getInstance(emf);
    private static Populator populator = new Populator(hotelDao, roomDao, emf);

    private static HotelDTO california, hilton;
    private static List<HotelDTO> hotels;

    @BeforeAll
    static void init()
    {
        HibernateConfig.setTest(true);
        app = ApplicationConfig.startServer(7070);
    }

    @BeforeEach
    void setUp()
    {
        hotels = populator.populateHotels();
        california = hotels.get(0);
        hilton = hotels.get(1);
    }

    @AfterEach
    void tearDown()
    {
        populator.cleanUpHotels();
    }

    @AfterAll
    static void closeDown()
    {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void testGetAllHotels()
    {
        HotelDTO[] hotelDTOS =
                given()
                        .when()
                        .get(BASE_URL + "/hotels")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(HotelDTO[].class);

        assertThat(hotelDTOS.length, is(2));
        assertThat(hotelDTOS, arrayContainingInAnyOrder(equalTo(california), equalTo(hilton)));
    }

    @Test
    void testGetHotelById()
    {
        HotelDTO hotelDTO =
                given()
                        .when()
                        .get(BASE_URL + "/hotels/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(HotelDTO.class);

        assertThat(hotelDTO, equalTo(california));
    }

    @Test
    void testCreateHotel()
    {
        Hotel h3 = new Hotel("Hotel 3", "Copenhagen", Hotel.HotelType.STANDARD);
        Hotel createdHotel =
                given()
                        .contentType("application/json")
                        .body(h3)
                        .when()
                        .post(BASE_URL + "/hotels")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(Hotel.class);

        assertThat(createdHotel.getHotelName(), equalTo(h3.getHotelName()));
        assertThat(createdHotel.getHotelAddress(), equalTo(h3.getHotelAddress()));
        assertThat(createdHotel.getHotelType(), equalTo(h3.getHotelType()));
    }

    @Test
    void testUpdateHotel()
    {
        california.setHotelName("Hotel California Updated");
        HotelDTO updatedHotel =
                given()
                        .contentType("application/json")
                        .body(california)
                        .when()
                        .put(BASE_URL + "/hotels/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(HotelDTO.class);

        assertThat(updatedHotel.getHotelName(), equalTo(california.getHotelName()));
        assertThat(updatedHotel.getHotelAddress(), equalTo(california.getHotelAddress()));
        assertThat(updatedHotel.getHotelType(), equalTo(california.getHotelType()));
    }

    @Test
    void deleteHotel()
    {
        given()
                .when()
                .delete(BASE_URL + "/hotels/1")
                .then()
                .log().all()
                .statusCode(204);

        HotelDTO[] hotelDTOS =
                given()
                        .when()
                        .get(BASE_URL + "/hotels")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(HotelDTO[].class);

        assertThat(hotelDTOS.length, is(1));
        assertThat(hotelDTOS[0], equalTo(hilton));
    }
}
