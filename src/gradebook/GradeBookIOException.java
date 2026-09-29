package gradebook;

/**
 * Thrown when the roster cannot be written to disk. Wraps the low-level
 * IOException so callers get a clear, GradeBook-specific error instead of
 * a raw java.io exception leaking out of the API.
 */
public class GradeBookIOException extends RuntimeException {

    public GradeBookIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
