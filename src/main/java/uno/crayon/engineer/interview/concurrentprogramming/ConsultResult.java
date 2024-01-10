package uno.crayon.engineer.interview.concurrentprogramming;

import lombok.Data;

/**
 * @author
 */
@Data
public class ConsultResult {
    private boolean isEnable;
    private String errorCode;

    public ConsultResult(boolean isEnable, String errorCode) {
        this.isEnable = isEnable;
        this.errorCode = errorCode;
    }

    public ConsultResult() {
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
