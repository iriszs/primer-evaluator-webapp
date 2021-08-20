package nl.bioinf.imgorter.primer_evaluator.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates the returned value might be null and should be null-checked
 * Used when a parameter might return null
 */
@Documented
@Target(ElementType.METHOD)
public @interface Nullable {}
