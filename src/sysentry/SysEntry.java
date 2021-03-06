package sysentry;

/**
 * @author David Allison
 * All entries in the tree should be of this type
 */
public interface SysEntry extends Visitable {
	public String toString();
	public long getCreationTime();
	public String getPrettyCreationTime();
}