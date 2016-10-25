# 数据库操作工具类

## 使用方法 ##

* 具体流程：
  * 在activity中，先定义一个静态变量，也可以在application中定义，如下：
- 先创建一个集合，用来保存实体类.class。
        private static final List<Class> classes = new ArrayList<Class>(){
          {
            add(Classs.class);
            add(Result.class);
          }
        };

- 接下来，要创建配置参数了，就像这样：
      DBManager.DaoConfig daoConfig = new DBManager.DaoConfig()
      .setDbVersion(1)
            .setDbName("test.db")
            .setDbClass(classes)
            .setContext(this)
            .setUpdateCallBack(new DbUpdateCallBack() {
                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                }
            });

  * 参数配置好，这样就会创建出一个版本号为1，数据库文件名为test.db的数据库，并且在此时，会自动创建数据表。


- 修改初始化的集合，如下：
    private static final List<Class> classes = new ArrayList<Class>(){
        {
            add(Classs.class);
            add(Result.class);
            // 在这里追加一个
            add(House.class);
        }
      };

- 修改配置，只修改一个位置：
      .setDbVersion(2)

   * 此时，数据库版本升级了，因为追加了集合数据，因此会自动创建House这个实体类的数据表


- 说到增加，应该联想起删除，但是很抱歉，删除操作同样需要追加数据版本号，但只能在回调方法内删除，如：
      .setDbVersion(3)
      // 省略部分
      .setUpdateCallBack(new DbUpdateCallBack() {
          @Override
          public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)      
              {
                db.execSQL("drop table house;");
              }
          });

    * 这样，升级版本号的同时，把house这个数据表删除了（结构删除，数据也就没了）

### 以下说一说如何增删改查 ###
###### 本次实例主要是在onCreate方法里面操作

      @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBManager manager = SQLFactory.getDbControl(daoConfig);
      }

  * 通过DBManager这个类，完成了数据库操作之前的初始化。

### 定义类House，如下
    @TableName(tableName = "HOUSE")
    public class House {
      @DBColumn(columnName = "_ID" ,isId = true)
      private String _id;
      @DBColumn(columnName = "HOUST_ID",isUnique = true)
      private String houseId;
      @DBColumn(columnName = "MASTER")
      private String master;

      public House(){}

      public House(String houseId, String master) {
        this._id = UUID.randomUUID().toString();
        this.houseId = houseId;
        this.master = master;
      }

      // 一堆set、get省略
     }

  * 此实体类，将会在数据库创建或者更新的时候，生成对应的sql语句，并且根据sql语句生成表，sql语句如下：

        create table HOUSE(
          _ID text,
          HOUST_ID text NUIQUE KEY,
          MASTER text,
          PRIMARY KEY('_ID')
        );


- 创建一条数据
  * 在上述代码DBManager manager = SQLFactory.getDbControl(daoConfig);接着写：

        House house = new House("1234","github");
        try {
            manager.createOrUpdate(house);
        } catch (Exception e) {
            e.printStackTrace();
        }

    * 此时就会生成一条house的数据表


- 创建多条数据
       List<House> houses = new ArrayList<>();
       houses.add(new House("123","github"));
        houses.add(new House("124","giThub"));
        houses.add(new House("125","gitHub"));
        try {
            manager.createOrUpdate(houses);
        } catch (Exception e) {
            e.printStackTrace();
        }

    * 此时就会生成多条house的数据表

- 创建后修改数据内容

      House house = new House("1234","github");
      manager.createOrUpdate(house);//创建

      house.setMaster("hahaha");
        house.setHouseId("+++++");

        // 修改
        manager.createOrUpdate(house);

- 删除单条数据表

        House house = new House("1234","github");
        manager.createOrUpdate(house);//创建

        manager.delete(house);//删除


- 删除多条数据表

        List<House> houses = new ArrayList<>();
        houses.add(new House("123","github"));
         houses.add(new House("124","giThub"));
         houses.add(new House("125","gitHub"));

         manager.createOrUpdate(houses);//创建

         manager.delete(houses);//删除

### 最后一个点，查询，这个挺有意思的，写几个出来

    - 先创建9条数据


          List<Result> results = new ArrayList<>();
          results.add(new Result("12110","大小","29","54","120"));
          results.add(new Result("12111","小大","26","76","19"));
          results.add(new Result("12112","大大","25","44","2"));
          results.add(new Result("12113","大三","25","97","99"));
          results.add(new Result("12114","小妞","23","76","100"));
          results.add(new Result("12115","小三","22","60","60"));
          results.add(new Result("12116","小张","20","100","120"));
          results.add(new Result("12117","大张","25","54","20"));
          results.add(new Result("12118","大大","20","44","120"));

    - 入库
          manager.createOrUpdate(results);

   * 查询

    - 此条必须放在一开始
            manager.selector(House.class)


- 查询例子1：
          List<Result> all = manager
          .selector(Result.class)
          .where("NAME", WhereBuilder.Operation.EQUALS, "大小")
          .findAll();

  *   查找名字等于大小的全部House


-  查询例子2：

          List<Result> all1 = manager.selector(Result.class)
          .order(new String[]{"CHINESE"}, new OrderByBuilder.Operation[]{OrderByBuilder.Operation.DESC})
          .limit(2, 5).findAll();

  * 根据语文成绩降序，从第三条开始共查找5条。

-  查询例子3：

        List<Result> all2 = manager.selector(Result.class).limit(1, 4).findAll();
  * 按照插入顺序，从第二条开始查询，共查询4条

-  查询例子4：


          List<Result> all3 = manager.selector(Result.class)
          .where("CHINESE", WhereBuilder.Operation.BETWEEN, "50", "99")
          .next(WhereBuilder.Symbol.AND, "MATH", WhereBuilder.Operation.MORE, "60")
          .next(WhereBuilder.Symbol.AND, "NAME", WhereBuilder.Operation.LIKE, "大_").findAll();

  *  查询语文成绩在50和99直接，数学成绩大于60，并且 并且以“大”开始，并且“大”后面仅有一个字符的结果。
