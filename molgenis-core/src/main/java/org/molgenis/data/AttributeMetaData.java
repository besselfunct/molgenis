package org.molgenis.data;

import org.molgenis.fieldtypes.FieldType;

/**
 * Attribute defines the properties of an entity. Synonyms: feature, column, data item.
 */
public interface AttributeMetaData
{
	/**
	 * Name of the attribute
	 */
	String getName();

	/**
	 * Description of the attribute
	 */
	String getDescription();

	/**
	 * DataType of the attribute
	 */
	FieldType getDataType();

	/**
	 * Whether attribute has not null constraint
	 */
	boolean isNillable();

	/**
	 * Whether attribute is readonly
	 */
	boolean isReadonly();

	/**
	 * Whether attribute must be unique
	 */
	boolean isUnique();

	/**
	 * Default value
	 */
	Object getDefaultValue();

	/**
	 * Whether attribute is primary key
	 */
	boolean isIdAtrribute();

	/**
	 * Whether attribute is human readable key
	 */
	boolean isLabelAttribute();

	/**
	 * When getDataType=xref/mref, get other end of xref
	 */
	EntityMetaData getRefEntity();

}
