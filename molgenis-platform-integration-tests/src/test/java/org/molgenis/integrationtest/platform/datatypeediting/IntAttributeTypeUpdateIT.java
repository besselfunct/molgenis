package org.molgenis.integrationtest.platform.datatypeediting;

import org.molgenis.data.meta.AttributeType;
import org.molgenis.data.validation.ValidationException;
import org.molgenis.integrationtest.platform.PlatformITConfig;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.*;

import static org.molgenis.data.meta.AttributeType.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

@ContextConfiguration(classes = { PlatformITConfig.class })
public class IntAttributeTypeUpdateIT extends AbstractAttributeTypeUpdateIT
{
	@BeforeClass
	private void setup()
	{
		super.setup(INT, INT);
	}

	@AfterMethod
	private void afterMethod()
	{
		super.afterMethod(INT);
	}

	@AfterClass
	public void afterClass()
	{
		super.afterClass();
	}

	@DataProvider(name = "validConversionData")
	public Object[][] validConversionData()
	{
		return new Object[][] { { 1, STRING, "1" }, { 0, BOOL, false }, { 1, BOOL, true }, { 1, TEXT, "1" },
				{ 1, DECIMAL, 1.0 }, { 1, LONG, 1L }, { 1, XREF, "label1" }, { 1, CATEGORICAL, "label1" },
				{ 1, ENUM, "1" } };
	}

	/**
	 * Valid conversion cases for INT to:
	 * STRING, TEXT, DECIMAL, LONG, BOOL, ENUM
	 *
	 * @param valueToConvert  The value that will be converted
	 * @param typeToConvertTo The type to convert to
	 * @param convertedValue  The expected value after converting the type
	 */
	@Test(dataProvider = "validConversionData")
	public void testValidConversion(int valueToConvert, AttributeType typeToConvertTo, Object convertedValue)
	{
		testTypeConversion(valueToConvert, typeToConvertTo);

		// Assert if conversion was successful
		assertEquals(getActualDataType(), typeToConvertTo);
		assertEquals(getActualValue(), convertedValue);
	}

	@DataProvider(name = "invalidConversionTestCases")
	public Object[][] invalidConversionTestCases()
	{
		return new Object[][] { { 10, XREF, "type:MAINENTITY attribute:mainAttribute value: 10" },
				{ 10, CATEGORICAL, "type:MAINENTITY attribute:mainAttribute value: 10" }, { 10, EMAIL,
				"Attribute data type update from [INT] to [EMAIL] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				{ 10, HYPERLINK,
						"Attribute data type update from [INT] to [HYPERLINK] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				{ 10, HTML,
						"Attribute data type update from [INT] to [HTML] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				{ 10, ENUM, "type:MAINENTITY" }, { 10, DATE,
				"Attribute data type update from [INT] to [DATE] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				{ 10, DATE_TIME,
						"Attribute data type update from [INT] to [DATE_TIME] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				{ 10, MREF,
						"Attribute data type update from [INT] to [MREF] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				{ 10, CATEGORICAL_MREF,
						"Attribute data type update from [INT] to [CATEGORICAL_MREF] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				{ 10, FILE,
						"Attribute data type update from [INT] to [FILE] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				{ 10, COMPOUND,
						"Attribute data type update from [INT] to [COMPOUND] not allowed, allowed types are [BOOL, CATEGORICAL, DECIMAL, ENUM, LONG, STRING, TEXT, XREF]" },
				new Object[] { 10, ONE_TO_MANY,
						"Invalid [xref] value [] for attribute [Referenced entity] of entity [mainAttribute] with type [sys_md_Attribute]. Offended validation expression: $('refEntityType').isNull().and($('type').matches(/^(categorical|categoricalmref|file|mref|onetomany|xref)$/).not()).or($('refEntityType').isNull().not().and($('type').matches(/^(categorical|categoricalmref|file|mref|onetomany|xref)$/))).value().Invalid [xref] value [] for attribute [Mapped by] of entity [mainAttribute] with type [sys_md_Attribute]. Offended validation expression: $('mappedBy').isNull().and($('type').eq('onetomany').not()).or($('mappedBy').isNull().not().and($('type').eq('onetomany'))).value()" } };
	}

	/**
	 * Invalid conversions cases for INT to:
	 * XREF, CATEGORICAL, EMAIL, HYPERLINK, HTML, ENUM, DATE, DATE_TIME, MREF, CATEGORICAL_MREF, FILE, COMPOUND, ONE_TO_MANY
	 *
	 * @param valueToConvert   The value that will be converted
	 * @param typeToConvertTo  The type to convert to
	 * @param exceptionClass   The expected class of the exception that will be thrown
	 * @param exceptionMessage The expected exception message
	 */
	@Test(dataProvider = "invalidConversionTestCases")
	public void testInvalidConversions(int valueToConvert, AttributeType typeToConvertTo, String exceptionMessage)
	{
		try
		{
			testTypeConversion(valueToConvert, typeToConvertTo);
			fail("Conversion should have failed");
		}
		catch (ValidationException e)
		{
			assertEquals(e.getMessage(), exceptionMessage);
		}
	}
}
