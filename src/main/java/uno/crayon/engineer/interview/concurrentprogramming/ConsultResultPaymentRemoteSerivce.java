package uno.crayon.engineer.interview.concurrentprogramming;

/**
 * @author
 */
public class ConsultResultPaymentRemoteSerivce {
    public static ConsultResult isEnabled(String paymentType) {
        ConsultResult consultResult = null;
        switch (paymentType) {
            case "余额": consultResult = new ConsultResult(true, "20000");
            case "余额包": consultResult = new ConsultResult(true, "20000");
            case "红包": consultResult = new ConsultResult(true, "50000");
            default: consultResult = new ConsultResult(false, "7777");
        }
        return consultResult;
    }
}
