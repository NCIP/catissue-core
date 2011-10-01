//package edu.wustl.catissuecore.factory;
//
//import edu.wustl.catissuecore.domain.ProcedureEventParameters;
//
//public class ProcedureEventParametersFactory extends AbstractSpecimenEventParametersFactory<ProcedureEventParameters>
//{
//	private static ProcedureEventParametersFactory procedureEventParametersFactory;
//
//	private ProcedureEventParametersFactory()
//	{
//		super();
//	}
//
//	public static synchronized ProcedureEventParametersFactory getInstance()
//	{
//		if(procedureEventParametersFactory==null){
//			procedureEventParametersFactory = new ProcedureEventParametersFactory();
//		}
//		return procedureEventParametersFactory;
//	}
//	public ProcedureEventParameters createClone(ProcedureEventParameters obj)
//	{
//		ProcedureEventParameters procedureEventParameters = createObject();
//		return procedureEventParameters;
//	}
//
//	public ProcedureEventParameters createObject()
//	{
//		ProcedureEventParameters procedureEventParameters = new ProcedureEventParameters();
//		initDefaultValues(procedureEventParameters);
//		return procedureEventParameters;
//	}
//
//	public void initDefaultValues(ProcedureEventParameters obj)
//	{
//	}
//}

