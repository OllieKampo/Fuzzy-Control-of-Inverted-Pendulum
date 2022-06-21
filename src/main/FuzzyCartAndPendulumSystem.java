package main;

import java.util.ArrayList;
import java.util.Collections;

public class FuzzyCartAndPendulumSystem extends CartAndPendulumSystem {
	private static final long serialVersionUID = 4934241740727854631L;
	
	public static final String[] DEFAULT_FUZZY_RULES = {"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY HUGE THEN force IS -2000.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY LARGE THEN force IS -1500.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY BIG THEN force IS -1000.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY SMALL THEN force IS -1000.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY LITTLE THEN force IS -500.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY TINY THEN force IS -0.0",
														
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY HUGE THEN force IS -1000.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY LARGE THEN force IS -500.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY BIG THEN force IS -250.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY SMALL THEN force IS -250.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY LITTLE THEN force IS -100.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY TINY THEN force IS -0.0",
														
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY HUGE THEN force IS -500.0",
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY LARGE THEN force IS -250.0",
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY BIG THEN force IS -100.0",
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY SMALL THEN force IS -50.0",
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY LITTLE THEN force IS 50.0",
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY TINY THEN force IS 100.0",
														
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY HUGE THEN force IS -100.0",
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY LARGE THEN force IS -50.0",
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY BIG THEN force IS 50.0",
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY SMALL THEN force IS 100.0",
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY LITTLE THEN force IS 250.0",
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY TINY THEN force IS 500.0",
														
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY HUGE THEN force IS 0.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY LARGE THEN force IS 100.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY BIG THEN force IS 250.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY SMALL THEN force IS 250.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY LITTLE THEN force IS 500.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY TINY THEN force IS 1000.0",
														
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY HUGE THEN force IS 0.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY LARGE THEN force IS 500.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY BIG THEN force IS 1000.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY SMALL THEN force IS 1000.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY LITTLE THEN force IS 1500.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY TINY THEN force IS 2000.0"};
	
	public static final String[] DEFAULT_FUZZY_RULES1 = {"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY HUGE THEN force IS -2000.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY LARGE THEN force IS -1500.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY BIG THEN force IS -1000.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY SMALL THEN force IS -1000.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY LITTLE THEN force IS -500.0",
														"IF pendulum angle IS ANY HUGE AND pendulum angular velocity IS ANY TINY THEN force IS -0.0",
														
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY HUGE THEN force IS -1000.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY LARGE THEN force IS -500.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY BIG THEN force IS -250.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY SMALL THEN force IS -250.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY LITTLE THEN force IS -100.0",
														"IF pendulum angle IS ANY LARGE AND pendulum angular velocity IS ANY TINY THEN force IS -0.0",
														
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY HUGE THEN force IS -500.0",
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY LARGE THEN force IS -250.0",
														
														"IF pendulum angle IS EXTREMELY BIG AND pendulum angular velocity IS ANY BIG THEN force IS -100.0",
														"IF pendulum angle IS VERY BIG AND pendulum angular velocity IS ANY BIG THEN force IS -75.0",
														"IF pendulum angle IS MEDIUM BIG AND pendulum angular velocity IS ANY BIG THEN force IS -50.0",
														"IF pendulum angle IS SOMEWHAT BIG AND pendulum angular velocity IS ANY BIG THEN force IS -25.0",
														"IF pendulum angle IS SLIGHTLY BIG AND pendulum angular velocity IS ANY BIG THEN force IS -10.0",
														
														"IF pendulum angle IS EXTREMELY BIG AND pendulum angular velocity IS ANY SMALL THEN force IS -50.0",
														"IF pendulum angle IS VERY BIG AND pendulum angular velocity IS ANY SMALL THEN force IS -25.0",
														"IF pendulum angle IS MEDIUM BIG AND pendulum angular velocity IS ANY SMALL THEN force IS -0.0",
														"IF pendulum angle IS SOMEWHAT BIG AND pendulum angular velocity IS ANY SMALL THEN force IS 10.0",
														"IF pendulum angle IS SLIGHTLY BIG AND pendulum angular velocity IS ANY SMALL THEN force IS 25.0",
														
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY LITTLE THEN force IS 50.0",
														"IF pendulum angle IS ANY BIG AND pendulum angular velocity IS ANY TINY THEN force IS 100.0",
														
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY HUGE THEN force IS -100.0",
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY LARGE THEN force IS -50.0",
														
														"IF pendulum angle IS EXTREMELY SMALL AND pendulum angular velocity IS ANY BIG THEN force IS -25.0",
														"IF pendulum angle IS VERY SMALL AND pendulum angular velocity IS ANY BIG THEN force IS -10.0",
														"IF pendulum angle IS MEDIUM SMALL AND pendulum angular velocity IS ANY BIG THEN force IS 0.0",
														"IF pendulum angle IS SOMEWHAT SMALL AND pendulum angular velocity IS ANY BIG THEN force IS 25.0",
														"IF pendulum angle IS SLIGHTLY SMALL AND pendulum angular velocity IS ANY BIG THEN force IS 50.0",
														
														"IF pendulum angle IS EXTREMELY SMALL AND pendulum angular velocity IS ANY SMALL THEN force IS 10.0",
														"IF pendulum angle IS VERY SMALL AND pendulum angular velocity IS ANY SMALL THEN force IS 25.0",
														"IF pendulum angle IS MEDIUM SMALL AND pendulum angular velocity IS ANY SMALL THEN force IS 50.0",
														"IF pendulum angle IS SOMEWHAT SMALL AND pendulum angular velocity IS ANY SMALL THEN force IS 75.0",
														"IF pendulum angle IS SLIGHTLY SMALL AND pendulum angular velocity IS ANY SMALL THEN force IS 100.0",
														
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY LITTLE THEN force IS 250.0",
														"IF pendulum angle IS ANY SMALL AND pendulum angular velocity IS ANY TINY THEN force IS 500.0",
														
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY HUGE THEN force IS 0.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY LARGE THEN force IS 100.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY BIG THEN force IS 250.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY SMALL THEN force IS 250.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY LITTLE THEN force IS 500.0",
														"IF pendulum angle IS ANY LITTLE AND pendulum angular velocity IS ANY TINY THEN force IS 1000.0",
														
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY HUGE THEN force IS 0.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY LARGE THEN force IS 500.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY BIG THEN force IS 1000.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY SMALL THEN force IS 1000.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY LITTLE THEN force IS 1500.0",
														"IF pendulum angle IS ANY TINY AND pendulum angular velocity IS ANY TINY THEN force IS 2000.0"};
	
	public static final String[] DEFAULT_FUZZY_RULES2 = {"IF cart position IS ANY HUGE AND pendulum angle IS ANY HUGE THEN force IS -2000.0",
														"IF cart position IS ANY HUGE AND pendulum angle IS ANY LARGE THEN force IS -250.0",
														"IF cart position IS ANY HUGE AND pendulum angle IS ANY BIG THEN force IS -10.0",
														"IF cart position IS ANY HUGE AND pendulum angle IS ANY SMALL THEN force IS 10.0",
														"IF cart position IS ANY HUGE AND pendulum angle IS ANY LITTLE THEN force IS -5000.0",
														"IF cart position IS ANY HUGE AND pendulum angle IS ANY TINY THEN force IS -5000.0",
														
														"IF cart position IS ANY LARGE AND pendulum angle IS ANY HUGE THEN force IS -1000.0",
														"IF cart position IS ANY LARGE AND pendulum angle IS ANY LARGE THEN force IS -50.0",
														"IF cart position IS ANY LARGE AND pendulum angle IS ANY BIG THEN force IS -10.0",
														"IF cart position IS ANY LARGE AND pendulum angle IS ANY SMALL THEN force IS 10.0",
														"IF cart position IS ANY LARGE AND pendulum angle IS ANY LITTLE THEN force IS -5000.0",
														"IF cart position IS ANY LARGE AND pendulum angle IS ANY TINY THEN force IS -5000.0",
														
														"IF cart position IS ANY BIG AND pendulum angle IS ANY HUGE THEN force IS 5000.0",
														"IF cart position IS ANY BIG AND pendulum angle IS ANY LARGE THEN force IS -10.0",
														"IF cart position IS ANY BIG AND pendulum angle IS ANY BIG THEN force IS -5.0",
														"IF cart position IS ANY BIG AND pendulum angle IS ANY SMALL THEN force IS 5.0",
														"IF cart position IS ANY BIG AND pendulum angle IS ANY LITTLE THEN force IS -5000.0",
														"IF cart position IS ANY BIG AND pendulum angle IS ANY TINY THEN force IS -5000.0",
														
														"IF cart position IS ANY SMALL AND pendulum angle IS ANY HUGE THEN force IS 5000.0",
														"IF cart position IS ANY SMALL AND pendulum angle IS ANY LARGE THEN force IS 5000.0",
														"IF cart position IS ANY SMALL AND pendulum angle IS ANY BIG THEN force IS -5.0",
														"IF cart position IS ANY SMALL AND pendulum angle IS ANY SMALL THEN force IS 5.0",
														"IF cart position IS ANY SMALL AND pendulum angle IS ANY LITTLE THEN force IS 10.0",
														"IF cart position IS ANY SMALL AND pendulum angle IS ANY TINY THEN force IS -5000.0",
														
														"IF cart position IS ANY LITTLE AND pendulum angle IS ANY HUGE THEN force IS 5000.0",
														"IF cart position IS ANY LITTLE AND pendulum angle IS ANY LARGE THEN force IS 5000.0",
														"IF cart position IS ANY LITTLE AND pendulum angle IS ANY BIG THEN force IS -10.0",
														"IF cart position IS ANY LITTLE AND pendulum angle IS ANY SMALL THEN force IS 10.0",
														"IF cart position IS ANY LITTLE AND pendulum angle IS ANY LITTLE THEN force IS 50.0",
														"IF cart position IS ANY LITTLE AND pendulum angle IS ANY TINY THEN force IS 1000.0",
														
														"IF cart position IS ANY TINY AND pendulum angle IS ANY HUGE THEN force IS 5000.0",
														"IF cart position IS ANY TINY AND pendulum angle IS ANY LARGE THEN force IS 5000.0",
														"IF cart position IS ANY TINY AND pendulum angle IS ANY BIG THEN force IS -10.0",
														"IF cart position IS ANY TINY AND pendulum angle IS ANY SMALL THEN force IS 10.0",
														"IF cart position IS ANY TINY AND pendulum angle IS ANY LITTLE THEN force IS 250.0",
														"IF cart position IS ANY TINY AND pendulum angle IS ANY TINY THEN force IS 2000.0",
														
														"IF pendulum angular velocity IS ANY HUGE AND cart position IS NOT ANY TINY AND cart position IS NOT ANY LITTLE AND cart position IS NOT ANY LARGE AND cart position IS NOT ANY HUGE THEN force IS -200.0",
														"IF pendulum angular velocity IS ANY LARGE AND cart position IS NOT ANY TINY AND cart position IS NOT ANY LITTLE AND cart position IS NOT ANY LARGE AND cart position IS NOT ANY HUGE THEN force IS -100.0",
														"IF pendulum angular velocity IS ANY BIG AND cart position IS NOT ANY TINY AND cart position IS NOT ANY LITTLE AND cart position IS NOT ANY LARGE AND cart position IS NOT ANY HUGE THEN force IS -50.0",
														"IF pendulum angular velocity IS ANY SMALL AND cart position IS NOT ANY TINY AND cart position IS NOT ANY LITTLE AND cart position IS NOT ANY LARGE AND cart position IS NOT ANY HUGE THEN force IS 50.0",
														"IF pendulum angular velocity IS ANY LITTLE AND cart position IS NOT ANY TINY AND cart position IS NOT ANY LITTLE AND cart position IS NOT ANY LARGE AND cart position IS NOT ANY HUGE THEN force IS 100.0",
														"IF pendulum angular velocity IS ANY TINY AND cart position IS NOT ANY TINY AND cart position IS NOT ANY LITTLE AND cart position IS NOT ANY LARGE AND cart position IS NOT ANY HUGE THEN force IS 200.0"};
	
	public static final String[] DEFAULT_FUZZY_VARIABLE_NAMES = {"cart position", "cart velocity", "pendulum angle", "pendulum angular velocity"};
	
	public static final FuzzyVariable[] DEFAULT_FUZZY_VARIABLES = {new FuzzyVariable("cart position", (CartAndPendulumSystem.CART_LENGTH / 2) - 1, (CartAndPendulumSystem.TRACK_LENGTH + 1) - (CartAndPendulumSystem.CART_LENGTH / 2)),
																   new FuzzyVariable("cart velocity", -10.0, 10.0),
																   new FuzzyVariable("pendulum angle", 0.099, Math.PI - 0.099),
																   new FuzzyVariable("pendulum angular velocity", -75.0 / NANO_UNITS_PER_STD_UNIT, 75.0 / NANO_UNITS_PER_STD_UNIT)};
	
	public static final String[] DEFAULT_MEMBER_FUNCTION_NAMES = {"HUGE", "LARGE", "BIG", "SMALL", "LITTLE", "TINY"};
	
	public static final String[] DEFAULT_MEMBER_FUNCTION_NAMES_WITH_COLOURS = {"HUGE (Blue)", "LARGE (Red)", "BIG (Green)", "SMALL (Cyan)", "LITTLE (Orange)", "TINY (Magenta)"};
	
	public static final MemberFunction[] DEFAULT_MEMBER_FUNCTIONS = {new MemberFunction("HUGE", 0.72727272, 0.81818181, 1.0, 1.0),
																	 new MemberFunction("LARGE", 0.54545454, 0.63636363, 0.72727272, 0.81818181),
																	 new MemberFunction("BIG", 0.45454545, 0.54545454, 0.54545454, 0.63636363),
																	 new MemberFunction("SMALL", 0.36363636, 0.45454545, 0.45454545, 0.54545454),
																	 new MemberFunction("LITTLE", 0.18181818, 0.27272727, 0.36363636, 0.45454545),
																	 new MemberFunction("TINY", 0.0, 0.0, 0.18181818, 0.27272727)};
	
	public static final String[] DEFAULT_HEDGE_NAMES = {"EXTREMELY", "VERY", "MEDIUM", "SOMEWHAT", "SLIGHTLY", "ABS_MAX", "ABS_MEDIAN", "ABS_MIN", "ANY"};
	
	public static final String[] DEFAULT_HEDGE_NAMES_WITH_COLOURS = {"EXTREMELY (Blue)", "VERY (Red)", "MEDIUM (Green)", "SOMEWHAT (Cyan)", "SLIGHTLY (Orange)", "ABS_MAX (Yellow)", "ABS_MEDIAN (Pink)", "ABS_MIN (Gray)", "ANY (Magenta)"};
	
	public static final Hedge[] DEFAULT_HEDGES = {new Hedge("EXTREMELY", 0.72727272, 0.81818181, 1.0, 1.0),
												  new Hedge("VERY", 0.54545454, 0.63636363, 0.72727272, 0.81818181),
												  new Hedge("MEDIUM", 0.36363636, 0.45454545, 0.54545454, 0.63636363),
												  new Hedge("SOMEWHAT", 0.18181818, 0.27272727, 0.36363636, 0.45454545),
												  new Hedge("SLIGHTLY", 0.0, 0.0, 0.18181818, 0.27272727),
												  new Hedge("ABS_MAX", 0.99, 0.99, 1.0, 1.0),
												  new Hedge("ABS_MEDIAN", 0.49, 0.49, 0.51, 0.51),
												  new Hedge("ABS_MIN", 0.0, 0.0, 0.01, 0.01),
												  new Hedge("ANY", 0.0, 0.0, 1.0, 1.0)};
	
	private final FuzzySet fuzzySet;
	private final ArrayList<FuzzyRule> fuzzyRules;

	public FuzzyCartAndPendulumSystem(Client client) {
		super(client, "fuzzy");
		this.fuzzySet = new FuzzySet(this);
		this.fuzzyRules = new ArrayList<>();
		for (int i = 0; i < DEFAULT_FUZZY_RULES.length; i++) {
			this.addFuzzyRule(DEFAULT_FUZZY_RULES[i]);
		}
	}

	@Override
	protected void control(long deltaNanoSeconds) {
		double num = 0.0, den = 0.0, temp = 0.0;
		for (int i = 0; i < this.fuzzyRules.size(); i++) {
			temp = this.fuzzySet.fuzzy_if(this.fuzzyRules.get(i));
			num += temp * this.fuzzyRules.get(i).outVal;
			den += temp;
		}
		double force = (num / den) / NANO_UNITS_PER_STD_UNIT;
		if (!Double.isNaN(force)) {
			if (this.useNoise()) {
				this.setForceWithNoise(force);
			} else {
				this.setForce(force);
			}
		} else {
			this.setForce(0.0);
		}
	}
	
	public String[] getFuzzyRuleList() {
		String[] s = new String[fuzzyRules.size()];
		for (int i = 0; i < this.fuzzyRules.size(); i++) {
			s[i] = this.fuzzyRules.get(i).toString();
		}
		return s;
	}
	
	public MemberFunction[] getCopyOfCurrentMemberFunctions() {
		MemberFunction[] mfs = this.fuzzySet.memberFunctions.toArray(new MemberFunction[this.fuzzySet.memberFunctions.size()]);
		MemberFunction[] mfsNew = new MemberFunction[mfs.length];
		for (int i = 0; i < mfs.length; i++) {
			mfsNew[i] = new MemberFunction(mfs[i].name, mfs[i].start, mfs[i].firstPeek, mfs[i].secondPeek, mfs[i].end);
		}
		return mfsNew;
	}
	
	public Hedge[] getCopyOfCurrentHedges() {
		Hedge[] mfs = this.fuzzySet.hedges.toArray(new Hedge[this.fuzzySet.hedges.size()]);
		Hedge[] mfsNew = new Hedge[mfs.length];
		for (int i = 0; i < mfs.length; i++) {
			mfsNew[i] = new Hedge(mfs[i].name, mfs[i].start, mfs[i].firstPeek, mfs[i].secondPeek, mfs[i].end);
		}
		return mfsNew;
	}
	
	public boolean isUsableRule(String rule) {
		return ruleFromString(rule) != null;
	}
	
	public String addFuzzyRule(String rule) {
		FuzzyRule rR;
		try {
			FuzzyRule fr = ruleFromString(rule);
			if (fr == null) {
				return "Rule: " + rule + ", was not added as it is invalid";
			}
			int i;
			if ((i = this.fuzzyRules.indexOf(fr)) >= 0) {
				rR = this.fuzzyRules.remove(i);
				this.fuzzyRules.add(fr);
				return "Replaced rule: " + rR + ", with rule: " + fr;
			} else {
				this.fuzzyRules.add(fr);
				return "Added rule: " + fr.toString();
			}
		} catch (Exception e) {
			return "Rule: " + rule + ", was not added as it is invalid";
		}
	}
	
	public String removeFuzzyRule(String rule) {
		try {
			FuzzyRule fr = ruleFromString(rule);
			int i;
			if ((i = this.fuzzyRules.indexOf(fr)) >= 0) {
				return "Removed fuzzy rule: " + this.fuzzyRules.remove(i).toString();
			} else {
				return "Rule: " + rule + ", was not removed as it is not in the current list";
			}
		} catch (Exception e) {
			return "Rule: " + rule + ", was not removed as it is invalid";
		}
	}
	
	private static FuzzyRule ruleFromString(String rule) {
		try {
			String[] a = rule.split(" THEN force IS ");
			String[] b = a[0].split("IF ");
			String d[];
			if (b.length == 2) {
				d = b[1].split(" IS ", 2);
			} else {
				d = b[0].split(" IS ", 2);
			}
			String[] e = d[1].split(" IS ", 2);
			double outVal = 0.0;
			if (a.length == 2) {
				outVal = Double.valueOf(a[1]);
			}
			String inVar = d[0];
			boolean not;
			String inHedge;
			String inFunc;
			FuzzyRule aggregateRule = null;
			Aggregate aggregate = null;
			if (e.length == 1) {
				String[] f = d[1].split(" ");
				if (f.length == 3) {
					not = true;
					inHedge = f[1];
					inFunc = f[2];
				} else {
					not = false;
					inHedge = f[0];
					inFunc = f[1];
				}
			} else {
				String[] f = e[0].split(" ");
				if (f.length == 7) {
					not = true;
					inHedge = f[1];
					inFunc = f[2];
					aggregateRule = ruleFromString(f[4] + " " + f[5] + " " + f[6] + " IS " + e[1]);
					aggregate = Aggregate.getAggregate(f[3]);
				} if (f.length == 6 && f[0].equals("NOT")) {
					not = true;
					inHedge = f[1];
					inFunc = f[2];
					aggregateRule = ruleFromString(f[4] + " " + f[5] + " IS " + e[1]);
					aggregate = Aggregate.getAggregate(f[3]);
				} else if (f.length == 6) {
					not = false;
					inHedge = f[0];
					inFunc = f[1];
					aggregateRule = ruleFromString(f[3] + " " + f[4] + " " + f[5] + " IS " + e[1]);
					aggregate = Aggregate.getAggregate(f[2]);
				} else {
					not = false;
					inHedge = f[0];
					inFunc = f[1];
					aggregateRule = ruleFromString(f[3] + " " + f[4] + " IS " + e[1]);
					aggregate = Aggregate.getAggregate(f[2]);
				}
				if (aggregateRule == null) {
					return null;
				}
			}
			if (!FuzzyVariable.isValid(inVar) || !Hedge.isValid(inHedge) || !MemberFunction.isValid(inFunc)) {
				return null;
			}
			return new FuzzyRule(inVar, inHedge, inFunc, outVal, not, aggregateRule, aggregate);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void editMemberFunction(String name, double start, double firstPeek, double secondPeek, double end) {
		int i = Collections.binarySearch(this.fuzzySet.memberFunctions, new MemberFunction(name));
		if (i >= 0) {
			MemberFunction mf = this.fuzzySet.memberFunctions.get(i);
			mf.start = start;
			mf.firstPeek = firstPeek;
			mf.secondPeek = secondPeek;
			mf.end = end;
			this.data.addLog("Member function: " + name + ", redefined to: start= " + start + ", first peek= " + firstPeek + ", second peek= " + secondPeek + ", end= " + end);
		} else {
			this.data.addLog("Member function: " + name + ", does not exist so cannot be edited");
		}
	}
	
	public void editHedge(String name, double start, double firstPeek, double secondPeek, double end) {
		int i = -1;
		if ((i = Collections.binarySearch(this.fuzzySet.hedges, new Hedge(name))) >= 0) {
			Hedge h = this.fuzzySet.hedges.get(i);
			h.start = start;
			h.firstPeek = firstPeek;
			h.secondPeek = secondPeek;
			h.end = end;
			this.data.addLog("Hedge: " + name + ", redefined to: start= " + start + ", first peek= " + firstPeek + ", second peek= " + secondPeek + ", end= " + end);
		} else {
			this.data.addLog("Hedge: " + name + ", does not exist so cannot be edited");
		}
	}
	
	public static class FuzzyRule implements Comparable<FuzzyRule> {
		private String inVar;
		private String inHedge;
		private String inFunc;
		private double outVal;
		private boolean not;
		private FuzzyRule aggregateRule;
		private Aggregate aggregate;
		
		private FuzzyRule(String inVar, String inHedge, String inFunc, double outVal, boolean not, FuzzyRule aggregateRule, Aggregate aggregate) {
			this.inVar = inVar;
			this.inHedge = inHedge;
			this.inFunc = inFunc;
			this.outVal = outVal;
			this.not = not;
			this.aggregateRule = aggregateRule;
			this.aggregate = aggregate;
		}
		
		@Override
		public String toString() {
			return "IF " + inVar + " IS " + (not ? "NOT " : "") + inHedge + " " + inFunc + " " + (aggregateRule != null ? aggregate + " " + aggregateRule.toAggregateString() : "") + "THEN force IS " + outVal;
		}
		
		public String toAggregateString() {
			return inVar + " IS "+ (not ? "NOT " : "") + inHedge + " " + inFunc + " " + (aggregateRule != null ? aggregate + " " + aggregateRule.toAggregateString() : "");
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FuzzyRule) {
				FuzzyRule fr = (FuzzyRule)obj;
				return this.inVar.equals(fr.inVar) && this.inHedge.equals(fr.inHedge) && this.inFunc.equals(fr.inFunc) && this.not == fr.not && (this.aggregate != null ? (fr.aggregate != null ? this.aggregate.equals(fr.aggregate) && this.aggregateRule.equals(fr.aggregateRule) : false) : fr.aggregate == null);
			} else if (obj instanceof String) {
				return ruleFromString((String)obj) != null && ruleFromString((String)obj).equals(this);
			} else {
				return super.equals(obj);
			}
		}
		
		@Override
		public int compareTo(FuzzyRule o) {
			int res = 0;
			return (res = this.inVar.compareTo(o.inVar)) != 0 ? res : (res = this.inHedge.compareTo(o.inHedge)) != 0 ? res : this.inFunc.compareTo(o.inFunc);
		}
	}
	
	private static abstract class Aggregate {
		public static final Aggregate AND = new Aggregate("AND"){
			@Override
			protected double applyAggregate(double var1, double var2) {
				return var1 < var2 ? var1 : var2;
			}
		};
		public static final Aggregate NAND = new Aggregate("NAND"){
			@Override
			protected double applyAggregate(double var1, double var2) {
				return 1 - (var1 < var2 ? var1 : var2);
			}
		};
		public static final Aggregate OR = new Aggregate("OR"){
			@Override
			protected double applyAggregate(double var1, double var2) {
				return var1 > var2 ? var1 : var2;
			}
		};
		public static final Aggregate NOR = new Aggregate("NOR"){
			@Override
			protected double applyAggregate(double var1, double var2) {
				return 1 - (var1 > var2 ? var1 : var2);
			}
		};
		
		private final String name;
		
		private Aggregate(String name) {
			this.name = name;
		}
		
		public static Aggregate getAggregate(String ag) throws Exception {
			switch(ag) {
			case "AND":
				return AND;
			case "NAND":
				return NAND;
			case "OR":
				return OR;
			case "NOR":
				return NOR;
			}
			throw new Exception();
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Aggregate) {
				return this.getClass() == ((Aggregate)o).getClass();
			} else {
				return false;
			}
		}
		
		protected abstract double applyAggregate(double var1, double var2);
	}

	private static class FuzzySet {
		private FuzzyCartAndPendulumSystem sys;
		private ArrayList<FuzzyVariable> variables;
		private ArrayList<MemberFunction> memberFunctions;
		private ArrayList<Hedge> hedges;

		private FuzzySet(FuzzyCartAndPendulumSystem sys) {
			this.sys = sys;
			this.variables = new ArrayList<>();
			for (int i = 0; i < DEFAULT_FUZZY_VARIABLES.length; i++) {
				this.variables.add(DEFAULT_FUZZY_VARIABLES[i]);
			}
			Collections.sort(this.variables);
			this.memberFunctions = new ArrayList<>();
			for (int i = 0; i < DEFAULT_MEMBER_FUNCTIONS.length; i++) {
				this.memberFunctions.add(DEFAULT_MEMBER_FUNCTIONS[i]);
			}
			Collections.sort(this.memberFunctions);
			this.hedges = new ArrayList<>();
			for (int i = 0; i < DEFAULT_HEDGES.length; i++) {
				this.hedges.add(DEFAULT_HEDGES[i]);
			}
			Collections.sort(this.hedges);
		}
		
		private double fuzzy_if(FuzzyRule fr) {
			FuzzyVariable fv = this.variables.get(Collections.binarySearch(this.variables, new FuzzyVariable(fr.inVar)));
			double var;
			if (fv.equals("cart position") && !this.sys.data.ignoreCart()) {
				if (sys.useNoise()) {
					var = sys.getCartPositionWithNoise();
				} else {
					var = sys.getCartPosition();
				}
			} else if (fv.equals("cart velocity") && !this.sys.data.ignoreCart()) {
				if (sys.useNoise()) {
					var = sys.getCartVelocityWithNoise();
				} else {
					var = sys.getCartVelocity();
				}
			} else if (fv.equals("pendulum angle")) {
				if (sys.useNoise()) {
					var = sys.getPendulumAngleWithNoise();
				} else {
					var = sys.getPendulumAngle();
				}
			} else if (fv.equals("pendulum angular velocity")) {
				if (sys.useNoise()) {
					var = sys.getPendulumAngularVelocityWithNoise();
				} else {
					var = sys.getPendulumAngularVelocity();
				}
			} else {
				return 0.0;
			}
			double x = (var - fv.minVal) / (fv.maxVal - fv.minVal);
			double temp = this.memberFunctions.get(Collections.binarySearch(this.memberFunctions, new MemberFunction(fr.inFunc))).fuzzify(x, this.hedges.get(Collections.binarySearch(this.hedges, new Hedge(fr.inHedge))));
			temp = fr.not ? 1.0 - temp : temp;
			temp = fr.aggregate != null ? fr.aggregate.applyAggregate(temp, this.fuzzy_if(fr.aggregateRule)) : temp;
			return temp;
		}
	}
	
	public static class FuzzyVariable implements Comparable<FuzzyVariable> {
		private final String name;
		private double minVal;
		private double maxVal;

		public FuzzyVariable(String name, double minVal, double maxVal) {
			this.name = name;
			this.minVal = minVal;
			this.maxVal = maxVal;
		}
		
		public FuzzyVariable(String name) {
			this.name = name;
		}
		
		public static boolean isValid(String fv) {
			for (int i = 0; i < DEFAULT_FUZZY_VARIABLE_NAMES.length; i++) {
				if (fv.equals(DEFAULT_FUZZY_VARIABLE_NAMES[i])) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FuzzyVariable) {
				return this.name.equals(((FuzzyVariable)obj).name);
			} else if (obj instanceof String) {
				return this.name.equals(((String)obj));
			} else {
				return super.equals(obj);
			}
		}
		
		@Override
		public int compareTo(FuzzyVariable o) {
			return this.name.compareTo(o.name);
		}
		
		public String getName() {
			return name;
		}

		public double getMinVal() {
			return minVal;
		}

		public double getMaxVal() {
			return maxVal;
		}
	}
	
	public static class MemberFunction implements Comparable<MemberFunction> {
		private final String name;
		private double start;
		private double firstPeek;
		private double secondPeek;
		private double end;
		
		public MemberFunction(String name, double start, double firstPeek, double secondPeek, double end) {
			this.name = name;
			this.start = start;
			this.firstPeek = firstPeek;
			this.secondPeek = secondPeek;
			this.end = end;
		}
		
		public MemberFunction(String name) {
			this.name = name;
		}
		
		public static boolean isValid(String mf) {
			for (int i = 0; i < DEFAULT_MEMBER_FUNCTION_NAMES.length; i++) {
				if (mf.equals(DEFAULT_MEMBER_FUNCTION_NAMES[i])) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MemberFunction) {
				return this.name.equals(((MemberFunction)obj).name);
			} else if (obj instanceof String) {
				return this.name.equals(((String)obj));
			} else {
				return super.equals(obj);
			}
		}
		
		@Override
		public int compareTo(MemberFunction o) {
			return this.name.compareTo(o.name);
		}
		
		public double fuzzify(double x, Hedge h) {
			if (x >= firstPeek && x <= secondPeek) {
				return h.applyHedge((x - start) / (end - start));
			} else if (x < start || x > end) {
				return 0.0;
			} else if (x < firstPeek) {
				return Aggregate.AND.applyAggregate((x - start) / (firstPeek - start), h.applyHedge((x - start) / (end - start)));
			} else {
				return Aggregate.AND.applyAggregate(1.0 - ((x - secondPeek) / (end - secondPeek)), h.applyHedge((x - start) / (end - start)));
			}
		}
		
		public double fuzzify(double x) {
			if (x >= firstPeek && x <= secondPeek) {
				return 1.0;
			} else if (x < start || x > end) {
				return 0.0;
			} else if (x < firstPeek) {
				return (x - start) / (firstPeek - start);
			} else {
				return 1.0 - ((x - secondPeek) / (end - secondPeek));
			}
		}
		
		public String getName() {
			return name;
		}

		public double getStart() {
			return start;
		}

		public double getFirstPeek() {
			return firstPeek;
		}

		public double getSecondPeek() {
			return secondPeek;
		}

		public double getEnd() {
			return end;
		}
	}
	
	public static class Hedge implements Comparable<Hedge> {
		private final String name;
		private double start;
		private double firstPeek;
		private double secondPeek;
		private double end;
		
		public Hedge(String name, double start, double firstPeek, double secondPeek, double end) {
			this.name = name;
			this.start = start;
			this.firstPeek = firstPeek;
			this.secondPeek = secondPeek;
			this.end = end;
		}
		
		public Hedge(String name) {
			this.name = name;
		}
		
		public static boolean isValid(String h) {
			for (int i = 0; i < DEFAULT_HEDGE_NAMES.length; i++) {
				if (h.equals(DEFAULT_HEDGE_NAMES[i])) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Hedge) {
				return this.name.equals(((Hedge)obj).name);
			} else if (obj instanceof String) {
				return this.name.equals(((String)obj));
			} else {
				return super.equals(obj);
			}
		}
		
		@Override
		public int compareTo(Hedge o) {
			return this.name.compareTo(o.name);
		}
		
		public double applyHedge(double x) {
			if (x >= firstPeek && x <= secondPeek) {
				return 1.0;
			} else if (x < start || x > end) {
				return 0.0;
			} else if (x < firstPeek) {
				return (x - start) / (firstPeek - start);
			} else {
				return 1 - ((x - secondPeek) / (end - secondPeek));
			}
		}
		
		public String getName() {
			return name;
		}
		
		public double getStart() {
			return start;
		}

		public double getFirstPeek() {
			return firstPeek;
		}

		public double getSecondPeek() {
			return secondPeek;
		}

		public double getEnd() {
			return end;
		}
	}
}
