package mr.zhang.databaseoperation.anno.sqlbuilder;

import mr.zhang.databaseoperation.anno.db.Dian;

public class OrderByBuilder {
	private StringBuilder sb;
	private OrderByBuilder(){
		sb = new StringBuilder();
	}
	private static final String DIAN = Dian.DIAN;
	
	public static OrderByBuilder create(String[] columnNames,Operation[] operations) {
		OrderByBuilder wb = new OrderByBuilder();
		final StringBuilder sb = wb.sb;
		// ƴ��where
		sb.append("ORDER BY ");
		for (int i = 0; i < columnNames.length; i++) {
			if (i != columnNames.length - 1) {
				sb.append(columnNames[i]+" "+operations[i].getOper()+", ");
			} else {
				sb.append(columnNames[i]+" "+operations[i].getOper()+" ");
			}
		}
		return wb;
	}
	
	public String getSql(){
		return sb.toString();
	}
	
	public enum Operation{
		DESC("DESC"),
		ASC("ASC");
		
		private String mOper;
		private Operation(String oper) {
			mOper = oper;
		}
		
		public String getOper() {
			return mOper;
		}
	}
}
