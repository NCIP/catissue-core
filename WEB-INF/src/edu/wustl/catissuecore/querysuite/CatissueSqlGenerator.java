package edu.wustl.catissuecore.querysuite;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IDateOffset;
import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class CatissueSqlGenerator extends SqlGenerator {
    @Override
    protected String getCustomFormulaString(ICustomFormula formula) {
        formula = modifyForRounding(formula);
        return super.getCustomFormulaString(formula);
    }

    private static class CaTissueFormula {
        private IArithmeticOperand lhsOpnd1;

        private ArithmeticOperator lhsOper;

        private IArithmeticOperand lhsOpnd2;

        private RelationalOperator relOper;

        private IArithmeticOperand rhs;

        private QueryType queryType;

        private enum QueryType {
            rhsDate, rhsOffset;
            private static QueryType fromTermType(TermType termType) {
                return TermType.isInterval(termType) ? rhsOffset : rhsDate;
            }
        }

        CaTissueFormula(ICustomFormula formula) {
            if (!formula.isValid()) {
                throw new IllegalArgumentException("invalid custom formula.");
            }
            rhs = formula.getAllRhs().get(0).getOperand(0);
            lhsOpnd1 = formula.getLhs().getOperand(0);
            lhsOpnd2 = formula.getLhs().getOperand(1);
            lhsOper = formula.getLhs().getConnector(0, 1).getOperator();
            relOper = formula.getOperator();

            queryType = QueryType.fromTermType(rhs.getTermType());
        }

        NormalizedFormula normalizedForm() {
            NormalizedFormula res = new NormalizedFormula();
            res.relOper = relOper;
            switch (queryType) {
                case rhsDate :
                    res.rhsDate = rhs;
                    res.lhsOper = lhsOper;
                    // formula is valid; assume +/- are correct.
                    if (lhsOpnd1.getTermType() == TermType.Date) {
                        res.lhsDate = lhsOpnd1;
                        res.lhsOffset = lhsOpnd2;
                    } else {
                        res.lhsDate = lhsOpnd2;
                        res.lhsOffset = lhsOpnd1;
                    }
                    break;
                case rhsOffset :
                    res.rhsDate = lhsOpnd2;
                    res.lhsDate = lhsOpnd1;
                    res.lhsOffset = rhs;
                    res.lhsOper = ArithmeticOperator.Minus;
                    break;
                default :
                    throw new RuntimeException("can't occur.");
            }
            return res;
        }
    }

    private static class NormalizedFormula {
        // date (+/-) Interval = date
        private IArithmeticOperand lhsDate;

        private ArithmeticOperator lhsOper;

        private IArithmeticOperand lhsOffset;

        private IArithmeticOperand rhsDate;

        private RelationalOperator relOper;

        private TimeInterval<?> timeInterval() {
            if (lhsOffset.getTermType() == TermType.Numeric) {
                return TimeInterval.Day;
            }
            return ((IDateOffset) lhsOffset).getTimeInterval();
        }

        private ICustomFormula roundedGenericForm() {
            ICustomFormula res = QueryObjectFactory.createCustomFormula();
            res.getLhs().addOperand(lhsDate);
            res.getLhs().addOperand(conn(lhsOper), lhsOffset);

            if (relOper == RelationalOperator.Equals) {
                res.setOperator(RelationalOperator.Between);
                res.addRhs(rhsLower());
                res.addRhs(rhsUpper());
                return res;
            }
            res.setOperator(relOper);
            ITerm rhs;
            if (relOper == RelationalOperator.LessThanOrEquals) {
                rhs = rhsUpper();
            } else if (relOper == RelationalOperator.GreaterThanOrEquals) {
                rhs = rhsLower();
            } else {
                rhs = rhsTerm();
            }
            res.addRhs(rhs);
            return res;
        }

        private ITerm rhsUpper() {
            ITerm term = rhsTerm();
            term.addOperand(conn(ArithmeticOperator.Plus), roundingOffset());
            return term;
        }

        private ITerm rhsLower() {
            ITerm term = rhsTerm();
            term.addOperand(conn(ArithmeticOperator.Minus), roundingOffset());
            return term;
        }

        private ITerm rhsTerm() {
            ITerm term = QueryObjectFactory.createTerm();
            term.addOperand(rhsDate);
//            term.addOperand(conn(ArithmeticOperator.Unknown), roundingOffset());
            return term;
        }

        private IArithmeticOperand roundingOffset() {
            TimeInterval<?> timeInterval = timeInterval();
            if (timeInterval.equals(TimeInterval.Second)) {
                return offset(0, TimeInterval.Second);
            }
            if (timeInterval.equals(TimeInterval.Minute)) {
                return offset(30, TimeInterval.Second);
            }
            if (timeInterval.equals(TimeInterval.Hour)) {
                return offset(30, TimeInterval.Minute);
            }
            if (timeInterval.equals(TimeInterval.Day)) {
                return offset(12, TimeInterval.Hour);
            }
            if (timeInterval.equals(TimeInterval.Week)) {
                // 3.5 * 24
                return offset(84, TimeInterval.Hour);
            }
            if (timeInterval.equals(TimeInterval.Month)) {
                return offset(15, TimeInterval.Day);
            }
            if (timeInterval.equals(TimeInterval.Quarter)) {
                return offset(45, TimeInterval.Day);
            }
            if (timeInterval.equals(TimeInterval.Year)) {
                return offset(6, TimeInterval.Month);
            }
            throw new RuntimeException("won't occur.");
        }

        private static IDateOffsetLiteral offset(int offset, TimeInterval<?> timeInterval) {
            return QueryObjectFactory.createDateOffsetLiteral(String.valueOf(offset), timeInterval);
        }

        private static IConnector<ArithmeticOperator> conn(ArithmeticOperator oper) {
            return QueryObjectFactory.createArithmeticConnector(oper, 0);
        }
    }

    private ICustomFormula modifyForRounding(ICustomFormula formula) {
        if (formula.getAllRhs().isEmpty()) {
            return formula;
        }
        CaTissueFormula caTissueFormula = new CaTissueFormula(formula);
        if (!isTemporal(caTissueFormula)) {
            return formula;
        }
        NormalizedFormula normalizedFormula = caTissueFormula.normalizedForm();
        return normalizedFormula.roundedGenericForm();
    }

    private boolean isTemporal(CaTissueFormula caTissueFormula) {
        TermType rhsType = caTissueFormula.rhs.getTermType();
        return TermType.isInterval(rhsType) || rhsType == TermType.Timestamp || rhsType == TermType.Date;
    }
}
