public class IslandTest {
//    private final Connection connection = DatabaseManager.getConnection();
//
//    public IslandTest() throws SQLException {}
//
//    @Test
//    public void randomTest() throws ExecutionException, InterruptedException {
//        DatabaseHelper.prepareStatement("CREATE TABLE IF NOT EXISTS test(not_text Text)", preparedStatement -> {
//            try {
//                preparedStatement.execute();
//            } catch (SQLException exception) {
//                exception.printStackTrace();
//            }
//        }).get();
//    }
//
//    @Test
//    public void newLibraryTest() throws SQLException, ExecutionException, InterruptedException {
//        final JdbcMapper<Island> mapper = IslandDatabase.ISLAND_DATABASE.getMapper();
//
//        try (final PreparedStatement statement = connection.prepareStatement("select * from islands")) {
//            final ResultSet resultSet = statement.executeQuery();
//            mapper.forEach(resultSet, System.out::println);
//        }
//
//        final IslandDatabase islandDatabase = IslandDatabase.ISLAND_DATABASE;
//
//        islandDatabase.create(new Island(22, "testCategory")).get();
//        Island foundIsland = islandDatabase.read(22).get();
//        System.out.println(foundIsland);
//
//        foundIsland.setCategory("anotherCategory");
//
//        islandDatabase.update(foundIsland).get();
//        System.out.println(islandDatabase.read(22).get());
//
//        islandDatabase.delete(22).get();
//        System.out.println(islandDatabase.read(22).get());
//    }
//
//    @Test
//    public void createTable() throws SQLException {
//        final StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS islands(")
//                .append("slot INTEGER NOT NULL PRIMARY KEY")
//                .append(',')
//                .append("category TEXT")
//                .append(')');
//
//        connection.prepareStatement(builder.toString()).execute();
//    }
//
//    @Test
//    public void selectIslands() throws SQLException {
//        connection.prepareStatement("SELECT * FROM islands").execute();
//    }
}
