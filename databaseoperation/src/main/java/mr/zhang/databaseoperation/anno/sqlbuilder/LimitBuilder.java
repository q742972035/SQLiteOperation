package mr.zhang.databaseoperation.anno.sqlbuilder;

public class LimitBuilder {
	private String sql;
	private LimitBuilder(){}
	public static LimitBuilder create(int start,int counts){
		LimitBuilder limitBuilder = new LimitBuilder();
		limitBuilder.sql = "limit "+start+","+counts;
		return limitBuilder;
	}
	public String getSql(){
		return sql;
	}
}
