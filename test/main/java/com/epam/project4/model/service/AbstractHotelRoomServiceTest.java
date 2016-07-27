package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.dao.GenericHotelRoomDao;
import main.java.com.epam.project4.model.dao.GenericReservationDao;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class AbstractHotelRoomServiceTest {

    private static AbstractHotelRoomService hotelRoomService;

    private static GenericHotelRoomDao hotelRoomDao;

    private static GenericReservationDao reservationDao;

    private static AbstractParameterValueService parameterValueService;

    @BeforeClass
    public static void beforeClass() throws Exception{

    }

    @Test
    public void testGetAllRoomFullDetails() throws Exception {

    }

    @Test
    public void testGetFullDetailsWithMonthReservationsDetails() throws Exception {

    }
}