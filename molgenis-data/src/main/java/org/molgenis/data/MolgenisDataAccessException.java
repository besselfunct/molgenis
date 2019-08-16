package org.molgenis.data;

import org.molgenis.util.exception.CodedRuntimeException;

/** @deprecated use class that extends from {@link CodedRuntimeException} */
@Deprecated
public class MolgenisDataAccessException extends RuntimeException {
  private static final long serialVersionUID = 4738825795930038340L;

  public MolgenisDataAccessException() {}

  public MolgenisDataAccessException(String msg) {
    super(msg);
  }

  public MolgenisDataAccessException(Throwable t) {
    super(t);
  }

  public MolgenisDataAccessException(String msg, Throwable t) {
    super(msg, t);
  }
}
