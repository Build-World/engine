package com.buildworld.game.mod;

public interface IMod {

    String getKey();
    String getName();
    String getDescription();
    String getVersion();

    default void onBoot() throws Exception {}
    default void onLoad() throws Exception {}
    default void onReady() throws Exception {}
    default void onPlay() throws Exception {}
    default void onEnable() throws Exception {}
    default void onDisable() throws Exception {}
    default void onTick() throws Exception {}
    default void onDraw() throws Exception {}

//    default ArrayList<DatabaseModel> getDatabaseModels()
//    {
//        return new ArrayList<>();
//    }
//    default ArrayList<Command> getCommands()
//    {
//        return new ArrayList<>();
//    }
//    default ArrayList<PostDatabaseLoad> getPostDatabaseLoads()
//    {
//        return new ArrayList<>();
//    }
//    default ArrayList<PlayerData> getPlayerDatas()
//    {
//        return new ArrayList<>();
//    }
//    default ArrayList<Recipe> getRecipes()
//    {
//        return new ArrayList<>();
//    }
//    default ArrayList<ScoreboardSection> getScoreboardSections()
//    {
//        return new ArrayList<>();
//    }
//    default ArrayList<PermissionNode> getPermissionNodes()
//    {
//        return new ArrayList<>();
//    }
//    default LinkedList<ConfigurationProperty> getConfigurationProperties()
//    {
//        return new LinkedList<>();
//    }
}
