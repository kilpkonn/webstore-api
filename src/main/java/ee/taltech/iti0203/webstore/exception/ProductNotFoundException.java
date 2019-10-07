package ee.taltech.iti0203.webstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Product Not Found")
public class ProductNotFoundException extends RuntimeException {
}
