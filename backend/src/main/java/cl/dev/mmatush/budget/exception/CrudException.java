package cl.dev.mmatush.budget.exception;

import org.springframework.dao.DataAccessException;

import java.io.Serial;
import java.io.Serializable;

public class CrudException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = -6733477056755247543L;

    private static final String ERROR_DATA_ACCESS_EXCEPTION = "E000 internal operation";
    private static final String ERROR_READ_RECORD = "E001 retrieving transaction with id: ";
    private static final String ERROR_CREATE_RECORD = "E002 creating transaction";
    private static final String ERROR_UPDATE_RECORD = "E003 updating transaction with id: ";
    private static final String ERROR_DELETE_RECORD = "E004 deleting transaction with id: ";
    private static final String ERROR_READ_COLLECTION = "E005 reading transaction collection";
    private static final String ERROR_CREATE_COLLECTION = "E006 creating transaction collection";

    public CrudException(String message) {
        super(message);
    }

    public CrudException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CrudException readException(String id, Throwable cause) {
        return new CrudException(ERROR_READ_RECORD + id, cause);
    }

    public static CrudException createException(Throwable cause) {
        return new CrudException(ERROR_CREATE_RECORD, cause);
    }

    public static CrudException updateException(Throwable cause) {
        return new CrudException(ERROR_UPDATE_RECORD, cause);
    }

    public static CrudException deleteException(String id, Throwable cause) {
        return new CrudException(ERROR_DELETE_RECORD + id, cause);
    }

    public static CrudException readCollectionException(Throwable cause) {
        return new CrudException(ERROR_READ_COLLECTION, cause);
    }

    public static CrudException createCollectionException(Throwable cause) {
        return new CrudException(ERROR_CREATE_COLLECTION, cause);
    }

    public static CrudException fromDataAccess(DataAccessException e) {
        return new CrudException(ERROR_DATA_ACCESS_EXCEPTION, e);
    }

}
