package mr.zhang.databaseoperation.anno.sqlbuilder;

import mr.zhang.databaseoperation.anno.db.Dian;

public class WhereBuilder {
	private StringBuilder sb;
	private WhereBuilder(){
		sb = new StringBuilder();
	}
	private static final String DIAN = Dian.DIAN;

	/**
	 * 用于创建where的开头写法
	 * @param columnName 列名
	 * @param operation 操作符
	 * @param assignment 赋值或者其他
	 * @return
	 */
	public static WhereBuilder create(String columnName, Operation operation,
									  String...assignment) {
		WhereBuilder wb = new WhereBuilder();
		final StringBuilder sb = wb.sb;
		// 拼接where
		sb.append("where ");
		wb.pinJie(columnName, operation, assignment);
		return wb;
	}



	public WhereBuilder next(Symbol symbol, String columnName, Operation operation,
							 String...assignment){
		sb.append(symbol.getOper() + " ");
		pinJie(columnName, operation, assignment);
		return this;
	}

	public String getSql(){
		return sb.toString();
	}

	private void pinJie(String columnName, Operation operation, String... assignment) {
		// 拼接条件
		String opera = operation.getOper();
		sb.append(columnName + " " + opera + " ");
		// 如果属于符号判断
		if (operation == Operation.EQUALS || operation == Operation.NOTEQUALS || operation == Operation.LESS
				|| operation == Operation.MORE || operation == Operation.LESSEQUALS
				|| operation == Operation.MOREEQUALS||operation == Operation.LIKE) {
			sb.append(DIAN + assignment[0] + DIAN + " ");
		} else if (operation == Operation.BETWEEN) {
			sb.append(DIAN + assignment[0] + DIAN + " " + "AND" + " " + DIAN + assignment[1] + DIAN + " ");
		} else if (operation == Operation.IN) {
			sb.append("(");
			for (int i = 0; i < assignment.length; i++) {
				// 不是最后一个
				if (i != assignment.length - 1) {
					sb.append(DIAN + assignment[i] + DIAN + ",");
				} else {
					sb.append(DIAN + assignment[i] + DIAN + ") ");
				}
			}
		}
	}

	/**
	 * 操作符
	 */
	public enum Operation{
		EQUALS("="),
		NOTEQUALS("<>"),
		LESS("<"),
		MORE(">"),
		LESSEQUALS("<="),
		MOREEQUALS(">="),
		BETWEEN("BETWEEN"),
		IN("IN"),
		ISNULL("IS NULL"),
		ISNOTNULL("IS NOT NULL"),
		LIKE("LIKE");


		private String mOper;
		private Operation(String oper) {
			mOper = oper;
		}

		public String getOper() {
			return mOper;
		}
	}

	/**
	 * 运算符
	 */
	public enum Symbol{
		AND("AND"),
		OR("OR");

		private String mOper;
		private Symbol(String oper) {
			mOper = oper;
		}

		public String getOper() {
			return mOper;
		}
	}

}
