package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbUtils {

    private static final String DB_URL = "jdbc:postgresql://dac-hawkeye-database-nonprod.clnio2au1rmq.ap-southeast-1.rds.amazonaws.com:5432/hawkeye_db";
    private static final String DB_USER = "hawkeye_user";
    private static final String DB_PASSWORD = "GIT2UsI=2s9=3ljiBRo_";

    /**
     * Executes a clean filtered query to pull data for a specific campaign name.
     * @param campaignTitle The precise text token inside the table row
     * @return A map containing key-value data rows pulled straight from the table schema
     */
    public static Map<String, String> getCampaignMetadataByTitle(String campaignTitle) {
        Map<String, String> campaignDataMap = new HashMap<>();

        // Target SQL statement: Filter for the targeted record rows using a secure placeholder
        String selectSqlQuery = "SELECT name, created_at, updated_at, status FROM uat.campaigns WHERE name = ?;";

        // Automatically open connections and clean up system assets when finished
        try (Connection dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSqlQuery)) {

            // Inject the input parameter safely into the placeholder injection shield
            preparedStatement.setString(1, campaignTitle);

            try (ResultSet databaseResultPayload = preparedStatement.executeQuery()) {
                if (databaseResultPayload.next()) {
                    // Pull specific columns out of the target schema layer
                    campaignDataMap.put("title", databaseResultPayload.getString("name"));
                    campaignDataMap.put("last_updated", databaseResultPayload.getString("updated_at"));
                    campaignDataMap.put("date_created", databaseResultPayload.getString("created_at"));
                    campaignDataMap.put("status", databaseResultPayload.getString("status"));
                } else {
                    System.out.println("⚠️ DB WARNING: No database row records discovered for campaign: " + campaignTitle);
                }
            }
        } catch (SQLException exceptionContext) {
            System.err.println("❌ CRITICAL: PostgreSQL JDBC Connection Error execution crash: " + exceptionContext.getMessage());
        }

        return campaignDataMap;
    }
}
