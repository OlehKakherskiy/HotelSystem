package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.MessageCode;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.ReservationStatus;
import main.java.com.hotelSystem.model.roomParameter.Parameter;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;
import main.java.com.hotelSystem.service.IParameterValueService;
import main.java.com.hotelSystem.service.IReservationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class FillNewReservationCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(FillNewReservationCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        IReservationService reservationService = serviceManager.getInstance(IReservationService.class);
        User user = (User) request.getSession(false).getAttribute(GlobalContextConstant.USER.getName());
        Reservation reservation = buildReservation(request);
        reservationService.addReservation(reservation, user);

        logger.info(MessageFormat.format("User (id = {0}) added new reservation request with params: dateFrom = {1}, " +
                        "dateTo = {2}, comment = {3}. New reservation id = {4}", reservation.getDateFrom(), reservation.getDateTo(),
                reservation.getComment(), reservation.getId()));

        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }

    private Reservation buildReservation(HttpServletRequest request) {
        Reservation reservation = new Reservation();

        String startDateStr = request.getParameter("dateFrom");
        if (startDateStr == null || startDateStr.isEmpty()) {
            throw new RequestException(MessageCode.NO_DATE_FROM_PARAMETER);
        }

        String endDateStr = request.getParameter("dateTo");
        if (endDateStr == null || endDateStr.isEmpty()) {
            throw new RequestException(MessageCode.NO_DATE_TO_PARAMETER);
        }
        LocalDate dateFrom = LocalDate.parse(startDateStr);
        LocalDate dateTo = LocalDate.parse(endDateStr);
        LocalDate reqDate = LocalDate.now();

        if (dateFrom.isBefore(reqDate)) {
            throw new RequestException(MessageCode.DATE_FROM_IS_BEFORE_REQ_DATE_EXCEPTION);
        }
        if (dateTo.isBefore(reqDate)) {
            throw new RequestException(MessageCode.DATE_TO_IS_BEFORE_REQ_DATE_EXCEPTION);
        }
        if (dateTo.isBefore(dateFrom)) {
            throw new RequestException(MessageCode.DATE_TO_IS_BEFORE_DATE_FROM_EXCEPTION);
        }
        reservation.setRequestDate(LocalDate.now());
        reservation.setDateFrom(LocalDate.parse(startDateStr));
        reservation.setDateTo(LocalDate.parse(endDateStr));
        reservation.setComment(request.getParameter("comment"));
        reservation.setRequestParameters(getParameterValueList(request));
        reservation.setStatus(ReservationStatus.PROCESSING);

        return reservation;
    }


    private List<ParameterValueTuple> getParameterValueList(HttpServletRequest request) {
        IParameterValueService parameterValueService = serviceManager.getInstance(IParameterValueService.class);
        return parameterValueService.getParamValueList(parseParameterValueList(request, parameterValueService));
    }

    private List<Integer> parseParameterValueList(HttpServletRequest request, IParameterValueService parameterValueService) {
        Map<Parameter, List<ParameterValueTuple>> parameterParameterValueMap = parameterValueService.getParameterValueMap();
        List<Integer> result = new ArrayList<>();

        parameterParameterValueMap.keySet().stream().forEach(key -> result.add(Integer.parseInt(request.getParameter(key.getParamName()))));
        return result;
    }
}