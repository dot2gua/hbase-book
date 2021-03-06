package client;

// cc DeleteExample Example application deleting data from HBase
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.util.Bytes;

import util.HBaseHelper;

public class DeleteExample {

  public static void main(String[] args) throws IOException {
    Configuration conf = HBaseConfiguration.create();

    HBaseHelper helper = HBaseHelper.getHelper(conf);
    helper.dropTable("testtable");
    helper.createTable("testtable", 100, "colfam1", "colfam2");
    helper.put("testtable",
      new String[] { "row1" },
      new String[] { "colfam1", "colfam2" },
      new String[] { "qual1", "qual1", "qual2", "qual2", "qual3", "qual3" },
      new long[]   { 1, 2, 3, 4, 5, 6 },
      new String[] { "val1", "val1", "val2", "val2", "val3", "val3" });
    System.out.println("Before delete call...");
    helper.dump("testtable", new String[]{ "row1" }, null, null);

    HConnection connection = HConnectionManager.createConnection(conf);
    HTableInterface table = connection.getTable(TableName.valueOf("testtable"));

    // vv DeleteExample
    Delete delete = new Delete(Bytes.toBytes("row1")); // co DeleteExample-1-NewDel Create delete with specific row.

    delete.setTimestamp(1); // co DeleteExample-2-SetTS Set timestamp for row deletes.

    delete.deleteColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1")); // co DeleteExample-3-DelColNoTS Delete the latest version only in one column.
    delete.deleteColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual3"), 3); // co DeleteExample-4-DelColTS Delete specific version in one column.

    delete.deleteColumns(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1")); // co DeleteExample-5-DelColsNoTS Delete all versions in one column.
    delete.deleteColumns(Bytes.toBytes("colfam1"), Bytes.toBytes("qual3"), 2); // co DeleteExample-6-DelColsTS Delete the given and all older versions in one column.

    delete.deleteFamily(Bytes.toBytes("colfam1")); // co DeleteExample-7-AddCol Delete entire family, all columns and versions.
    delete.deleteFamily(Bytes.toBytes("colfam1"), 3); // co DeleteExample-8-AddCol Delete the given and all older versions in the entire column family, i.e., from all columns therein.

    table.delete(delete); // co DeleteExample-9-DoDel Delete the data from the HBase table.

    // ^^ DeleteExample
    table.close();
    connection.close();
    System.out.println("After delete call...");
    helper.dump("testtable", new String[] { "row1" }, null, null);
    helper.close();
  }
}
