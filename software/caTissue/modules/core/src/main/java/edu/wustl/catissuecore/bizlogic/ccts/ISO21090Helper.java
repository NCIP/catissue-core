/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.CollectionUtils;

import edu.duke.cabig.c3pr.webservice.iso21090.AD;
import edu.duke.cabig.c3pr.webservice.iso21090.ADXP;
import edu.duke.cabig.c3pr.webservice.iso21090.ANY;
import edu.duke.cabig.c3pr.webservice.iso21090.AddressPartType;
import edu.duke.cabig.c3pr.webservice.iso21090.BAGTEL;
import edu.duke.cabig.c3pr.webservice.iso21090.BL;
import edu.duke.cabig.c3pr.webservice.iso21090.CD;
import edu.duke.cabig.c3pr.webservice.iso21090.DSETAD;
import edu.duke.cabig.c3pr.webservice.iso21090.DSETCD;
import edu.duke.cabig.c3pr.webservice.iso21090.DSETENPN;
import edu.duke.cabig.c3pr.webservice.iso21090.DSETST;
import edu.duke.cabig.c3pr.webservice.iso21090.ED;
import edu.duke.cabig.c3pr.webservice.iso21090.ENPN;
import edu.duke.cabig.c3pr.webservice.iso21090.ENXP;
import edu.duke.cabig.c3pr.webservice.iso21090.EntityNamePartType;
import edu.duke.cabig.c3pr.webservice.iso21090.II;
import edu.duke.cabig.c3pr.webservice.iso21090.INTPositive;
import edu.duke.cabig.c3pr.webservice.iso21090.IVLTSDateTime;
import edu.duke.cabig.c3pr.webservice.iso21090.NullFlavor;
import edu.duke.cabig.c3pr.webservice.iso21090.ST;
import edu.duke.cabig.c3pr.webservice.iso21090.TEL;
import edu.duke.cabig.c3pr.webservice.iso21090.TSDateTime;
import edu.duke.cabig.c3pr.webservice.iso21090.TelecommunicationAddressUse;
import edu.wustl.common.util.logger.Logger;

/**
 * Helper methods to conveniently create instances of ISO 21090 JAXB classes.
 * Method names do not follow Sun's Java coding conventions <b>intentionally</b>
 * to keep names short and simple.
 * 
 * @author dkrylov
 * 
 */
public final class ISO21090Helper {
	
	private static final Logger logger = Logger
			.getCommonLogger(ISO21090Helper.class);
	
	public static final String TS_DATETIME_PATTERN = "yyyyMMddHHmmss";
	
	/**
	 * We allow creation of instances of this class so that other classes, if
	 * they want, could use very short form -- a variable with a short name --
	 * of referring to this class' methods to improve readability. See usage
	 * examples for details. Other than that, instantiating this class is not
	 * really necessary.
	 */
	public ISO21090Helper() {
	}

	public static final II II(String ext) {
		II ii = new II();
		ii.setExtension(ext);
		return ii;
	}

	public static final II II(NullFlavor nf) {
		II ii = new II();
		ii.setNullFlavor(nf);
		return ii;
	}

	public static final CD CD(String code) {
		CD cd = new CD();
		cd.setCode(code);
		return cd;
	}

	public static final IVLTSDateTime IVLTSDateTime(NullFlavor nf) {
		IVLTSDateTime cd = new IVLTSDateTime();
		cd.setNullFlavor(nf);
		return cd;
	}

	public static final BL BL(Boolean b) {
		BL cd = new BL();
		cd.setValue(b);
		return cd;
	}

	public static final INTPositive INTPositive(Integer i) {
		if (i == null) {
			return INTPositive(NullFlavor.NI);
		}
		INTPositive int1 = new INTPositive();
		int1.setValue(i);
		return int1;
	}

	public static final TSDateTime TSDateTime(String s) {
		TSDateTime dateTime = new TSDateTime();
		dateTime.setValue(s);
		return dateTime;
	}

	public static final TSDateTime TSDateTime(NullFlavor ni) {
		TSDateTime dateTime = new TSDateTime();
		dateTime.setNullFlavor(ni);
		return dateTime;
	}
	
	public static final TSDateTime TSDateTime(Date date) {
		TSDateTime tsDateTime = new TSDateTime();
		if (date != null) {
			tsDateTime.setValue(DateFormatUtils.format(date,
					TS_DATETIME_PATTERN));
		} else {
			tsDateTime.setNullFlavor(NullFlavor.NI);
		}
		return tsDateTime;
	}	

	public static final ST ST(String s) {
		ST st = new ST();
		st.setValue(s);
		return st;
	}

	public static final String str(ST st) {
		if (st == null || st.getValue() == null) {
			return "";
		} else {
			return st.getValue();
		}
	}

	public static final String str(CD cd) {
		if (cd == null || cd.getCode() == null) {
			return "";
		} else {
			return cd.getCode();
		}
	}
	
	public static final String str(II ii) {
		if (ii == null || ii.getExtension() == null) {
			return "";
		} else {
			return ii.getExtension();
		}
	}
	

	public static final DSETST DSETST(List<ST> list) {
		DSETST dsetst = new DSETST();
		dsetst.getItem().addAll(list);
		return dsetst;
	}

	public static final DSETCD DSETCD(CD... cd) {
		DSETCD d = new DSETCD();
		d.getItem().addAll(Arrays.asList(cd));
		return d;
	}

	public static final ENXP ENXP(String s, EntityNamePartType type) {
		ENXP en = new ENXP();
		en.setValue(s);
		en.setType(type);
		return en;
	}

	public static final ENPN ENPN(ENXP... part) {
		ENPN en = new ENPN();
		en.getPart().addAll(Arrays.asList(part));
		return en;
	}

	public static final DSETENPN DSETENPN(ENPN... enpn) {
		DSETENPN en = new DSETENPN();
		en.getItem().addAll(Arrays.asList(enpn));
		return en;
	}

	public static final ADXP ADXP(String s, AddressPartType typ) {
		ADXP ad = new ADXP();
		ad.setType(typ);
		ad.setValue(s);
		return ad;
	}

	public static final AD AD(ADXP... adxps) {
		AD ad = new AD();
		ad.getPart().addAll(Arrays.asList(adxps));
		return ad;
	}

	public static final DSETAD DSETAD(AD... ads) {
		DSETAD ad = new DSETAD();
		ad.getItem().addAll(Arrays.asList(ads));
		return ad;

	}

	public static final TEL TEL(String s, List<TelecommunicationAddressUse> uses) {
		TEL tel = new TEL();
		tel.setValue(s);
		if (!CollectionUtils.isEmpty(uses)) {
			tel.getUse().addAll(uses);
		}
		return tel;
	}

	public static final BAGTEL BAGTEL(TEL... tel) {
		BAGTEL b = new BAGTEL();
		b.getItem().addAll(Arrays.asList(tel));
		return b;
	}

	public static final ED ED(String s) {
		ED ed = new ED();
		ed.setValue(s);
		return ed;
	}

	public static final CD CD(NullFlavor ni) {
		CD cd = new CD();
		cd.setNullFlavor(ni);
		return cd;
	}

	public static final BL BL(NullFlavor ni) {
		BL bl = new BL();
		bl.setNullFlavor(ni);
		return bl;
	}

	public static final INTPositive INTPositive(NullFlavor ni) {
		INTPositive int1 = new INTPositive();
		int1.setNullFlavor(ni);
		return int1;
	}
	
	public static final boolean isNull(ANY cd) {
		return cd == null || cd.getNullFlavor() != null;
	}

	public static final Date toDate(TSDateTime tsDateTime) {
		String value = null;
		try {
			if (tsDateTime != null && tsDateTime.getNullFlavor() == null) {
				value = tsDateTime.getValue();
				if (value != null) {
					return DateUtils.parseDate(value,
							new String[] { TS_DATETIME_PATTERN });
				}
			}
		} catch (ParseException e) {
			logger.error("Unable to parse a date value: " + value);			
		}
		return null;
	}


}
