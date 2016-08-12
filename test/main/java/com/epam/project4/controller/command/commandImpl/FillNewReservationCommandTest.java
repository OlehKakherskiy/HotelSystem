package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.manager.AbstractServiceManager;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValueTuple;
import main.java.com.epam.project4.model.entity.roomParameter.Value;
import main.java.com.epam.project4.model.service.IParameterValueService;
import main.java.com.epam.project4.model.service.IReservationService;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class FillNewReservationCommandTest {

    private static HttpServletRequest request;

    private static HttpSession session;

    private static AbstractServiceManager serviceManager;

    private static IReservationService reservationService;

    private static IParameterValueService parameterValueService;

    private static List<Parameter> parameters;

    private static List<Value> values;

    private static List<ParameterValueTuple> parameterValueTuples;

    @BeforeClass
    public static void beforeClass() {
        parameters = Arrays.asList(new Parameter(1, true, null, "paramName1"), new Parameter(2, true, null, "paramName2"));
        values = Arrays.asList(new Value(1, "value1"), new Value(2, "value2"));
        parameterValueTuples = Arrays.asList(new ParameterValueTuple(5, parameters.get(0), values.get(0), 1), new ParameterValueTuple(6, parameters.get(1), values.get(1), 20));

    }

    @Test
    public void process() throws Exception {
        Reservation expectedReservation = new Reservation(1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1),
                LocalDate.now(), ReservationStatus.PROCESSING, 0, 1, null, Collections.emptyList());

        request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter("dateFrom")).andReturn(LocalDate.now().minusDays(1).toString());
        EasyMock.expect(request.getParameter("dateTo")).andReturn(LocalDate.now().plusDays(1).toString());

        session = EasyMock.createMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GlobalContextConstant.USER.getName())).andReturn(new User(1, "na", "d", UserType.ADMIN, Collections.emptyList()));
        EasyMock.expect(request.getSession()).andReturn(session);

        serviceManager = EasyMock.createMock(AbstractServiceManager.class);
        reservationService = EasyMock.createMock(IReservationService.class);
        parameterValueService = EasyMock.createMock(IParameterValueService.class);

        EasyMock.expect(serviceManager.getInstance(IReservationService.class)).andReturn(reservationService);
        EasyMock.expect(serviceManager.getInstance(IParameterValueService.class)).andReturn(parameterValueService);

        EasyMock.expect(request.getParameter("paramName1")).andReturn(String.valueOf(5));
        EasyMock.expect(request.getParameter("paramName2")).andReturn(String.valueOf(6));

        Map<Parameter, List<ParameterValueTuple>> parameterListMap = new HashMap<>();
        parameterListMap.put(parameters.get(0), Arrays.asList(parameterValueTuples.get(0)));
        parameterListMap.put(parameters.get(1), Arrays.asList(parameterValueTuples.get(1)));
        EasyMock.expect(parameterValueService.getParameterValueMap()).andReturn(parameterListMap);
        EasyMock.expect(parameterValueService.getParamValueList(Arrays.asList(5, 6))).andReturn(Arrays.asList(parameterValueTuples.get(0), parameterValueTuples.get(1)));

        Capture<Reservation> capture = new Capture<>();
//        Capture<User> userCapture = new Capture<>();
//        EasyMock.expect(reservationService.addReservation(null, null)).and
        reservationService.addReservation(EasyMock.and(EasyMock.isA(Reservation.class), expectedReservation), new User(1, "na", "d", UserType.ADMIN, Collections.emptyList())); //(User) EasyMock.and(EasyMock.isA(User.class), userCapture)

        EasyMock.replay(request, session, serviceManager, reservationService, parameterValueService);

        AbstractCommand command = new FillNewReservationCommand();
        command.setServiceManager(serviceManager);
        command.process(request, null);


        Assert.assertEquals(expectedReservation, capture.getValue());
        EasyMock.verify(request, session, serviceManager, reservationService, parameterValueService);
    }
}