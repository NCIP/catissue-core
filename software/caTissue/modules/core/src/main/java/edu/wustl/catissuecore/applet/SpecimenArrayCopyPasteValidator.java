/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.applet;

/**
 * <p>This class initializes the fields of SpecimenArrayCopyPasteValidator.java</p>.
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayCopyPasteValidator extends BaseCopyPasteValidator
{

	/**
	 * Specify the serialVersionUID field.
	 */
	private static final long serialVersionUID = -5048273801783877291L;

	/**
	 * @param validatorModel validator model
	 */
	public SpecimenArrayCopyPasteValidator(CopyPasteOperationValidatorModel validatorModel)
	{
		super(validatorModel);
	}

	/**
	 * @see edu.wustl.catissuecore.applet.BaseCopyPasteValidator#doValidate()
	 * @return String
	 */
	@Override
	protected String doValidate()
	{
		return "";
	}

}
