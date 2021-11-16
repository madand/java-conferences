package net.madand.conferences.util;

/**
 * Functional interface that designates an exception handler that itself throws an exception.
 *
 * @param <IE> type of the handled (incoming) exception.
 * @param <OE> type of the exception, thrown by the handler.
 */
@FunctionalInterface
public interface ThrowingExceptionHandler<IE extends Throwable, OE extends Throwable> {
    /**
     * Perform appropriate handling of the given exception, then throw a different exception.
     *
     * @param exception the exception to be handled.
     * @throws OE the type of the exception, thrown by the handler.
     */
    void handle(IE exception) throws OE;
}
