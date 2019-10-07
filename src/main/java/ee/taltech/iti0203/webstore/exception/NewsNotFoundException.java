package ee.taltech.iti0203.webstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "News item Not Found")
public class NewsNotFoundException extends RuntimeException {
}
