package sysentry;

import visitor.SysEntryVisitor;

/**
 * @author David Allison
 * Interface for visitor pattern all objects in tree should 
 * implement this.
 */
public interface Visitable {
	int accept(SysEntryVisitor visitor);
}
